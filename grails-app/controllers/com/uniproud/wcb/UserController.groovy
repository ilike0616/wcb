package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import wcb.CopyVersionInfoJob

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@AgentAuthAnnotation
@Transactional(readOnly = true)
class UserController {
    def userService
    def smsRecordService
    def viewService
    def moduleService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = User.where{
            if(params.moduleId) {
                modules {
                    id == params.moduleId
                }
            }
            if(params.fromWhere && params.fromWhere == 'agent'){ // 代理商查询
                agent == session.agent
            }
            if(params.isTemplate){ // 查询模板企业
                isTemplate == params.isTemplate?.toBoolean()
            }
            if(params.useSysTpl){ //
                useSysTpl == params.useSysTpl?.toBoolean()
            }
            if(params.edition){ // 根据edition查询下面的用户（如：版本管理）
                edition{
                    id == params.edition.toLong()
                }
            }
            if(params.searchValue){ // 快速搜索
                or {
                    ilike("userId","%$params.searchValue%")
                    ilike("name","%$params.searchValue%")
                }
            }
            deleteFlag == false
        }
        def users = []
        query.list(params).each {
            users << [id:it.id,userId: it.userId, name:it.name,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,useSysTpl:it.useSysTpl,
                      balance:it.balance,sumAddBalance:it.sumAddBalance,sumAddRealBalance:it.sumAddRealBalance,modules:it.modules.id,
                      moduleNames:it.modules.moduleName,edition:it.edition?.id,editionName:it.edition?.name,isTest:it.isTest,testDueDate:it.testDueDate,allowedNum:it.allowedNum,
                      monthlyFee:it.monthlyFee,dueDate:it.dueDate,enabled:it.enabled,agent:it.agent?.id,agentName:it.agent?.name,
                        versionCopyState:it.versionCopyState]
        }
        def json = [success:true,data:users, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(User userInstance) {
        log.info params
        if (userInstance == null) {
            render(successFalse)
            return
        }
        if (!userInstance.validate()) {
            log.info(userInstance.errors)
            render([success:false,errors: errorsToResponse(userInstance.errors)] as JSON)
            return
        }
        if(params.fromWhere && params.fromWhere == 'agent'){ // 代理商新增
            userInstance.agent = session.agent
        }
        if(params.edition){
            userInstance.edition = Edition.get(params.edition as Integer)
        }
        userInstance.save flush: true

        // 当版本不为空，并且版本的企业模板也不为空，则需要拷贝版本信息
        if(userInstance.edition && userInstance.edition?.templateUser){
            CopyVersionInfoJob.triggerNow([id:userInstance.id])
        }
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        User.executeUpdate("update User set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            render(successFalse)
            return
        }
        if (!userInstance.validate()) {
            render([success:false,errors: errorsToResponse(userInstance.errors)] as JSON)
            return
        }
        if(params.edition){
            userInstance.edition = Edition.get(params.edition as Integer)
        }
        // 切换版本
        if (params.switchVersion && params.switchVersion == "switchVersion") {
            if(userInstance.edition){
                userInstance.properties['versionCopyState'] = 1
                CopyVersionInfoJob.triggerNow([id:userInstance.id])
            }
        }
        // 重新计算扣费
        def edition = Edition.get(userInstance.edition?.id)
        userInstance.properties['monthlyFee'] = edition.monthlyFee * userInstance.allowedNum
        userInstance.save flush: true
        render(successTrue)
    }
    /**
     * 启用个性化自定义配置
     * @return
     */
    @AdminAuthAnnotation
    @Transactional
    def enableCustom(){
        if(!params.id){
            render(successFalse)
            return
        }
        def user = User.get(params.id as Long)
        user.useSysTpl = false
        user.versionCopyState = 1
        user.save flush: true
        CopyVersionInfoJob.triggerNow([id:user.id])
        render(successTrue)
    }
    // 模块分配->保存
    @Transactional
    def moduleAssignForUser(){
        if(!params.id){
            render(successFalse)
            return
        }
        def user = User.get(params.id?.toLong())
        if(user == null){
            render(successFalse)
            return
        }
        if(params.moduleIds){
            def newModules = JSON.parse(params.moduleIds) as List
            def userModules = user.modules?.id
            // 删除该用户的用户操作和用户操作绑定的一些关系
            userService.deleteUserOperationSomeRelation(user,userModules,newModules)
            def maxIdx = UserMenu.executeQuery("select max(idx) from UserMenu where user.id=:userId",[userId:user.id])
            if(maxIdx) maxIdx = 1
            newModules.each {   // 本次更新用户不包含的模块，要生成用户菜单
                if(!userModules.contains(it.toLong())){
                    def module = Module.get(it.toLong())
                    if(module.isMenu != false){
                        def parentUserMenu = UserMenu.findByUserAndModule(user,module.parentModule)
                        new UserMenu(user: user,module: module,text: module.moduleName,idx: ++maxIdx,iconCls: module.iconCls,parentUserMenu: parentUserMenu).save()
                    }
                    def opts = Operation.where {
                        module == module
                    }.list()
                    opts.each {opt->
                        // 2015年12月2日16:06:36 乔旭修改 ，，，原来的代码可能存在 it 不明确的问题
                        // 没找到再添加,防止重复插入
                        if(!UserOperation.findByUserAndModuleAndOperation(user,module,opt)){
                            new UserOperation(user: user,module: module,operation: opt,text:opt.text,iconCls: opt.iconCls).save()
                        }
                    }
                    if(UserField.countByUserAndModule(user,module)==0) {
                        log.info "模块分配时，初始化 ${module.moduleName} de 企业字段"
                        userService.initUserModuleNew(user, module)
                    }
                    if(View.countByUserAndModule(user,module)==0) {
                        log.info "模块分配时，初始化 ${module.moduleName} de 视图"
                        viewService.initUserViewNew(user, module)
                    }
                }
            }
            user.properties['modules'] = newModules
        }
        user.save flush: true
        event('initUmv',user)
        render(successTrue)
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)

        def userInstance
        data.each {
            userInstance = User.get(it.id)
            userInstance.properties = it
            log.info userInstance.validate()
            if(!userInstance.validate()) {
                render([success:false,errors: errorsToResponse(userInstance.errors)] as JSON)
                return
            }
        }
        userInstance.save(flush:true)
        render(successTrue)
    }

    def field(){
        userService.fieldInit()
    }

    @Transactional
    def register(User user){
        if (user == null) {
            render(successFalse)
            return
        }
        //校验验证码
        if(!smsRecordService.checkVerify(user.mobile,params.verify)){
            render (verifyErr)
            return
        }
        if(params.edition){
            user.edition = Edition.get(params.edition as Integer)   //版本
        }
        user.monthlyFee = user.edition?.monthlyFee * user.allowedNum    //月使用费
        if(user.isTest){
            def days = 7; //测试账号默认测试天数
            user.testDueDate = DateUtil.add(new Date(),Calendar.DAY_OF_YEAR,days)   //测试截止日期
        }else{
            user.dueDate = new  Date()      //今晚就会扣费
        }
        if (!user.validate()) {
            log.info(user.errors)
            render([success:false,errors: errorsToResponse(user.errors)] as JSON)
            return
        }
        user.save flush: true
        //生成默认管理员账号
        def admin = new Employee(name:'admin',password:'123456',user:user,email:user.email,phone:user.phone,mobile: user.mobile,enabled: true)
        admin.save flush: true
        render(successTrue)
    }

    @AdminAuthAnnotation
    def setDefaultModules(){
        if(!params.id){
            render(successFalse)
            return
        }
        def user = User.get(params.id as Long)
        moduleService.createModulesFiles(user)
        render(successTrue)
    }
}
