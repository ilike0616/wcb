package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getVerifyErr
import static com.uniproud.wcb.ErrorUtil.successTrue

@NormalAuthAnnotation
@Transactional(readOnly = true)
class InstallOrderPendingController {

    def baseService
    def modelService
    def smsRecordService

    def list() {
        def extraCondition = {
            isNull('owner')
        }
        render modelService.getDataJSON('install_order_pending', extraCondition,true,true)
    }

    def test(){
        render (view: "Add")
    }

    /**
     * 微信端查看列表
     */
    def wxList(){
        def wx = Weixin.findByAppid(params.state)
        if(!wx){
            render "请从微信公众号中访问！"
            return
        }
        def url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${wx.appid}&secret=${wx.secret}&code=${params.code}&grant_type=authorization_code"
        def json = JSON.parse(url.toURL().text)
        def weixinCustomer = WeixinCustomer.findByOpenid(json?.openid)
        def list = InstallOrder.createCriteria().list {
            eq("customer",weixinCustomer?.customer)
            order("dateCreated", "desc")
        }
        render(view: "wxList",model: [installOrderList:list])
    }
    /**
     * 微信菜单中“安装/维修”跳转
     * @return
     */
    def wxSkip(){
        def wx = Weixin.findByAppid(params.state)
        if(!wx){
            render "请从微信公众号中访问！"
            return
        }
        def url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${wx.appid}&secret=${wx.secret}&code=${params.code}&grant_type=authorization_code"
        def json = JSON.parse(url.toURL().text)
        def weixinCustomer = WeixinCustomer.findByOpenid(json?.openid)
        if(weixinCustomer && weixinCustomer.customer && !weixinCustomer.customer?.deleteFlag) {
            render(view: "installOrderWxAdd", model: [weixinCustomer:weixinCustomer,customer: weixinCustomer?.customer])
        }else if(weixinCustomer){
            render(view: "bingdingWarn", model: [weixinCustomer:weixinCustomer])
        }else{
            render "微信关注者中没有该用户！"
        }
    }
    /**
     * 点击注册，跳转到注册按钮
     * @return
     */
    def toBinding(){
        def weixinCustomer = WeixinCustomer.findByOpenid(params.openid)
        render(view: "customerWxAdd", model: [weixinCustomer:weixinCustomer])
    }
    /**
     * 注册绑定成功后跳转到安装单页面
     * @return
     */
    def toInstallOrderPage(){
        def weixinCustomer = WeixinCustomer.findByOpenid(params.openid)
        render(view: "installOrderWxAdd", model: [weixinCustomer:weixinCustomer,customer: weixinCustomer?.customer])
    }

    @Transactional
    def insert(){
        InstallOrder installOrder = new InstallOrder(params)
        def weixinCustomer = WeixinCustomer.findByOpenid(params.openid)
        installOrder.user = weixinCustomer?.user
        installOrder.customer = weixinCustomer?.customer
        if(params.appointmentDate) {
            installOrder.appointmentDate = DateUtil.toDate(params.appointmentDate, "yyyy-MM-dd HH:mm")
        }
        if(installOrder.hasErrors()){
            log.info installOrder.errors
            def json = [success:false,errors: errorsToResponse(installOrder.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params, installOrder, 'install_order_pending')
    }

    /**
     * 关注者为绑定客户，则创建客户
     * @return
     */
    @Transactional
    def insertCustomer(){
        if(!params.mobile || !params.openid || !params.verifyCode){
            render(successFalse)
            return
        }
        //验证码是否正确
        if (!smsRecordService.checkVerify(params.mobile, params.verifyCode)) {
            render (verifyErr)
            return
        }
        def wxCustomer = WeixinCustomer.findByOpenid(params.openid)
        if(wxCustomer) {
            def customer = new Customer(params)
            customer.user = wxCustomer.user
            customer.customerType = 1
            customer.save()
            def contact = new Contact(params)
            contact.user = wxCustomer.user
            contact.customer = customer
            contact.kind = 1
            contact.save()
            wxCustomer.customer = customer
            wxCustomer.save()
            render (successTrue)
        }else{
            render(successFalse)
        }
    }

}
