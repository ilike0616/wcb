package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

@UserAuthAnnotation
class WeixinSendMsgController {
    def weixinSendMsgService
    def materialService
    def newsMaterialService
    def list(Long id) {
        println params
        def sendMsgList = WeixinSendMsg.where {
            eq('weixin.id',params.weixin as Long)
        }.list()
        def jsonData = []
        sendMsgList.each {
            if (it.material.type =='text') {
                jsonData << [id:it.id,isAll:it.isAll,content:it.material.content,'msgtype':'text',url:null,type:'text']//,customers:customers
            }else{
                jsonData << [id:it.id,isAll:it.isAll,content:it.material.content,'msgtype':'text',type:it.material.type,title:it.material.title,url:it.material.doc.name,material:[id:it.material.id]]//,customers:customers
            }
        }
        render ([success:true,data:jsonData]as JSON)
    }
    def insert(){
        def sendMsg = new WeixinSendMsg(params)
        sendMsg.setMaterial(Material.get(params.material as Long))
        sendMsg.setWeixin(Weixin.get(params.weixin as Long))
        sendMsg.properties["user"]= session.employee.user
        sendMsg.properties["employee"] = session.employee
        sendMsg.save(flush:true)
        if(params.customers){
            def customers = params.customers*.toLong()
            customers.each{
                println it
                def cus =WeixinCustomer.get(it as Long)
                println cus
                def sendmsgcustomer = new SendMsgWeixinCustomer()
                sendmsgcustomer.properties["user"]= session.employee.user
                sendmsgcustomer.properties["employee"] = session.employee
                sendmsgcustomer.setSendMsg(sendMsg)
                sendmsgcustomer.setWeixinCustomer(cus)
                sendmsgcustomer.setWeixin(sendMsg.weixin)
                println sendmsgcustomer as JSON
                if(cus){
                    sendmsgcustomer.setContact(cus.contact)
                    sendmsgcustomer.setCustomer(cus.customer)
                }
                sendmsgcustomer.save(flush:true)
            }
        }
        def weixin = sendMsg.getWeixin()
        if(sendMsg.material.type != 'text'&&sendMsg.material.mediaId == null){
            if(sendMsg.material.type == 'news'){
                def news = NewsMaterial.findByMaterialAndIdx(sendMsg.material,1)
                if(news.material.mediaId == null){
                    materialService.create(news.material,weixin)
                }
                newsMaterialService.create(news)
            }else{
                materialService.create(sendMsg.material,weixin)
            }
        }
        def result = weixinSendMsgService.send(sendMsg,weixin)
        if (result.errcode==0) {
            sendMsg.msgId = result.msg_id
            sendMsg.save(flush:true)
            render([success:true] as JSON)
        }else{
            render([success:false] as JSON)
        }
    }

    def destroy(Long id){
        println params
        def sendMsg = WeixinSendMsg.get(id)
        sendMsg.delete(flush:true)
        render([success:true] as JSON)
    }

    def update(Long id){
        println params
        def sendMsg = WeixinSendMsg.get(id)
        sendMsg.properties = params
        if(sendMsg.hasErrors()){
            render([success:false] as JSON)
        }else{
            sendMsg.save(flush:true)
            render([success:true] as JSON)
        }
    }

    def send(Long id){
        println params
        def sendMsg = WeixinSendMsg.get(id)
        def weixin = WeixinSendMsg.getWeixin()
        def result = WeixinSendMsgService.send(sendMsg,weixin)
        if (result.errcode==0) {
            sendMsg.msgId = result.msg_id
            sendMsg.save(flush:true)
            render([success:true] as JSON)
        }else{
            render([success:false] as JSON)
        }
    }
}
