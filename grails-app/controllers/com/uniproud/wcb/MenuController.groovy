package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
class MenuController {
    def privilegeService

    def index() {
        def model = new Model(modelClass:'com.uniproud.wcb.Customer',modelName:'Customer',remark:'客户管理数据模型')
        def fields = DomainFieldUtil.getField("com.uniproud.wcb.Customer")
        fields.each{
            model.addToFields(new Field(it))
        }
        log.info model.errors
        model.save()
        render model
    }

    @AdminAuthAnnotation
    def tree(){
        def tree = []
        def node1children = []
        node1children << [text:"模块管理",ctrl: 'ModuleController',view:'moduleMain',leaf:true]
        node1children << [text:"模块分配",ctrl:'ModuleAssignmentController',view:'moduleAssignmentMain',leaf:true]
        node1children << [text:"菜单管理",ctrl:'UserMenuController',view:'userMenuMain',leaf:true]
        node1children << [text:"字段管理",ctrl:'ModelController',view:'modelMain',leaf:true]
        def node1 = [text:"资源管理",leaf:false,expanded:true,children:node1children]

        def node2children = []
        node2children << [text:"企业账号",ctrl: 'UserController',view:'userMain',leaf:true]
        node2children << [text:"员工管理",ctrl: 'EmployeeController',view:'employeeMain',leaf:true]
        node2children << [text:"部门管理",ctrl: 'DeptController',view:'deptMain',leaf:true]
        node2children << [text:"权限管理",ctrl: 'PrivilegeController',view:'privilegeMain',leaf:true]

        node2children << [text:"充值记录",ctrl: 'AddBalanceController',view:'addBalanceMain',leaf:true]
        node2children << [text:"扣费记录",ctrl: 'PaymentRecordController',view:'paymentRecordList',leaf:true]
        node2children << [text:"用户反馈",ctrl: 'UserFeedbackController',view:'userFeedbackList',leaf:true]
        node2children << [text:"一级代理商",ctrl: 'AgentController',view:'agentList',leaf:true]
        node2children << [text:"下级代理商",ctrl: 'SubAgentController',view:'subAgentList',leaf:true]

        def node2 = [text:"用户管理",leaf:false,expanded:true,children:node2children]

        def node3children = []
        node3children << [text:"数据字典",ctrl: 'DataDictController',view:'dictMain',leaf:true]
        node3children << [text:"数据模型",ctrl: 'ModelController',view:'modelMain',leaf:true]
        // node3children << [text:"模块管理",ctrl: 'ModelController',view:'modelList',leaf:true]
//        node3children << [text:"字段管理",ctrl: 'FieldController',view:'fieldMain',leaf:true]
        node3children << [text:"企业字段",ctrl: 'UserFieldController',view:'userFieldMain',leaf:true]
//        node3children << [text:"视图管理",ctrl: 'ViewController',view:'userFieldMain',leaf:true]
        node3children << [text:"视图管理",ctrl: 'ViewController',view:'viewMain',leaf:true]
        def node3 = [text:"自定义管理",leaf:false,expanded:true,children:node3children]

        def node4children = []
        node4children << [text:"APP升级管理",ctrl: 'AppVersionController',view:'appVersionList',leaf:true]
        node4children << [text:"版本管理",ctrl: 'EditionController',view:'editionMain',leaf:true]
        node4children << [text:"管理员管理",ctrl: 'AdministratorController',view:'administratorList',leaf:true]
        def node4 = [text:"系统管理",leaf:false,expanded:true,children:node4children]

        tree << node1
        tree << node2
        tree << node3
        tree << node4

        if(params.callback) {
            render "${params.callback}(${tree as JSON})"
        }else{
            render(tree as JSON)
        }
    }
    @AgentAuthAnnotation
    def agentTree(){
        def tree = []
        def node1children = []
        node1children << [text:"企业账号",ctrl: 'UserController',view:'userList',leaf:true]
        if(session.agent?.isAllowLowerAgent == true){
            node1children << [text:"下级代理商",ctrl: 'SubAgentController',view:'subAgentList',leaf:true]
        }
        node1children << [text:"充值记录",ctrl: 'AddBalanceController',view:'addBalanceMain',leaf:true]
        node1children << [text:"用户登录日志",ctrl: 'LoginLogController',view:'loginLogList',leaf:true]
        def node1 = [text:"企业管理",leaf:false,expanded:true,children:node1children]

        tree << node1

        if(params.callback) {
            render "${params.callback}(${tree as JSON})"
        }else{
            render(tree as JSON)
        }
    }

    def userTree(){
        def user = User.get(session.employee?.user?.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def employee = Employee.get(session.employee?.id)
        if(!useUser){
            render([] as JSON)
            return
        }
        def query = UserMenu.where{
            parentUserMenu == null
            user == useUser
        }
        def userOperations = privilegeService.getEmployeePrivilegeOperations(employee,'view')
        def json = spendChild(query.list(sort:'idx',order:'ASC'),userOperations) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def spendChild(userMenus,userOperations){
        def child = []
        def obj = [:]
        userMenus.each {
            obj = [:]
			def iconCls = it.iconCls
			if(!iconCls) iconCls=it.module?.moduleId
            obj << [id:it.id,text:it.text,iconCls:iconCls,idx:it.idx,expanded:true,parentUserMenu:it.parentUserMenu?.id]
            obj << [parentUserMenuName:it.parentUserMenu?.text,ctrl:it.module?.ctrl,view:it.module?.vw,del:false
                    ,moduleId:it.module?.moduleId]
            if(it.getChildren()){
                def son = spendChild(it.getChildren(),userOperations)
                if(son) {
                    obj << ["children": son]
                }else{
                    obj << ["del":true]
                }
                obj << ["leaf":false]
            }else{
                obj << ["leaf":true]
            }
            if(userOperations && userOperations.contains(it.module?.moduleId+"_view") && it.parentUserMenu){
                child << obj
            }
            if(!it.parentUserMenu && obj.get('del')==false){
                child << obj
            }

        }
        child
    }

    /**
     * 手机端业务菜单
     * @return
     */
    def phone() {
        def user = User.get(session.employee?.user?.id)
        def employee = Employee.get(session.employee?.id)
        def uos = employee.privileges.userOperation.flatten()
        def menu = []

//        if (uos.contains(privilegeService.getUserOperationId(user,'public_customer_view'))) {
//            menu << [id: 1, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('public_customer')).text, moduleId: 'public_customer', view: 'publicCustomerView', group: '1']
//        }
        if (uos.contains(privilegeService.getUserOperationId(user,'customer_view'))) {
            menu << [id: 2, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('customer')).text, moduleId: 'customer', view: 'customerList', group: '1']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'contact_view'))) {
            menu << [id: 3, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('contact')).text, moduleId: 'contact', view: 'contactList', group: '1']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'schedule_task'))) {//拜访计划
            menu << [id: 4, title:  privilegeService.getUserOperationId(user,'schedule_task')?.text, moduleId: 'schedule', view: 'myScheduleList', group: '1']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'customer_follow_view'))) {//客户跟进
            menu << [id: 5, title:  UserMenu.findByUserAndModule(user,Module.findByModuleId('customer_follow')).text, moduleId: 'customer_follow', view: 'customerFollowList', group: '1']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'site_photo_view'))) {//现场拍照
            menu << [id: 6, title:  UserMenu.findByUserAndModule(user,Module.findByModuleId('site_photo')).text, moduleId: 'site_photo', view: 'sitePhotoList', group: '1']
        }

        if (uos.contains(privilegeService.getUserOperationId(user,'sale_chance_view'))) {
            menu << [id: 7, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('sale_chance')).text, moduleId: 'sale_chance', view: 'saleChanceList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'sale_chance_follow_view'))) {
            menu << [id: 8, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('sale_chance_follow')).text, moduleId: 'sale_chance_follow', view: 'saleChanceFollowList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'product_view'))) {
            menu << [id: 9, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('product')).text, moduleId: 'product', view: 'productList', group: '3']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'service_task_view'))) {
            menu << [id: 10, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('service_task')).text, moduleId: 'service_task', view: 'serviceTaskList', group: '4']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'contract_order_view'))) {
            menu << [id: 11, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('contract_order')).text, moduleId: 'contract_order', view: 'contractOrderList', group: '5']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'market_activity_view'))) {
            menu << [id: 12, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('market_activity')).text, moduleId: 'market_activity', view: 'marketActivityList', group: '6']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'competitor_view'))) {
            menu << [id: 13, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('competitor')).text, moduleId: 'competitor', view: 'competitorList', group: '7']
        }
//        if (uos.contains(privilegeService.getUserOperationId(user,'scan_card_view'))) {
            menu << [id: 14, title: '名片扫描', moduleId: 'scan_card', view: 'scanCardList', group: '8']
//        }

        if(params.callback) {
            render "${params.callback}(${menu as JSON})"
        }else{
            render(menu as JSON)
        }
    }
    /**
     * 手机端办公菜单
     * @return
     */
    def work(){
        def menu = []
        def user = User.get(session.employee?.user?.id)
        def employee = Employee.get(session.employee?.id)
        def uos = employee.privileges.userOperation.flatten()

        if(employee.attendanceModel){
            menu << [id: 1, title:UserMenu.findByUserAndModule(user,Module.findByModuleId('apply_mgmt')).text, view: 'attendance', group: '1']
        }

        if (uos.contains(privilegeService.getUserOperationId(user,'plan_summary_day_view'))||
            uos.contains(privilegeService.getUserOperationId(user,'plan_summary_week_view'))||
            uos.contains(privilegeService.getUserOperationId(user,'plan_summary_month_view'))
        ) {
            menu << [id: 2, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('plan_summary')).text, view: 'planSummaryList', group: '2']
        }

//        if (uos.contains(privilegeService.getUserOperationId(user,'plan_summary_day_view'))) {
//            menu << [id: 2, title: '日报', view: 'planSummaryListDay', group: '1']
//        }
//        if (uos.contains(privilegeService.getUserOperationId(user,'plan_summary_week_view'))) {
//            menu << [id: 2, title: '周报', view: 'planSummaryListWeek', group: '1']
//        }
//        if (uos.contains(privilegeService.getUserOperationId(user,'plan_summary_month_view'))) {
//            menu << [id: 3, title: '月报', view: 'planSummaryListMonth', group: '1']
//        }

        if (uos.contains(privilegeService.getUserOperationId(user,'goout_apply_view'))) {
            menu << [id: 3, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('goout_apply')).text, view: 'goOutApplyList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'business_trip_apply_view'))) {
            menu << [id: 4, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('business_trip_apply')).text, view: 'businessTripApplyList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'leave_apply_view'))) {
            menu << [id: 5, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('leave_apply')).text, view: 'leaveApplyList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'overtime_apply_view'))) {
            menu << [id: 6, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('overtime_apply')).text, view: 'overtimeApplyList', group: '2']
        }
        if (uos.contains(privilegeService.getUserOperationId(user,'fare_claims_view'))) {
            menu << [id: 7, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('fare_claims')).text, view: 'fareClaimsList', group: '2']
        }
//        if (uos.contains(privilegeService.getUserOperationId(user,'finance_income_view'))) {
//            menu << [id: 9, title: '入账申请', view: 'financeIncomeList', group: '2']
//        }
//        if (uos.contains(privilegeService.getUserOperationId(user,'finance_expense_view'))) {
//            menu << [id: 10, title: '出账申请', view: 'financeExpenseList', group: '2']
//        }
        if (uos.contains(privilegeService.getUserOperationId(user,'task_assigned_view'))) {
            menu << [id: 8, title:UserMenu.findByUserAndModule(user,Module.findByModuleId('task_assigned')).text, view: 'taskAssignedList', group: '3']
        }

        if (uos.contains(privilegeService.getUserOperationId(user,'share_view'))) {
            menu << [id: 9, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('share')).text, view: 'shareList', group: '4']
        }

        if (uos.contains(privilegeService.getUserOperationId(user,'schedule_view'))) {
            menu << [id: 10, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('schedule')).text, view: 'scheduleList', group: '5']
        }

        if (uos.contains(privilegeService.getUserOperationId(user,'employee_locus_view'))) {
            menu << [id: 11, title: UserMenu.findByUserAndModule(user,Module.findByModuleId('employee_locus')).text, view: 'employeeLocus', group: '6']
        }

        menu << [id:12,title:'员工通讯录',view:'employeeList',group:'6']

        if(params.callback) {
            render "${params.callback}(${menu as JSON})"
        }else{
            render(menu as JSON)
        }
    }
}
