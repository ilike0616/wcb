package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

@Transactional
class WeixinSendMsgService {

    def getMaterial(Weixin weixin,Material material){
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=${weixin.accessToken}")
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = ['media_id':material.mediaId]
            response.success = { resp, json ->
                println body as JSON
                println resp
                println json
            }
        }
    }

    def getVideoNewMediaId (Material material,Weixin weixin){
        def data
        def video = [:]
        video.put('media_id', material.mediaId)
        video.put('title',material.title)
        video.put('description',material.introduction)
        println video as JSON
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=${weixin.accessToken}&&{video}")
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = video
            response.success = { resp, json ->
                println body as JSON
                println resp
                println json
                data = json
            }
        }
        data
    }

    def send(WeixinSendMsg sendMsg,Weixin weixin) {
        def result
        def bodys = [:]
        def touser = []
        def msgtype
        def map = [:]
        if (sendMsg.isAll) {
            WeixinCustomer.where {
                eq('weixin',weixin)
            }.list().each {
                touser << it.openid
            }
        }else{
            SendMsgWeixinCustomer.where{
                eq('sendMsg',sendMsg)
                eq('weixin',weixin)
            }.list().each {
                println it.weixinCustomer
                touser  <<  it.weixinCustomer.openid
            }
        }
        def material = sendMsg.material
        if(material.type=='text'){
            msgtype = 'text'
            map.put('content', sendMsg.material.content)
        }else {
            msgtype = sendMsg.material.type
            if (msgtype == 'news') {
                msgtype = 'mpnews'
            }
            if (msgtype == 'video') {
                def data = getVideoNewMediaId(material,weixin)
                if (data.media_id) {
                    map.put('media_id',data.media_id)
                }
                map.put('title',material.title)
                map.put('description',material.introduction)
            }else{
                map.put('media_id',material.mediaId)
            }
        }
        bodys.put('touser', touser)
        bodys.put('msgtype', msgtype)
        bodys.put(msgtype, map)
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=${weixin.accessToken}")
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = bodys
            response.success = { resp, json ->
                println "success"
                println body as JSON
                println resp
                println json
                result = json
            }
        }
        result
    }
}
