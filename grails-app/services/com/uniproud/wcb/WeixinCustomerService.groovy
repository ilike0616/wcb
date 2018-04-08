package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@Transactional
class WeixinCustomerService {
    /***
     * 初始化 某个公众帐号的 关注着
     * @param id
     * @return
     */
    def init(Long id) {
        getOpenId(Weixin.get(id))
    }
    /**
     * 获取公共帐号关注者的openId集合
     * @param weixin
     * @param NEXT_OPENID
     * @return
     */
    def getOpenId(Weixin wx,String NEXT_OPENID = null){
        if(wx){
            def url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=${wx.accessToken}"
            if(NEXT_OPENID){
                url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=${wx.accessToken}&NEXT_OPENID=${NEXT_OPENID}"
            }
            def text = url.toURL().text
            //返回数据类型{"total":2,关注该公众账号的总用户数
            //"count":2,拉取的OPENID个数，最大值为10000
            //"data":{"openid":["","OPENID1","OPENID2"]},OPENID的列表
            //"next_openid":"NEXT_OPENID"拉取列表的最后一个用户的OPENID}
            def json = JSON.parse(text)
            json.data?.openid.each{openId ->
                initCustomerForOpenId(wx,openId,json.next_openid)
            }
        }
    }
    /**
     * 把数据持久化
     * @param weixin
     * @param openId
     * @param NEXT_OPENID
     * @return
     */
    def initCustomerForOpenId(Weixin wx,String openId,String NEXT_OPENID = null){
        def url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=${wx.accessToken}&openid=${openId}&lang=zh_CN"
//        def text = url.toURL().text
        def json = null
        def http = new HTTPBuilder(url)
        http.request(Method.POST, ContentType.JSON) {
            headers.encoding = "utf-8"
            response.success = { resp, reader ->
                json = reader
            }
        }
        def customer = WeixinCustomer.findByOpenid(openId)
        // 获取用户基本信息 必须通过微信认证
        if(customer){//说明该用户之前已经记录，需要更新客户的数据信息
            customer.properties = json
            def subscribe_time = new Date((json.subscribe_time as Long)*1000)
            customer.subscribeTime = subscribe_time
            if(customer.hasErrors()){
                println customer.errors
                return
            }
            customer.save flush: true
        }else{
            //说明该用户之前无记录，需要创建
            customer = new WeixinCustomer(json)
            customer.weixin = wx
            customer.setUser(wx.user)
            def subscribe_time = new Date((json.subscribe_time as Long)*1000)
            customer.subscribeTime = subscribe_time
            if(customer.hasErrors()){
                println customer.errors
                return
            }
            customer.save flush: true
        }
        customer
    }

    def getCustomerForOpenId(openId){
        def customer
        if(openId){
            customer = WeixinCustomer.findByOpenid(openId)
        }
        customer
    }

}
