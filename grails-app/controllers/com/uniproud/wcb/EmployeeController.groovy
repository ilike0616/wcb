package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

class EmployeeController {
    def modelService
	def baseService
    def smsRecordService
    def emailConfirmationService
    /**
     * 用户层列表查询所有的
     * @return
     */
    @UserAuthAnnotation
    @AdminAuthAnnotation
    def list(){
        def emp = session.employee
        if(session.employee){
            params.userId = session.employee?.user?.id
        }
        def ids = []
        if(params.deptId){
            def dept = Dept.get(params.deptId?.toLong())
            ids.push(dept.id)
            ids.addAll(modelService.getChildrenIds(dept.children))
        }
        def extraCondition = {
            if(params.userId){
                user{
                    eq("id",params.userId?.toLong())
                }
            }
            if(params.deptId){
                'in'('dept.id',ids)
            }
            if(params.attendanceModel){
                eq('attendanceModel.id',params.attendanceModel?.toLong())
            }
            if(params.privilegeId){
                privileges{
                    'in'('id',params.privilegeId?.toLong())
                }
            }
        }

        def isPaging = false
        if(params.isPaging && params.isPaging?.toBoolean() == true){
            isPaging = true
        }
        render modelService.getDataJSONExcludeEmp('employee',extraCondition,isPaging)
    }

    @UserAuthAnnotation
    def listPaging(){
        params.isPaging = true
        list()
    }

    /**
     * 用户层列表查询我和下级的
     * @return
     */
    @UserAuthAnnotation
    @AdminAuthAnnotation
    def listSub(){
        def emp = session.employee
        if(session.employee){
            params.userId = session.employee?.user?.id
        }
        def ids = []
        if(params.deptId){
            def dept = Dept.get(params.deptId?.toLong())
            ids.push(dept.id)
            ids.addAll(modelService.getChildrenIds(dept.children))
        }
        def extraCondition = {
            if(params.userId){
                user{
                    eq("id",params.userId?.toLong())
                }
            }
            if(params.deptId){
                'in'('dept.id',ids)
            }
            if(params.attendanceModel){
                eq('attendanceModel.id',params.attendanceModel?.toLong())
            }
            if(params.privilegeId){
                privileges{
                    'in'('id',params.privilegeId?.toLong())
                }
            }
        }
        def isPaging = true
        if(params.isPaging && params.isPaging?.toBoolean() == false){
            isPaging = false
        }

        render modelService.getDataJSON('employee',extraCondition,isPaging,false)
    }

    /**
     * 手机端通讯录格式
     */
    @UserAuthAnnotation
    def listAddressBook(){
        def user = session.employee?.user
        log.info user
        def query = Employee.where{
            user == user
            deleteFlag == false
        }
        def employees = []
        query.list(params).each {
            employees << ["name":it.name,"qxuid":it.qxuid,"photo":it.photo?.name]
        }
        def json = employees as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 管理员层用到
     * @return
     */
    @AdminAuthAnnotation
    def listForAdmin(){
        RequestUtil.pageParamsConvert(params)
        def employees = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Employee');
        def ids = []
        if(params.deptId){
            def dept = Dept.get(params.deptId?.toLong())
            ids.push(dept.id)
            ids.addAll(modelService.getChildrenIds(dept.children))
        }
        def query = Employee.where{
            if(params.userId){
                user.id == params.userId?.toLong()
            }
            deleteFlag == false
            if(params.deptId){
                dept{
                    id in (ids)
                }
            }
            if(params.searchValue){ // 快速搜索
                or {
                    ilike("name","%$params.searchValue%")
                    ilike("account","%$params.searchValue%")
                    user{
                        ilike("name","%$params.searchValue%")
                    }
                }
            }
        }
        if(!params.sort){
            params.sort = 'name'
        }
        query.list(params).each {
            def employee = [:]
            // 处理普通字段
            fields.each {field->
                employee << [(field.fieldName):it."${field.fieldName}"]
            }
            // 自己处理关联字段信息
            employee << ["parentEmployee":it.parentEmployee?.id,"parentEmployee.name":it.parentEmployee?.name
                         ,"parentEmployeeName":it.parentEmployee?.name,"user":it.user?.id,"userName":it.user?.name
                         ,"dept":it.dept?.id,"dept.name":it.dept?.name,"deptName":it.dept?.name,"isLocus":it.isLocus,"account":it.account]
            employees << employee
        }
        def json = [success:true,data:employees, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * admin层查询
     * @return
     */
    @AdminAuthAnnotation
    def treeListForAdmin(){
        println(params)
        if(params.findCondition){   // 员工新增时用到
            params.userId = params.findCondition?.toLong()
        }

        // 过滤自己及下属子对象
        def filterIds = []
        if(params.filterSelfAndChildrenId){
            def e = Employee.get(params.filterSelfAndChildrenId?.toLong())
            filterIds.push(e.id)
            filterIds.addAll(modelService.getChildrenIds(e.children))
            params.filterIds = filterIds
        }
        def searchClosure = {
            if(params.node && params.node != 'root' && params.node != 'NaN'){
                eq('parentEmployee.id',params.node.toLong())
            }else{
                isNull('parentEmployee')
            }
            eq('user.id',params.userId?.toLong())
            eq('deleteFlag',false)
            if(filterIds && filterIds.size() > 0){
                not {'in'("id",filterIds)}
            }
        }
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Employee');
        def query = Employee.createCriteria().list(searchClosure)
        def employees = modelService.spendChild(query,fields)
        def json = employees as JSON
        println(json)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @UserAuthAnnotation
    def treeList(){
        def user = session.employee?.user
        if(params.userId){
            user = User.get(params.userId?.toLong())
        }

        // 过滤自己及下属子对象
        def filterIds = []
        if(params.filter){
            def data = JSON.parse(params.filter)
            def filterName = data[0]?.property
            if(filterName == 'filterSelfAndChildrenId'){
                def e = Employee.get(data[0].value?.toLong())
                filterIds.push(e.id)
                filterIds.addAll(modelService.getChildrenIds(e.children))
                WebUtilTools.params.filterIds = filterIds
            }
        }

        def searchClosure = {
            if(params.node && params.node != 'root'){
                eq('parentEmployee.id',params.node.toLong())
            }else{
                isNull('parentEmployee')
            }
            if(user){
                eq('user.id',user.id)
            }
            if(params.privilegeId){
                privileges{
                    'in'('id',params.privilegeId?.toLong())
                }
            }
            if(filterIds && filterIds.size() > 0){
                not {'in'("id",filterIds)}
            }
            eq('deleteFlag',false)
        }
        def query = Employee.createCriteria().list(searchClosure)
        render modelService.getTreeDataJSON(query,user,'employee')
    }

    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def insert(Employee employee) {
        println(params)
        if (employee == null) {
			render baseService.error(params)
            return
        }
        if(employee.account){
            def count = Employee.where{
                user == session.employee?.user
                account == params.account
            }.count()
            if(count){
                render([success:false,msg:"员工账号不能重复",errors:[account:'员工账号不允许重复']] as JSON)
                return
            }
        }
        if(params.email) {
            def count = Employee.where {
                user == session.employee?.user
                email == params.email
            }.count()

            if(count){
                render([success:false,msg:"邮箱地址不能重复",errors:[email:'邮箱地址不允许重复']] as JSON)
                return
            }
        }
        // user为空，普通用户添加
        if(!employee.user){
            if(session.employee){
                employee.properties["user"] = session.employee?.user
            }
        }
		
		render baseService.save(params,employee)
    }

    /**
     * 下拉列表(客户分配用到)
     * @return
     */
    @UserAuthAnnotation
    @Transactional
    def employeeForEdit(){
        def user = session.employee?.user
        def query = Employee.where {
            parentEmployee == null
            user == user
        }
        def json = spendChild(query.list(sort:'id',order:'ASC')) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def spendChild(employees){
        def child = []
        def obj = [:]
        employees.each {
            obj = [:]
            obj.put("id",it.id)
            obj.put("name",it.name)
            obj.put("expanded",true)
            if(it.getChildren()){
                def son = spendChild(it.getChildren())
                if(son) obj.put("children", son)
                obj.put("leaf",false)
            }else{
                obj.put("leaf",true)
            }
            child << obj
        }
        child
    }

    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def update() {
        //def employee = Employee.get(params.id as Long)
        if(params.account){
            def count = Employee.where{
                user.id == params.user as Long
                account == params.account
                id != params.id as Long
            }.count()
            if(count){
                render([success:false,msg:"员工账号不能重复",errors:[account:'员工账号不允许重复']] as JSON)
                return
            }
        }
        def employee = Employee.get(params.id as Long)
        employee.properties = params
        log.info employee.account
		render baseService.save(params,employee,'employee')
    }

    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Employee.executeUpdate("update Employee set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    @AdminAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def initPassword() {
        def ids = JSON.parse(params.ids) as List
        Employee.executeUpdate("update Employee set password='000000' where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    @UserAuthAnnotation
    @AdminAuthAnnotation
    @Transactional
    def getSelectedPrivilege(){
        def selectedValue = []
        if(params.employeeId){
            def privilegeClosure = {
                employees{
                    'in'('id',params.employeeId?.toLong())
                }
            }
            Privilege.createCriteria().list(privilegeClosure).each {
                selectedValue.add(it.id)
            }
        }
        def json = [selectedValue:selectedValue] as JSON
        render json
    }

    @UserAuthAnnotation
    @AdminAuthAnnotation
    @Transactional
    def bindEmpPrivilege(){
        if(!params.employeeId){
			render baseService.error(params)
            return
        }
        def employee = Employee.get(params.employeeId?.toLong());
        def bindPrivileges = employee.privileges as List
        bindPrivileges.each {
            it.removeFromEmployees(employee)
        }
        if(params.employeePrivileges){
            def privilege
            if(params.employeePrivileges instanceof String) {
                privilege = Privilege.get(params.employeePrivileges as Long)
                privilege.addToEmployees(employee)
                privilege.save(flush: true)
            }else{
                params.employeePrivileges.each {
                    privilege = Privilege.get(it.toLong())
                    privilege.addToEmployees(employee)
                    privilege.save(flush: true)
                }
            }
        }
        render(successTrue)
    }

    @UserAuthAnnotation
    def saveSkin(){
        def emp = session.employee
        if(emp)
            Employee.executeUpdate("update Employee set skin=:skin where id =:ids",[skin:params.skin,ids:emp?.id])
        render baseService.success(params)
    }

    @UserAuthAnnotation
    @AdminAuthAnnotation
    @Transactional
    def modifyPassword(){
        def emp
        if(params.employeeId){ // 管理员修改用户密码
            emp = Employee.get(params.employeeId?.toLong())
        }else{
            emp = Employee.get(session.employee.id)
        }
        if(params.oldPassword != emp.password){
            render([success:false,msg:'原密码不正确，请重新输入！'] as JSON)
            return
        }
        emp.password = params.newPassword
        emp.save()
        def json = [success:true,msg:'密码修改成功！'] as JSON
        render json
    }

    @UserAuthAnnotation
    def me(){
        def extraCondition = {
            eq("id",session.employee?.id as Long)
        }
        render modelService.getDataJSONExcludeEmp('employee',extraCondition)
    }

    @UserAuthAnnotation
    def checkMobile(){
        if (params.mobile == null || params.verify == null) {
            render(successFalse)
            return
        }
        //校验验证码
        if(!smsRecordService.checkVerify(params.mobile,params.verify)){
            render (verifyErr)
            return
        }
        def emp = session.employee
        Employee.executeUpdate("update Employee set checkMobile=:checkMobile,checkMobileDate=:checkMobileDate where id =:ids",[checkMobile:true,checkMobileDate:new Date(),ids:emp?.id])
        render baseService.success(params)
    }

    /**
     * 发送验证邮箱的邮件
     */
    @UserAuthAnnotation
    def sendCheckEmail(){
        def emp = session.employee
        def subject = grailsApplication.config.email.confirmation.subject
        def from = grailsApplication.config.email.confirmation.from
        emailConfirmationService.sendConfirmation(
                view:'/emailConfirm/template',
                model:[emp:emp],
                from:from,
                to:params.email,
                subject:subject,
                id:emp.id
        )
        render baseService.success(params)
    }

    @NormalAuthAnnotation
    def emailConfirmation(){
        def result = emailConfirmationService.checkConfirmation(params.id)
        if ( result.actionToTake ) {
            chain(controller:result.actionToTake?.controller,action:result.actionToTake?.action)
        } else {
            emailCheckError()
        }
    }

    @NormalAuthAnnotation
    def emailChecked(){
        render '验证成功！<a href="javascript:window.opener=null;window.open(\'\',\'_self\');window.close();">关闭页面</a>'
    }

    @NormalAuthAnnotation
    def emailCheckError(){
        render '验证失败，请重新验证！<a href="javascript:window.opener=null;window.open(\'\',\'_self\');window.close();">关闭页面</a>'
    }
}
