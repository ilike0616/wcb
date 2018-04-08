package com.uniproud.wcb

import grails.converters.JSON

class LoginController {
    def baseService
    def employeeService
	def index(){
            def employee = Employee.where {
                or{
                    if(params.account){
                        and{
                            user{
                                ilike('userId',params.userId)
                            }
                            ilike('account',params.account)
                        }
                    }
                    if(params.email){
                        and{
                            ilike('email',params.email)
                            eq('checkEmail',true)
                        }
                        and{
                            ilike('mobile',params.email)
                            eq('checkMobile',true)
                        }
                    }
                }
                ilike('password',params.password)
            }.find()
			def json
			if (employee){
                def user = employee.user
				session.employee = employee
				session.employee.user = user
                def am = employee.attendanceModel
                def token = ""
                if(params.loginKind=='2'){      //loginKind为2代表手机端登录
                    def nowDate = new Date()
                    token = "$user.userId;$employee.email;$employee.password;${params.macAddress}".encodeAsSHA1()
                    if(employee.token!=token) {
                        log.info("token :$token 变更 ")
                        employee.tokenDate = nowDate
                        employee.token = token
                        employee.save(flush: true)
                    }
                    if(!employee.qxuid){//如果没有企信账号，尝试创建
                        employeeService.registQX(employee)
                    }
                }
                def data = [sessionId:session.id,id:employee.id,name:employee.name,userName:user.name,token:token,serverTime:new Date().getTime(),qxuid:employee.qxuid,qxpwd:employee.qxpwd,jpushAlias:employee.jpushAlias,skin:employee.skin,isLocus:employee.isLocus]
                def jpushTag = []
                if(am) {
                    def attendanceModel = [id : am.id, longtitude: am.longtitude, latitude: am.latitude, maxDistance: am.maxDistance, ifMaxDistSign: am.ifMaxDistSign, timeMode: am.timeMode, location: am.location,
                                           startTime1: am.startTime1, endTime1: am.endTime1, startTime2: am.startTime2, endTime2: am.endTime2, havePictures: am.havePictures, otherEquSign: am.otherEquSign]
                    data << [attendanceModel: attendanceModel]
                    jpushTag << "AttendanceModel_${am.id}"
                }
                if(user.jpushTag) jpushTag << user.jpushTag
                if(employee.dept) jpushTag << employee.dept.jpushTag
                data << [jpushTag:jpushTag]
                json = [success:true,msg:'登陆成功',data:data] as JSON
                baseService.insertLoginLog(SysConst.LOGIN_TYPE_LOGIN)
                def agentEmail = employee.user?.agent?.email
                if(agentEmail){
                    def emailSubject = "${employee.user.name}的员工${employee.name}刚刚登陆系统！"
                    def emailContent = "${employee.name}刚刚登陆了系统！"
                    baseService.sendMail(agentEmail,emailSubject,emailContent)
                }
			}else{
				json = [success:false,msg:'登陆失败，用户名和密码不匹配',errors:[email:'登陆失败，帐号和密码不匹配']] as JSON
			}
			if(params.callback) {
				render "${params.callback}($json)"
			}else{
				render(json)
			}
	}

	def check(){
		def json
        def session = this.getSession()
		def employee = session.employee
		if (employee){
			json = [success:true,data:[id:employee.id,name:employee.name,email:employee.email,skin:employee.skin,userId:employee.user?.userId,account:employee.account]] as JSON
		}else{
			json = [success:false] as JSON
		}
		if(params.callback) {
			render "${params.callback}($json)"
		}else{
			render(json)
		}
	}

    def admin(){
        def admin = Administrator.where{
            adminId == params.adminId
            password == params.password
        }.find()
        def json
        if (admin){
            session.admin = admin
            def data = [id:admin.id,name:admin.name]
            json = [success:true,msg:'登陆成功',data:data] as JSON
            baseService.insertLoginLog(SysConst.LOGIN_TYPE_LOGIN)
        }else{
            json = [success:false,msg:'登陆失败，用户账号和密码不匹配',errors:[adminId:'登陆失败，帐号和密码不匹配']] as JSON
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def checkAdmin(){
        def json
        def admin = session.admin
        if (admin){
            json = [success:true,data:[id:admin.id,name:admin.name]] as JSON
        }else{
            json = [success:false] as JSON
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def agent(){
        def agent = Agent.where{
            agentId == params.agentId
            password == params.password
        }.find()
        def json
        if (agent){
            session.agent = agent
            def data = [id:agent.id,name:agent.name]
            json = [success:true,msg:'登陆成功',data:data] as JSON
            baseService.insertLoginLog(SysConst.LOGIN_TYPE_LOGIN)
        }else{
            json = [success:false,msg:'登陆失败，用户账号和密码不匹配',errors:[agentId:'登陆失败，帐号和密码不匹配']] as JSON
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def checkAgent(){
        def json
        def agent = session.agent
        if (agent){
            json = [success:true,data:[id:agent.id,name:agent.name]] as JSON
        }else{
            json = [success:false] as JSON
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def redirectUserDir(){
        redirect(url:"/u/")
    }

}
