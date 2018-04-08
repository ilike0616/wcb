package com.uniproud.wcb

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

@Transactional
class WeixinMenuService {

    def init() {
        def m = []
        def params = WebUtilTools.params
        //通过company.id查找parentMenu为空的对象集合 并根据idx排序
        def menus = WeixinMenu.where{
            eq('weixin.id',  params.weixin as Long)
            isNull('parentMenu')
        }.list([order: 'asc',sort: 'idx'])
        //迭代menus并把数据写入m[]  并返回
        menus.each {
            def chi = getChildren(it)
            if(chi) {
                if(it.material){
                    m << [id: it.id, text: it.name, name: it.name, type: it.type,url:it.url, idx: it.idx, expanded: true,children:chi,material:[id:it.material.id,title:it.material.title]]//,appendOnly: true,ddGroup :it.name,dropGroup:it.name
                }else{
                    m << [id: it.id, text: it.name, name: it.name, type: it.type,url:it.url, idx: it.idx, expanded: true,children:chi]
                }
            }else{
                if(it.material){
                    m << [id: it.id, text: it.name, name: it.name, type: it.type, url:it.url,idx: it.idx,leaf: true, expanded: true,material:[id:it.material.id,title:it.material.title]]//material:it.material, msgType:it.msgType,content:it.content
                }else{
                    m << [id: it.id, text: it.name, name: it.name, type: it.type, url:it.url,idx: it.idx,leaf: true, expanded: true]
                }

            }
        }
        m
    }

    def getChildren(WeixinMenu parent){
        def chi = []
        def menu =  WeixinMenu.where{
            eq('parentMenu',parent)
        }.list([order: 'asc',sort: 'idx'])
        menu.each {
            println it
            if(it.material){
                chi << [id:it.id, text:it.name,name:it.name,type:it.type,url:it.url,idx:it.idx,leaf: true,material:[id:it.material.id,title:it.material.title]]//material:it.material,
            }else{
                chi << [id:it.id, text:it.name,name:it.name,type:it.type,url:it.url,idx:it.idx,leaf: true]
            }
        }
        chi
    }

    /**
     * 发送post 请求的方式。仅供参考
     * @return
     */
    def create(Weixin weixin) {
        def errcode
        def menus = WeixinMenu.where{
            eq('weixin.id',  weixin.id)
            isNull('parentMenu')
        }.list([order: 'asc',sort: 'idx'])
        def m = []
        menus.each {
            def chi = sub_button(it)
            if(chi) {
                def one = [:]
                one.put("name", it.name)
                one.put("sub_button", chi)
                m << one
            }else{
                def one = [:]
                one.put("name", it.name)
                if (it.type==1) {
                    String begin = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe7ffa2a4e3962f7e&redirect_uri=http://wx2.uniproud.com/wx/customer/auth?rurl="
                    String end = "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
                    def url = begin+ it.url + end
                    one.put('url', url)
                    one.put("type", 'view')
                }else if(it.type==2){
                    one.put('type','click')
                    one.put('key', it.material)
                }
                m << one
            }
        }
        println m
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=${weixin.accessToken}")
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = [
                    "button":m
            ]
            response.success = { resp, json ->
                println resp
                println json
                errcode = json.errcode
            }
        }
        errcode
    }

    def sub_button(WeixinMenu parent){
        def chi = []
        def menu =  WeixinMenu.where{
            eq('parentMenu',parent)
        }.list([order: 'asc',sort: 'idx'])
        menu.each {
            def sub =[:]
            sub.put("name", it.name)
            if (it.type==1) {
                String begin = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe7ffa2a4e3962f7e&redirect_uri=http://wx2.uniproud.com/wx/customer/auth?rurl="
                String end = "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
                def url = begin+ it.url + end
                sub.put('url', url)
                sub.put("type", 'view')
            }else if(it.type==2){
                sub.put('type','click')
                sub.put('key', it.id)
            }
            chi << sub
        }
        chi
    }
}
