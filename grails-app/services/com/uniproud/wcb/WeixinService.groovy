package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import javax.net.ssl.HttpsURLConnection
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

@Transactional
class WeixinService {

    def weixinCustomerService

    def initToken(){
        def now = new Date()
        println "now：${now.format('yyyy-MM-dd hh:mm:ss.S')}"
        def diff = new Date(now['time'] + 10*60*1000)
        def tokens = Weixin.where {
            endExpires == null|| endExpires < diff
            state == 2
        }.list()
        if(tokens) {
            println "$tokens.token 重新生成token"
            getTokens(tokens)
        }else{
            println "暂时没有需要更换的token"
        }
    }
    /**
     * 更换多个tokens
     * @param tokens
     * @return
     */
    def getTokens(List<Weixin> tokens){
        tokens.each {
            getToken(it)
        }
    }
    /**
     * 更换单个token
     * @param tk
     * @return
     */
    def getToken(Weixin tk){
        def url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${tk.appid}&secret=${tk.secret}"
        def text = url.toURL().text
        def json = JSON.parse(text)
        if(json.access_token){
            def now = new Date()
            tk.accessToken = json.access_token
            tk.expiresin = json.expires_in
            tk.startExpires = now
            tk.endExpires = new Date(now['time']+json.expires_in*1000)
            if(tk.hasErrors()){
                return
            }
            tk.save()
        }
    }

    /**
     * 效验 该公众号是否在平台做了绑定
     * @param params
     * @return
     */
    def checkSignature() {
        def params = WebUtilTools.params
        println params
        def token = Weixin.get(params.id as Long).token
        if(!token){
            return false
        }
        def signature = params.signature
        println signature
        def timestamp = params.timestamp
        def nonce = params.nonce
        def tmpArr = [token,timestamp,nonce]
        tmpArr.sort()
        def tmpStr = ""
        tmpArr.each {tmpStr += it }
        if(signature==tmpStr.encodeAsSHA1())  {
            return true
        } else {
            return false
        }
    }

    def parse(){
        def request = WebUtilTools.request
        def xml = new XmlParser().parse(new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8")))
        def map = [:]
        xml.iterator().each{k->
            map.put(k.name(),k.text())
        }
        def weixin = Weixin.findByYsid(map.ToUserName)
        if(map.MsgType=='event') {
            if(map.Ticket){
                def weixinCustomer
                if(map.Event=='SCAN'){
                    //关注后扫描推送
                    log.info('weixinService parse  SCAN  Event')
                    println map
                    println '----------------------------SCAN'    //签到
                    weixinCustomer = WeixinCustomer.findByOpenid(map.FromUserName)
                    if(weixinCustomer.contact){
                        println 'weixinCustomer.contact'
                        println weixinCustomer.contact
                        def json = [:]
                        json.put('ToUserName',weixinCustomer.openid)
                        json.put('FromUserName',weixin.ysid)
                        json.put('CreateTime',new Date())
                        json.put('MsgType','text')
                        json.put('Content','您好！/::*感谢关注傲融CRM软件—您身边专业的客户管理软件！/::)我们一直在努力，欢迎您的见证！/:,@-D')
                        return json
                    }else{
                        println 'weixinCustomer.contact'
                        println weixinCustomer.contact
                        def json = [:]
                        json.put('ToUserName',weixinCustomer.openid)
                        json.put('FromUserName',weixin.ysid)
                        json.put('CreateTime',new Date().getTime())
                        json.put('MsgType','text')
                        json.put('Content','<a href=\"http://wx2.uniproud.com/wcb/mui1/1.html\">111111111111</a>\n' +
                                '<a href="http://wx2.uniproud.com/wcb/mui1/2.html">2222222</a>\n' +
                                '<a href="http://wx2.uniproud.com/wcb/mui1/3.html">3333333</a>\n' +
                                '<a href="http://wx2.uniproud.com/wcb/mui1/4.html">4444444</a>\n' +
                                '<a href="http://wx2.uniproud.com/wcb/mui1/5.html">555555555</a>\n' +
                                '<a href="http://wx2.uniproud.com/wcb/mui1/6.html">6666666</a>' )
                        return json
                    }
                }
                if(map.Event=='subscribe'){
                    log.info('weixinService parse  subscribe  Event')
                    weixinCustomer = weixinCustomerService.initCustomerForOpenId(weixin,map.FromUserName)
                    def json = [:]
                    json.put('ToUserName',weixinCustomer.openid)
                    json.put('FromUserName',weixin.ysid)
                    json.put('CreateTime',new Date())
                    json.put('MsgType','text')
                    json.put('Content','您好！/::*感谢关注傲融CRM软件—您身边专业的客户管理软件！/::)我们一直在努力，欢迎您的见证！/:,@-D')
                    return json
                }
            }
           else{
                if(map.Event=='subscribe'){
                    log.info('weixinService parse  subscribe  Event')
                    def weixinCustomer = weixinCustomerService.initCustomerForOpenId(weixin,map.FromUserName)
                    def json = [:]
                    json.put('ToUserName',weixinCustomer.openid)
                    json.put('FromUserName',weixin.ysid)
                    json.put('CreateTime',new Date())
                    json.put('MsgType','text')
                    json.put('Content','您好！/::*感谢关注傲融CRM软件—您身边专业的客户管理软件！/::)我们一直在努力，欢迎您的见证！/:,@-D')
                    return json
                }
                println 'not SCAN  subscribe'
                return null
            }

        }else{
            println 'not event '
            return null
        }
    }
}
