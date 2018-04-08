package com.uniproud.wcb

import grails.converters.JSON
import grails.events.Listener
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@Transactional
class EmployeeService {

    def getChildren(emps, emp) {
        emp.children.each {
            emps << it
            if (it.children) getChildren(emps, it)
        }
    }

    def static grailsApplication

    /**
     * 注册环信账户
     * @param user
     * @param employee
     */
    def registQX(employee) {
        def token = getToken()
        def http = new HTTPBuilder(baseUrl() + "/users")        //注册地址
        def username = employee.user.id + "_" + employee.id         //用户名：user.id_employee.id
        def password = grailsApplication.config.QX_DEFAULT_PASSWORD     //默认密码
        http.request(Method.POST, JSON) {
            requestContentType = ContentType.JSON
            headers = ["Authorization": "Bearer " + token]
            body = [username: username, password: password, nickname: employee.name]
            response.success = { resp, reader ->
                employee.qxuid = username
                employee.qxpwd = password
                employee.save()
            }
            response.failure = { resp, reader ->
                log.info "注册环信账号失败（$employee.name)：" + reader.text
            }
        }
    }

    def updateQXUsername(employee) {
        def token = getToken()
        def http = new HTTPBuilder(baseUrl() + "/users/" + employee.qxuid)
        http.request(Method.PUT, JSON) {
            requestContentType = ContentType.JSON
            headers = ["Authorization": "Bearer " + token]
            body = [nickname: employee.name]
            response.success = { resp, reader ->
            }
            response.failure = { resp, reader ->
                log.info "修改企信昵称失败（${employee?.name}）:" + reader?.text
            }
        }
    }

    def getToken() {
        def token
        def client_id = grailsApplication.config.QX_CLIENT_ID
        def client_secret = grailsApplication.config.QX_CLIENT_SECRET
        def http = new HTTPBuilder(baseUrl() + "/token")
        http.request(Method.POST, JSON) {
            requestContentType = ContentType.JSON
            body = [grant_type: 'client_credentials', client_id: client_id, client_secret: client_secret]
            response.success = { resp, reader ->
                token = new JsonSlurper().parseText(reader.text)
            }
        }
        token.access_token
    }

    def baseUrl() {
        def server = grailsApplication.config.QX_SERVER_HOST
        def app = grailsApplication.config.QX_APPKEY
        def url = server + "/" + app.replace("#", "/")
        url
    }

    /**
     * 绑定用户权限
     * @param employee
     * @param privilege
     */
    def bindEmployeePrivilege(employee, privilege) {
        def user = employee.user
        def userOperationQuery = UserOperation.where {
            user == user
        }
        def userOperationIds = []
        userOperationQuery.list()?.each { uo ->
            userOperationIds << uo.id
        }
        privilege.properties['userOperation'] = userOperationIds
        privilege.addToEmployees(employee)
        privilege.save(flush: true)
    }

    /**
     * 验证邮箱成功
     * @param info
     * @return
     */
    @Listener(topic = 'confirmed', namespace = 'plugin.emailConfirmation')
    def emailConfirmed(info) {
        def emp = Employee.get(info.id as Long)
        if(emp.email == info.email){
            emp.checkEmail = true
            emp.checkEmailDate = new Date()
            emp.save flush: true
            return [controller:'employee', action:'emailChecked']
        }else{
            return [controller:'employee', action:'emailCheckError']
        }
    }

}
