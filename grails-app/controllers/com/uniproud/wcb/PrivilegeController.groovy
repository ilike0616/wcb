package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@UserAuthAnnotation
@Transactional(readOnly = true)
class PrivilegeController {

    def modelService
	def baseService
    def privilegeService

    @UserAuthAnnotation
    def list(){
        def emp = session.employee
        def extraCondition = {
            if(params.moduleId){
                module{
                    eq('id', params.moduleId?.toLong())
                }
            }
        }
        render modelService.getDataJSON('privilege',extraCondition,false)
    }

    @AdminAuthAnnotation
    def listForAdmin(){
        println(params)
        RequestUtil.pageParamsConvert(params)
        def privileges = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Privilege');
        def query = Privilege.where{
            user.id == params.userId?.toLong()
            deleteFlag == false
            if(params.employeeId){ // 员工的权限视图
                employees.id == params.employeeId?.toLong()
            }
        }
        query.list(params).each {
            def privilege = [:]
            // 处理普通字段
            fields.each {field->
                privilege << [(field.fieldName):it."${field.fieldName}"]
            }
            privilege << ["user":it.user?.id,"userName":it.user?.name]
            privileges << privilege
        }
        def json = [success:true,data:privileges, total: query.count()] as JSON
        println(json)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Privilege privilegeInstance) {
        if (privilegeInstance == null) {
			render baseService.error(params)
            return
        }
        if(session.employee?.user){
            privilegeInstance.properties["user"] = session.employee?.user
        }
		baseService.save(params,privilegeInstance)
        if(params.templatePrivilege){
            def templatePrivilege = Privilege.get(params.templatePrivilege.toLong())
            privilegeService.copyPrivilege(privilegeInstance,templatePrivilege)

        }
        render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Privilege.executeUpdate("delete PrivilegeUserPortal where privilege.id in (:ids)",[ids:ids*.toLong()])
        Privilege.executeUpdate("delete Privilege where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    @Transactional
    def update(Privilege privilege) {
		render baseService.save(params,privilege)
    }

    @UserAuthAnnotation
    @Transactional
    def save() {
        println params
        def data = JSON.parse(params.data)
        def privilegeInstance
        data.each {
            privilegeInstance = Module.get(it.id)
            privilegeInstance.properties = it
            println privilegeInstance.validate()
            if(!privilegeInstance.validate()) {
                render([success:false,errors: errorsToResponse(privilegeInstance.errors)] as JSON)
                return
            }
        }
        privilegeInstance.save(flush:true)
        render(successTrue )
    }

    @UserAuthAnnotation
    @Transactional
    def searchUserOperation(){
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def userId = useUser?.id
        def privilegeUserOperation = []
        if(params.privilegeId){
            def privilege = Privilege.get(params.privilegeId.toLong())
            privilegeUserOperation = privilege.userOperation?.id
        }

        // 查询每个模块对应哪些用户操作
        def searchClosure = {
            eq('user',useUser)
            order("id", "asc")
        }
        def uo = []
        def userOperations = [:]
        UserOperation.createCriteria().list(searchClosure).each {
            def moduleId = it.module?.id
            def checked = false
            if(privilegeUserOperation.contains(it.id)){
                checked = true
            }
            if(userOperations.containsKey(moduleId)){
                uo = userOperations.get(moduleId)
            }else{
                uo = []
            }
            uo << [boxLabel: it.text, name: 'field',inputValue: it.id,checked:checked]
            userOperations.put(moduleId,uo)
        }

        // 查询用户拥有哪些模块
        def datas = []
        def moduleClosure = {
            isNotNull('parentModule')
            if(userId){
                users{
                    eq("id",userId)
                }
            }
            order("id", "asc")
        }
        Module.createCriteria().list(moduleClosure).each {
            def userOperation = []
            if(userOperations.containsKey(it.id)){
                userOperation = userOperations.get(it.id)
            }
            datas << [title:"【"+it.moduleName+"】",
                      items:[margin:'0 0 0 20',xtype: 'checkboxgroup', name : 'userOperation'+it.id,columns: 6,items:userOperation]
            ]
        }

        def json = [data:datas] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @UserAuthAnnotation
    @Transactional
    def bindUserOperation(){
        if(!params.privilegeId){
            render(successFalse)
        }

        def privilege = Privilege.get(params.privilegeId?.toLong())
        if(!privilege){
            render(successFalse)
        }

        def paramsBindIds = []
        if(params.data){
            paramsBindIds = params.data.split(",")
        }
        privilege.properties["userOperation"] = paramsBindIds*.toLong()
        if (privilege.hasErrors()) {
            println privilege.errors
        }
        privilege.save(flush: true)
        event('initUmv',[user:privilege.user])
        render(successTrue)
    }

    /**
     * 查询用户Portals
     * @return
     */
    @UserAuthAnnotation
    @Transactional
    def searchUserPortal(){
        def userId = session.employee?.user?.id
        def privilegeUserPortal = []
        if(params.privilegeId){
            def privilege = Privilege.get(params.privilegeId.toLong())
            PrivilegeUserPortal.createCriteria().list(){
                eq('privilege',privilege)
            }.each {
                privilegeUserPortal << it.userPortal.id
            }
        }
        // 查询每个模块对应哪些用户Portal
        def searchClosure = {
            user{
                eq('id',userId)
            }
            order("id", "asc")
        }
        def userPortal = []
        def userPortals = [:]
        UserPortal.createCriteria().list(searchClosure).each {
            def moduleId = it.portal?.module?.id
            def checked = false
            if(privilegeUserPortal.contains(it.id)){
                checked = true
            }
            if(userPortals.containsKey(moduleId)){
                userPortal = userPortals.get(moduleId)
            }else{
                userPortal = []
            }
            userPortal << [boxLabel: it.title, name: 'field',inputValue: it.id,checked:checked]
            userPortals.put(moduleId,userPortal)
        }

        // 查询用户拥有哪些模块
        def datas = []
        def moduleClosure = {
            isNotNull('parentModule')
            users{
                eq("id",userId)
            }
            order("id", "asc")
        }
        Module.createCriteria().list(moduleClosure).each {
            if(userPortals.containsKey(it.id)){
                def uPortal = userPortals.get(it.id)
                datas << [title:"【"+it.moduleName+"】",
                          items:[margin:'0 0 0 20',xtype: 'checkboxgroup', name : 'userPortal'+it.id,columns: 3,items:uPortal]
                ]
            }
        }

        def json = [data:datas] as JSON
        println json
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 绑定userPortal
     * @return
     */
    @UserAuthAnnotation
    @Transactional
    def bindUserPortal(){
        if(!params.privilegeId){
            render(successFalse)
        }
        def privilege = Privilege.get(params.privilegeId?.toLong())
        if(!privilege){
            render(successFalse)
        }
        def paramsBindIds = []
        if(params.data){
            paramsBindIds = params.data.split(",")
        }
        // 先删除掉以前有的，本次操作取消的
        PrivilegeUserPortal.createCriteria().list(){
            eq("privilege",privilege)
        }.each {
            if(!paramsBindIds.contains(it.userPortal.id.toString())){
                it.delete()
            }
        }
        // 保存本次操作选中的
        paramsBindIds.each {id->
            def userPortal = UserPortal.get(id.toLong())
            new PrivilegeUserPortal(privilege: privilege,userPortal: userPortal,user: userPortal.user).save()
        }
        render(successTrue)
    }

    @UserAuthAnnotation
    @Transactional
    def bindPrivilegeViewDetailList() {
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def userId = useUser?.id
        def bindPrivilegeUserField = [:]
        def privilegeViewDetailClosure = {
            if(params.privilegeId){
                privilege{
                    eq("id",params.privilegeId.toLong())
                }
            }

            if(params.viewId){
                viewDetail{
                    view{
                        eq("id",params.viewId.toLong())
                    }
                }
            }
        }

        PrivilegeViewDetail.createCriteria().list(privilegeViewDetailClosure).each {
            bindPrivilegeUserField.put(it.viewDetail?.id,it)
        }

        def viewDetailClosure = {
            ne('pageType','hidden')
            if(params.viewId){
                view{
                    eq("id",params.viewId.toLong())
                }
            }
            if(useUser){
                eq("user",useUser)
            }
        }
        def data = []
        def unShow = false
        def unEdit = false
        ViewDetail.createCriteria().list(viewDetailClosure).each{
            unShow = false
            unEdit = false
            if(bindPrivilegeUserField.containsKey(it.id)){
                def privilegeViewDetail = bindPrivilegeUserField.get(it.id)
                unShow = privilegeViewDetail.unShow
                unEdit = privilegeViewDetail.unEdit
            }
            data << [
                    id:it.id,text:it.userField?.text,unShow:unShow,unEdit:unEdit
            ]
        }

        event('initUmv',session.employee?.user)
        def json = [data:data] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @UserAuthAnnotation
    @Transactional
    def bindViewDetail() {
        def data = JSON.parse(params.data)
        def otherParams = JSON.parse(params.otherParams)
        def privilege = Privilege.get(otherParams.privilegeId?.toLong())
        // 获取屏蔽显示和编辑的记录
        def pvdIds = [:]
        PrivilegeViewDetail.findAllByPrivilege(privilege).each {
            pvdIds.put(it.viewDetail?.id,it)
        }
        def pvd
        def viewDetail
        data.each{
            if(!it.unShow && !it.unEdit && pvdIds.containsKey(it.id?.toLong())){   // 如果【没有屏蔽】显示和编辑，并且以前包含，则应该删除
                pvdIds.get(it.id?.toLong()).delete()
                return
            }
            if(pvdIds.containsKey(it.id.toLong())){
                pvd = pvdIds.get(it.id.toLong())
            }else{
                pvd = new PrivilegeViewDetail()
            }
            viewDetail = ViewDetail.get(it.id?.toLong())
            pvd.properties['privilege'] = privilege
            pvd.properties['viewDetail'] = viewDetail
            pvd.properties['unShow'] = it.unShow
            pvd.properties['unEdit'] = it.unEdit
            pvd.save flush: true
        }
        event('initUmv',session.employee?.user)
        render(successTrue)
    }

}
