package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class SfaExecuteController {

    def list(Integer max) {
        def user = session.employee?.user
        def result = SfaExecute.createCriteria().list(params) {
            eq("user", user)
            if(params.moduleId){
                module{
                    eq("moduleId",params.moduleId)
                }
            }
            if(params.linkId){
                eq("linkId",params.linkId as Long)
            }
            eq('deleteFlag',false)
        }
        def data = []
        result.each { rs ->
            data << [id: rs.id, sfa:[id:rs.sfa?.id,name:rs.sfa?.name,auto:rs.sfa?.auto,state:rs.sfa?.state],linkId:rs.linkId,state:rs.state,dateCreated:rs.dateCreated,lastUpdated:rs.lastUpdated]
        }
        def json = [success: true, data: data, total: result?.totalCount] as JSON
        render json
    }

    def eventExecuteList(){
        def user = session.employee?.user
        def result = SfaEventExecute.createCriteria().list(params){
            eq("user",user)
            if(params.sfaExecute){
                sfaExecute{
                    eq("id",params.sfaExecute as Long)
                }
            }
            if(params.sfaEvent){
                sfaEvent{
                    eq("id",params.sfaEvent as Long)
                }
            }
            eq('deleteFlag',false)
        }
        def data = []
        result.each { rs ->
            def employees = []
            rs.employees?.each {emp->
                employees << [id:emp.id,name: emp.name]
            }
            data << [id: rs.id, state:rs.state,receiverType:rs.sfaEvent?.receiverType,employees:employees,customer:[id:rs.customer?.id,name: rs.customer?.name],executeDate:rs.executeDate,isNotify:rs.isNotify,notifyResult:rs.notifyResult,
                     notifySubject:rs.notifySubject,notifyContent:rs.notifyContent,isSms:rs.isSms,smsResult:rs.smsResult,smsSubject:rs.smsSubject,smsContent:rs.smsContent,
                     isEmail:rs.isEmail,emailResult:rs.emailResult,emailSubject:rs.emailSubject,emailContent:rs.emailContent,dateCreated:rs.dateCreated,lastUpdated:rs.lastUpdated]
        }
        def json = [success: true, data: data, total: result?.totalCount] as JSON
        render json
    }
}
