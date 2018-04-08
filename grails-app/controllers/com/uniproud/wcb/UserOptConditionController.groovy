package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@AgentAuthAnnotation
@Transactional(readOnly = true)
class UserOptConditionController {
    def baseService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = UserOptCondition.where{
            userOperation {
                id == params.userOperation?.toLong()
            }
            user{
                id == params.user?.toLong()
            }
        }
        def userOptConditions = []
        query.list(params).each {
            userOptConditions << [
                id:it.id,name:it.name,remark:it.remark,userOperation:it.userOperation.id,
                userOperationName:it.userOperation.text,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,
            ]
        }
        def json = [success:true,data:userOptConditions, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(UserOptCondition userOptCondition) {
        if (userOptCondition == null) {
            render(successFalse)
            return
        }
        render baseService.save(params, userOptCondition)
    }
    @Transactional
    def update(UserOptCondition userOptCondition) {
        render baseService.save(params, userOptCondition)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ids.each{
            UserOptCondition.get(it.toLong()).delete()
        }
        render(successTrue )
    }

}
