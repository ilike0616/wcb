package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@NormalAuthAnnotation
class SmsRecordController {

    def baseService
    def smsRecordService
    @Transactional
    def sendVerifyCode(SmsRecord smsRecord) {
        if (params.mobile == null) {
            render baseService.error(params)
            return
        }
        //获取IP地址
        def ip = WebUtilTools.getRequest().getRemoteAddr()
        //验证是否触发限制
        def checkCode = smsRecordService.checkLimit(params.mobile,ip)
        if(checkCode == 1){
            render ([success:false,limit:true,msg:"获取验证码超过次数上限！"] as JSON)
            return
        }else if(checkCode == 2){
            render ([success:false,limit:true,msg:"获取验证码超过次数上限！"] as JSON)
            return
        }else if(checkCode == 3){
            render ([success:false,limit:true,msg:"获取验证码间隔时间太短！"] as JSON)
            return
        }
        //6位验证码
        def verify = Math.round(Math.random() * 1000000)
        //调用短信接口发送短信
        def result = smsRecordService.sendSms(params.mobile, verify)
        //短信记录
        if (result.code == 2) {
            def record = new SmsRecord(kind: 1, ip: ip, mobile: params.mobile, content: verify, result: result.code)
            render baseService.save(params, record)
        }else{
            def error = [success:false,limit:true,code:result.code,msg:"验证码发送失败！请稍后重试！"] as JSON
            render error
        }
    }
}
