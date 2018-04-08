package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@UserAuthAnnotation
@Transactional(readOnly = true)
class UserFeedbackController {
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = UserFeedback.where{
            if(params.userId){
                user.id == params.userId?.toLong()
            }
        }
        def userFeedbacks = []
        query.list(params).each {
            userFeedbacks << [id:it.id,kind:it.kind,content:it.content,user:it.user?.id,userName:it.user?.name,
                              employee:it.employee?.id,employeeName:it.employee?.name,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated]
        }
        def json = [success:true,data:userFeedbacks, total: query.count()] as JSON
        println(json)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(UserFeedback userFeedback) {
        log.info params
        if (userFeedback == null) {
            render([success:false] as JSON)
            return
        }
        userFeedback.user = session.employee?.user
        userFeedback.employee = session.employee
        if (!userFeedback.validate()) {
            render([success:false,errors: errorsToResponse(userFeedback.errors)] as JSON)
            return
        }
        userFeedback.save flush: true
        render([success:true] as JSON)
    }
}
