package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
class WeixinController {
    def modelService
    def baseService
    def weixinService
    def weixinCustomerService

    def list() {

        def extraCondition = {
            if(params.state == '1'){
                'in'("state",[1])  // 1,禁用
            }else if(params.state == '2'){
                'in'("state",[2])   //2，已激活
            }else if(params.state =='3'){
                'in'("state",[3])   //3，待审查
            }
        }
        def json = modelService.getDataJSON('weixin',extraCondition,true)
        render json
    }
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        TaskAssigned.executeUpdate("update Weixin set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
    @Transactional
    def update(Weixin weixin) {
        def res =  baseService.save(params,weixin)
        if(weixin.state==2) {
            weixinService.initToken()
            weixinCustomerService.init(weixin.id)
        }
        render res
    }
    @Transactional
    def insert(Weixin weixin) {
        if(weixin == null){
            render baseService.error(params)
            return
        }
        weixin.properties["user"]= session.employee.user
        weixin.properties["employee"] = session.employee
        if (weixin.hasErrors()){
            log.info weixin.errors
            def json = [success:false,errors: errorsToResponse(weixin.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        def res = baseService.save(params,weixin)
        if(res.properties.target.success == false){
            render ([success:true,"msg":"操作失败！"])
        }else{
            weixinService.initToken()
            weixinCustomerService.init(weixin.id)
            render res
        }
    }

    /**
     * 微信消息处理入口
     * @return
     */
    @NormalAuthAnnotation
    def auth(){
        println params
        if(weixinService.checkSignature()){
            println params.echostr+'params.echostr'
            if(!params.echostr){
                def resultXml
                def json = weixinService.parse()//messageService  消息处理
                println json
                if(json!=null){
                }else{
                    resultXml = ''
                }
                resultXml = """
                  <xml>
                        <ToUserName><![CDATA[${json.ToUserName}]]></ToUserName>
                        <FromUserName><![CDATA[${json.FromUserName}]]></FromUserName>
                        <MsgType><![CDATA[${json.MsgType}]]></MsgType>
                        <CreateTime>${json.CreateTime}</CreateTime>
                        <Content><![CDATA[${json.Content}]]></Content>
                    </xml>
                  """
                println resultXml
                render resultXml
            } else {
                println '初始化            params.id的customer'
                render params.echostr
            }
        }else{
            render 'error'
        }
    }
}
