package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import grails.converters.JSON
class LogoutController {
    def baseService
    def index() {
    	if (session.employee){
            baseService.insertLoginLog(SysConst.LOGIN_TYPE_LOGOUT)
            session.removeAttribute("user")
            session.removeAttribute("employee")
        }
        def json = [success:true,msg:'注销成功！'] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AdminAuthAnnotation
    def admin(){
        if (session.admin){
            session.removeAttribute("admin")
        }
        def json = [success:true,msg:'注销成功！'] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AgentAuthAnnotation
    def agent(){
        if (session.agent){
            session.removeAttribute("agent")
        }
        def json = [success:true,msg:'注销成功！'] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }
}
