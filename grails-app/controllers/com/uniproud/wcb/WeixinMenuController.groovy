package com.uniproud.wcb

import grails.converters.JSON
import com.uniproud.wcb.annotation.UserAuthAnnotation

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
class WeixinMenuController {
    def weixinMenuService
    def baseService

    def list() {
        println params
        println 'WeixinMenuController   list'
        def menus = weixinMenuService.init()
        println menus as JSON
        render menus as JSON
    }

    def delete(Long id) {
        println params
        println 'WeixinMenuController   delete'
        def menu = WeixinMenu.get(id)
        WeixinMenu.where {
            eq('parentMenu',menu)
        }.deleteAll()
        menu.delete flush: true
        render([success:true] as JSON)
    }

    def insert(WeixinMenu menu) {
        println  params
        menu.properties["user"] = session.employee?.user
        menu.properties["employee"] = session.employee
        if(menu == null){
            render baseService.error(params)
            return
        }
        def menus = WeixinMenu.where {
            eq('weixin',Weixin.get(params.weixin as Long))
        }.list([order: 'asc',sort: 'idx'])
        if(menus.size()!=0){
            def idx = menus.get((menus.size()-1)).idx + 1
            menu.setIdx(idx)
        }else{
            menu.setIdx(1)
        }
        if (menu.hasErrors()){
            log.info menu.errors
            def json = [success:false,errors: errorsToResponse(menu.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params,menu)
    }

    def release(Long id) {
        println params
        println id
        def weixin =  Weixin.get(id)
        println weixin
        def errcode = weixinMenuService.create(weixin)
        if (errcode == 0) {
            render([success:true] as JSON)
        }else{
            render([success:false] as JSON)
        }
    }

    def sort() {
        def idxs = params.idxs
        def ids = params.ids
        def parentId = params.parentId
        for(def i = 0;i< ids.length;i++){
            def	menu = WeixinMenu.get(ids[i] as Long)
            def	idx = idxs[i] as int
            menu.setIdx(idx)
            if(parentId == 'root'){
                menu.setParentMenu(null)
            }else{
                menu.setParentMenu(WeixinMenu.get(parentId))
            }
            menu.save(flush:true)
        }
        render([success:true] as JSON)
        println 'WeixinMenuController   sort'
    }
}
