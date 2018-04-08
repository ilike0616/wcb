package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
import static com.uniproud.wcb.ErrorUtil.successFalse

@Transactional(readOnly = true)
class UserPortalController {
    @AdminAuthAnnotation
    def list(){
        def userPortalClosure  =  {
            if(params.moduleId) {
                portal{
                    eq('module.id', params.moduleId?.toLong())
                }
            }
            if(params.userId){
                eq('user.id',params.userId?.toLong())
            }
        }

        // 查询用户portal有哪些
        def userPortals = [:]
        UserPortal.createCriteria().list(userPortalClosure).each {
            userPortals.put(it.portal?.id,it)
        }
        // 查询符合条件的portal有哪些,如果用户操作里面已经有的，则用用户的。否则用默认的
        def portalClosure  =  {
            if(params.moduleId) {
                eq('module.id', params.moduleId?.toLong())
            }
        }
        def portals = []
        def userPortalId
        def isEnable = false
        def index = 1
        def result = Portal.createCriteria().list(portalClosure).each {
            isEnable = false
            userPortalId = null
            def title = it.title
            def height = it.height
            def isShow = true
            if(userPortals.containsKey(it.id)){
                def userPortal = userPortals.get(it.id)
                title = userPortal.title
                height = userPortal.height
                userPortalId = userPortal.id
                isEnable = true
                isShow = userPortal.isShow
            }
            portals << [id:it.id,title:title,height:height,userPortalId:userPortalId,isEnable: isEnable,isShow:isShow,'idx':index++]
        }
        def json = [success:true,data:portals] as JSON
        log.info json
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 获取所有的UserPortal
     * @return
     */
    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def pureList(){
        if(!params.userId){
            render(successFalse)
            return
        }
        def userPortalClosure  =  {
            if(params.moduleId) {
                portal{
                    eq('module.id', params.moduleId?.toLong())
                }
            }
            if(params.userId){
                eq('user.id',params.userId?.toLong())
            }
            order("idx","asc")
        }

        def maxOrderIndex = UserPortal.executeQuery("select min(idx) from UserPortal where user.id = :userId",[userId: params.userId?.toLong()]).first()
        def idx = 1
        def userPortals = []
        UserPortal.createCriteria().list(userPortalClosure).each {
            if(maxOrderIndex && maxOrderIndex != 1){
                it.idx = idx++
                it.save flush: true
            }
            userPortals << [id:it.id,title:it.title,height:it.height,'idx':it.idx,user:it.user?.id,
                            portal:it.portal?.id,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated]
        }
        def json = [success:true,data:userPortals] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 排序
     * @return
     */
    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def sortIdx() {
        log.info params
        def data = JSON.parse(params.data)
        def userPortal
        data.each {
            userPortal = UserPortal.get(it.id?.toLong())
            userPortal.properties = it
            if(!userPortal.validate()) {
                render([success:false,errors: errorsToResponse(userPortal.errors)] as JSON)
                return
            }
        }
        userPortal.save(flush:true)
        render([success:true] as JSON )
    }

    /**
     * 员工Portal排序
     * @return
     */
    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def sortEmployeePortalIdx() {
        log.info params
        def data = JSON.parse(params.data)
        def employeePortal
        data.each {
            if(it.id?.toLong() == 0){
                employeePortal = new EmployeePortal()
                employeePortal.userPortal = UserPortal.get(it.userPortalId?.toLong())
                employeePortal.user = session.employee?.user
                employeePortal.employee = session.employee
            }else{
                employeePortal = EmployeePortal.get(it.id?.toLong())
            }
            employeePortal.idx = it.idx
            employeePortal.save(flush:true)
        }
        render([success:true] as JSON )
    }

    /**
     * admin层，模块分配->用户Portal->保存
     * @return
     */
    @AdminAuthAnnotation
    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def otherParams = JSON.parse(params.otherParams)
        def portalIds = []
        UserPortal.where {
            user == User.get(otherParams.userId?.toLong())
            portal.module.id == otherParams.moduleId?.toLong()
        }.each {
            portalIds.push(it.portal?.id)
        }

        def userPortal
        def portal
        data.each{
            if(!it.isEnable){
                if(!portalIds.contains(it.id?.toLong())) {
                    return    // 没有选中，并且没有包含在用户操作里面
                }else{
                    if(it.userPortalId){
                        def uPortal = UserPortal.get(it.userPortalId?.toLong())
                        EmployeePortal.findByUserPortal(uPortal).delete()
                        PrivilegeUserPortal.findAllByUserPortal(uPortal)*.delete()
                        uPortal.delete()
                    }
                    return
                }
            }

            if(it.userPortalId){
                userPortal = UserPortal.get(it.userPortalId?.toLong())
            }else{
                userPortal = new UserPortal()
                // 获取最大排序值
                def maxOrderIndex = UserPortal.executeQuery("select max(idx) from UserPortal where user.id = :userId",[userId: otherParams.userId?.toLong()]).first()
                if(!maxOrderIndex) maxOrderIndex = 0
                userPortal.properties['idx'] = maxOrderIndex + 1
            }
            portal = Portal.get(it.id?.toLong())
            userPortal.properties['portal'] = portal
            userPortal.properties['title'] = it.title
            userPortal.properties['height'] = it.height
            userPortal.properties['isShow'] = it.isShow
            userPortal.properties['user'] = otherParams.userId?.toLong()
            userPortal.save flush: true
        }
        render(successTrue)
    }

    /**
     * 员工工作台设置，工作台查询可用Portal
     * @return
     */
    @UserAuthAnnotation
    def searchAvailableUserPortal(){
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def employee = Employee.get(session.employee.id)
        def privilegeIds = employee.privileges.id
        def privilegeUserPortalIds = []
        PrivilegeUserPortal.createCriteria().list(){
            if(privilegeIds){
                inList("privilege.id",privilegeIds)
            }
        }.each {
            privilegeUserPortalIds << it.userPortal.id
        }
        def userPortals = []
        UserPortal.createCriteria().list(){
            eq('user',useUser)
            order('idx', 'asc')
        }.each {userPortal->
            if(privilegeUserPortalIds.contains(userPortal.id)){
                userPortals << [id:userPortal.id,title:userPortal.title,height:userPortal.height,'idx':userPortal.idx,
                                user:userPortal.user?.id,portal:userPortal.portal?.id,dateCreated:userPortal.dateCreated,
                                lastUpdated:userPortal.lastUpdated]
            }
        }
        def json = [success:true,data:userPortals] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 查询可用Portal（手机端）
     * @return
     */
    @UserAuthAnnotation
    def searchAvailableUserPortalForPhone(){
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def employee = Employee.get(session.employee.id)
        def privilegeIds = employee.privileges.id
        def privilegeUserPortalIds = []
        PrivilegeUserPortal.createCriteria().list(){
            inList("privilege.id",privilegeIds)
        }.each {
            privilegeUserPortalIds << it.userPortal.id
        }
        def userPortals = [:]
        UserPortal.createCriteria().list(){
            eq('user',useUser)
            order('idx', 'asc')
        }.each {userPortal->
            if(privilegeUserPortalIds.contains(userPortal.id) && userPortal.portal.type == 2){
                userPortals << ["${userPortal.portal.xtype}":[title: userPortal.title,moduleId:userPortal.portal.module.moduleId]]
            }
        }
        def json = [success:true,data:userPortals] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 员工工作台设置界面里面选中的UserPortal
     */
    @UserAuthAnnotation
    def getSelectedUserPortal(){
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def privilegeUserPortalIds = []
        PrivilegeUserPortal.createCriteria().list(){
            eq("user",session.employee?.user)
        }.each {
            privilegeUserPortalIds << it.userPortal.id
        }
        def selectedValue = []
        def employeeClosure = {
            eq('employee',session.employee)
            eq('user',session.employee?.user)
            order('idx', 'asc')
        }
        EmployeePortal.createCriteria().list(employeeClosure).each {
            if(privilegeUserPortalIds.contains(it.userPortal.id)){
                selectedValue.add(it.userPortal?.id)
            }
        }
        // 如果用户没有自己的Portals，则默认选中用户的
        if(selectedValue.size() <= 0){
            def userClosure = {
                eq('user',useUser)
                order('idx', 'asc')
            }
            UserPortal.createCriteria().list(userClosure).each {
                if(privilegeUserPortalIds.contains(it.id)){
                    selectedValue.add(it?.id)
                }
            }
        }

        def json = [selectedValue:selectedValue] as JSON
        render json
    }

    /**
     *  员工启用工作台设置，启用Portal
     * @return
     */
    @UserAuthAnnotation
    @Transactional
    def enableUserPortals(){
        def emp = session.employee;
        def user = session.employee?.user;
        // 删除掉所有的，从新插入
        EmployeePortal.where {
            user == user
            employee == emp
        }.deleteAll()
        if(params.employeePortals){
            def index = 1
            if(params.employeePortals instanceof String) {
                def userPortalId = params.employeePortals.toLong()
                new EmployeePortal(user: user,employee: emp,idx: index++,userPortal: UserPortal.get(userPortalId)).save()
            }else{
                params.employeePortals.each{
                    new EmployeePortal(user: user,employee: emp,idx: index++,userPortal: UserPortal.get(it.toLong())).save()
                }
            }
        }

        render(successTrue)
    }

    /**
     * 员工工作台显示的Portal
     */
    @UserAuthAnnotation
    def workBenchShowPortal(){
        def emp = Employee.get(session.employee?.id)
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def showPortals = []
        def employeePortalList = EmployeePortal.createCriteria().list {
            eq('user',useUser)
            eq('employee',emp)
            order("idx", "asc")
        }
        def empPrivilege = emp.privileges as List<Long>
        def privilegeUserPortalIds = []
        PrivilegeUserPortal.createCriteria().list(){
            eq("user",user)
            inList("privilege",empPrivilege)
        }.each {
            privilegeUserPortalIds << it.userPortal.id
        }

        if(privilegeUserPortalIds){
            if(employeePortalList && employeePortalList.size() > 0){
                employeePortalList.each {
                    if(privilegeUserPortalIds.contains(it.userPortal?.id)){
                        showPortals << [dataId:it.id,userPortalId: it.userPortal?.id,xtype:it.userPortal?.portal?.xtype,title: it.userPortal?.title,height:it.userPortal?.height]
                    }
                }
            }else{
                UserPortal.createCriteria().list {
                    eq('user',useUser)
                    eq('isShow',true)
                    order("idx", "asc")
                }.each {
                    if(privilegeUserPortalIds.contains(it.id)){
                        showPortals << [dataId:0,userPortalId: it.id,xtype:it.portal?.xtype,title: it.title,height:it.height]
                    }
                }
            }
        }
        def json = [portals:showPortals] as JSON
        render json
    }

    /**
     * 员工工作台，关闭Portal
     */
    @UserAuthAnnotation
    @Transactional
    def closeEmployeePortal(){
        if(!params.id && !params.userPortalId){
            render(successFalse)
            return
        }
        if(params.id?.toLong() == 0){
            render(success:false,msg:'请您设置属于自己的工作台！')
            return
        }
        EmployeePortal.get(params.id?.toLong()).delete()
        render(successTrue)
    }

    /**
     * 查询员工某个父模块下面有哪些portal
     * @return
     */
    @UserAuthAnnotation
    def moduleChartList(){
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def employee = Employee.get(session.employee.id)
        def privilegeIds = employee.privileges.id
        def portals = []
        def privilegeUserPortalIds = []
        PrivilegeUserPortal.createCriteria().list(){
            eq("user",user)
        }.each {
            privilegeUserPortalIds << it.userPortal.id
        }
        def userPortals = []
        UserPortal.createCriteria().list(){
            eq('user',useUser)
            order('idx', 'asc')
        }.each {userPortal->
            def portal = userPortal.portal
            if(privilegeUserPortalIds.contains(userPortal.id) && params.parentModuleId && params.parentModuleId == portal?.module?.parentModule?.moduleId){
                userPortals << [title: userPortal.title,moduleId:portal?.module?.moduleId,xtype: portal.xtype,
                                viewId:portal.viewId,viewStore:portal.viewStore]
            }
        }
        def json = [success:true,data:userPortals] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }
}
