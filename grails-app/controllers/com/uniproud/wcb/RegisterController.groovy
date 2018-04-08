package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import grails.converters.JSON
import wcb.CopyVersionInfoJob

@NormalAuthAnnotation
class RegisterController {

    def baseService
    def simpleCaptchaService
    def smsRecordService
    def userService
    def validationService

    def index() {}
    /**
     * 第一步提交，转向第二步
     * @return
     */
    def auth() {
        if ((!params.mobile && !session.register_mobile) ) {
            render(view: "index", model: [params: params, errors: true])
            return
        } else {
            if (!simpleCaptchaService.validateCaptcha(params.captcha)) {
                render(view: "index", model: [params: params, errors: true])
                return
            }
            //校验验证码
            if (!smsRecordService.checkVerify(params.mobile, params.verify)) {
                render(view: "index", model: [params: params])
                return
            }
            if (params.mobile)
                session.register_mobile = params.mobile
//            if (params.password)
//                session.register_password = params.password
        }
    }

    /**
     * 注册信息输入后新增并初始化用户
     * @return
     */
    def init() {
        //第一步信息不存在，返回第一步重新输入
        if (!session.register_mobile ) {
            render(view: "index", model: [params: params, errors: true])
            return
        }
        //第二步信息有误，返回并重新输入
        if (!params.name && !params.userId || !params.employeeName || !params.email || !params.password) {//|| !params.province
            render(view: "auth", model: [params: params, errors: true])
            return
        }
        //已创建用户，刷新页面停留在成功页面
        def hasUser = User.findByNameAndUserIdAndEmail(params.name, params.userId, params.email)
        if (hasUser) {
            render(view: "init", model: [user: hasUser, employee: Employee.findByUserAndMobile(hasUser, session.register_mobile)])
            return
        }
        //新建用户
        def user = new User(params)
        user.mobile = session.register_mobile
        //用户版本
        if (params.edition) {
            user.edition = Edition.get(params.edition as Integer)   //版本
        } else {
            log.info Edition.findByKindAndVer(1, 3)
            user.edition = Edition.findByKindAndVer(1, 3)    //销售铜高级版
        }
        //使用人数
        user.allowedNum = 5
        //月使用费
        user.monthlyFee = user.edition?.monthlyFee * user.allowedNum    //月使用费
//        if(user.isTest){
        def days = 7; //测试账号默认测试天数
        user.testDueDate = DateUtil.add(new Date(), Calendar.DAY_OF_YEAR, days)   //测试截止日期
//        }else{
//            user.dueDate = new  Date()      //今晚就会扣费
//        }
        //分配代理商  动态分配代理商的机制，暂时取消  2015年12月11日15:10:27 乔旭  Begin
        /**
        def agents = Agent.createCriteria().list {
            eq('isInner', true)
            ilike('provinces', "%${params.province}%")
            eq('isOthers', false)
            order("lastAssigned", "asc")
        }
        if (agents) {
            def agent = agents[0]
            user.agent = agent
            agent.lastAssigned = new Date()
            agent.save(flush: true)
        } else {
            agents = Agent.createCriteria().list {
                eq('isInner', true)
                eq('isOthers', true)
                order("lastAssigned", "asc")
            }
            if (agents) {
                def agent = agents[0]
                user.agent = agent
                agent.lastAssigned = new Date()
                agent.save(flush: true)
            }
        }
         动态分配代理商的机制，暂时取消  2015年12月11日15:10:27 乔旭  end
         **/
        def agent = Agent.get(76360)//暂时把所有的注册用户都分配给葛新宝
        user.agent = agent
        if (!user.validate()) {
            log.info user.errors
            render(view: "auth", model: [params: params, errors: true])
            return
        }
        def userSave = baseService.save(params, user).toString()
        if (!JSON.parse(userSave.toString()).success) {
            render(view: "auth", model: [params: params, errors: true])
            return
        }
        //当版本不为空，并且版本的企业模板也不为空，则需要拷贝版本信息
        if (user.edition && user.edition?.templateUser) {
            userService.copyVersionInfo(user.id)
        }
        //生成默认管理员账号
        def emp = new Employee(name: params.employeeName, password: params.password, user: user, email: user.email, phone: user.phone, mobile: user.mobile, checkMobile: true, enabled: true)
        def empSave = baseService.save(params, emp, 'employee')
        if (!JSON.parse(empSave.toString()).success) {
            render(view: "auth", model: [params: params, errors: true])
            return
        }
        //绑定该企业账户的所有权限
        def privilege = Privilege.findAllByUser(user)
        privilege.each {
            it.addToEmployees(emp)
            it.save(flush: true)
        }
        render(view: "init", model: [user: user, employee: emp])
    }

    /**
     * 验证企业名称是否唯一
     * @return
     */
    def uniqueUserName() {
        if (User.findByName(params.param)) {
            render "该名称已存在！"
        } else {
            render([status: 'y'] as JSON)
        }
    }

    /**
     * 验证企业账号是否唯一
     * @return
     */
    /**
     * 验证企业账号是否唯一
     * @return
     */
    def uniqueUserId() {
        def weiyi = validationService.weiyi(params, 'com.uniproud.wcb.User', 'userId', params.param, true)
        if (weiyi) {//唯一
            render([status: 'y'] as JSON)
        } else {//不唯一
            render "该账号已存在！"
        }
    }

    /**
     * 验证邮箱地址是否唯一
     * @return
     */
    def uniqueUserEmail() {
        if (User.findByEmailAndDeleteFlag(params.param, false) || Employee.findByEmailAndDeleteFlag(params.param, false)) {
            render "该邮箱账号已存在！"
        } else {
            render([status: 'y'] as JSON)
        }
    }

    /**
     * 验证手机号是否唯一
     * @return
     */
    def uniqueMobile() {
        if (User.findByMobileAndDeleteFlag(params.param, false) || Employee.findByMobileAndDeleteFlag(params.param, false)) {
            render "该手机号已存在！"
        } else {
            render([status: 'y'] as JSON)
        }
    }
}
