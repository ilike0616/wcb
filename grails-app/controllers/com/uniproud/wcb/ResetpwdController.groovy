package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import grails.converters.JSON

@NormalAuthAnnotation
class ResetpwdController {

    def baseService
    def simpleCaptchaService
    def smsRecordService

    def index() {}

    /**
     * 输入并验证通过手机号后，进入重置密码页面
     * @return
     */
    def resetpwd() {
        if (!params.mobile && !session.resetpwd_mobile) {
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
                session.resetpwd_mobile = params.mobile
        }
    }

    /**
     * 修改新密码
     * @return
     */
    def reseted() {
        if (!session.resetpwd_mobile) {
            render(view: "index")
            return
        }
        if (!params.password) {
            render(view: "resetpwd", model: [params: params, errors: true])
            return
        }
        //修改密码
        def employee = Employee.findByMobileAndDeleteFlag(session.resetpwd_mobile, false)
        employee.password = params.password
        baseService.save(params, employee, 'employee')
        render(view: "reseted", model: [employee: employee])
    }

    /**
     * 验证手机号是否存在
     * @return
     */
    def mobileIsExist() {
        def employee = Employee.findByMobileAndDeleteFlag(params.param, false)
        if (employee) {
            render([status: 'y'] as JSON)
        } else {
            render "手机号不存在，请输入正确的手机号！"
        }
    }

}
