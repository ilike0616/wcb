package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

@UserAuthAnnotation
class SendMsgWeixinCustomerController {
    def modelService
    def list() {
        println 'SendMsgWeixinCustomerController  list'
        println params
        def user = session.u
        def extraCondition = {
            if(params.sendMsgId){
                eq('sendMsg.id',params.sendMsgId as Long)
            }
            if(params.weixinId){
                eq('weixin.id',params.weixinId as Long)
            }
        }
        def json = modelService.getDataJSON('send_msg_weixin_customer',extraCondition,true)
        println(json)
        render json
    }
}
