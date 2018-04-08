package com.uniproud.wcb

import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class LoginLogController {
	
	def modelService

    def list(){
        def extraCondition = {
            eq("user",session.employee?.user)
        }
		render modelService.getDataJSON('login_log',extraCondition)
    }

    @AgentAuthAnnotation
    def listForAgent(){
        def users = User.where {
            agent == session.agent
        }.list()
        RequestUtil.pageParamsConvert(params)
        def query = LoginLog.where{
            if(users){
                inList("user",users)
            }
        }
        def datas = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.LoginLog');
        query.list(params).each {
            def data = [:]
            fields.each{field->
                data << [(field.fieldName):it."${field.fieldName}"]
            }
            data << [userName:it.user?.name,employeeName:it.employee.name]
            datas << data
        }
        def json = [success:true,data:datas, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

}
