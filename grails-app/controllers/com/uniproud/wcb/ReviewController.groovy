package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
class ReviewController {

    def baseService
    def modelService

    def list() {
        def extraCondition = {
        }
        render modelService.getDataJSON('review',extraCondition)
    }

    @Transactional
    def insert(Review review){
        review.properties['user'] = session.employee?.user
        review.properties['employee'] = session.employee
        def module = Module.findByModuleId(params.get('module.moduleId'))
        review.module = module
        render baseService.save(params,review)
    }

    @Transactional
    def update(Review review){
        render baseService.save(params,review)
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        Review.executeUpdate("update Review set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    @Transactional
    def replyList(){
        def review = Review.get(params.review as Long)
        def replys = []
        review?.replys?.each {rep->
            replys << [employee:[name:rep.employee?.name],content:rep.content,dateCreated:rep.dateCreated]
        }
        def json = [success: true, data: replys, total: review?.replys?.size()] as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

}
