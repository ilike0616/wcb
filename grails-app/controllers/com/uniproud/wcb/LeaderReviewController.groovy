package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
class LeaderReviewController {

    def baseService
    def modelService

    def list() {
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        emps << emp
        def extraCondition = {
            inList("owner", emps)
        }
        render modelService.getDataJSON('leader_review',extraCondition,true)
    }

    @Transactional
    def reply(Comment comment){
        comment.employee = session.employee
        if(!params.review || !comment.validate()){
            render([success:false,errors: errorsToResponse(comment.errors)] as JSON)
            return
        }
        baseService.save(params,comment)
        def review = Review.get(params.review as Long)
        review.addToReplys(comment);
        render baseService.save(params,review,'leader_review')
    }

    @Transactional
    def toRead(){
        def ids = JSON.parse(params.ids) as List
        Review.executeUpdate("update Review set isRead=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
}
