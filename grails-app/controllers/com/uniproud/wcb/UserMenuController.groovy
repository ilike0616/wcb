package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.successFalse
import static com.uniproud.wcb.ErrorUtil.successTrue

@AdminAuthAnnotation
@UserAuthAnnotation
class UserMenuController {
    @Transactional
    def list(){
        def user = User.findById(session.employee?.user?.id)
        def useUser = user
        if(user?.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        if(params.userId){
            useUser = User.get(params.userId?.toLong())
        }
        if(!useUser){
            render ([success:false]) as JSON
            return
        }
        def query = UserMenu.where{
            parentUserMenu == null
            if(useUser){
                user == useUser
            }
            if(params.module){
                module{
                    id == params.module?.toLong()
                }
            }
        }
        def json = spendChild(query.list(sort:'idx',order:'ASC')) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def spendChild(userMenus){
        def child = []
        def obj = [:]
        userMenus.each {
            obj = [:]
			def iconCls = it.iconCls
			if(!iconCls) iconCls=it.module?.moduleId
            if(it.parentUserMenu){
                obj << ["isLeaf":true]
            }else{
                obj << ["isLeaf":false]
            }
            obj << ["id":it.id,"text":it.text,"iconCls":iconCls,"idx":it.idx,"user":it.user?.id,"name":it.user?.name]
            obj << ["expanded":true,"module":it.module?.id,"moduleId":it.module?.moduleId,"moduleName":it.module?.moduleName,"parentUserMenu":it.parentUserMenu?.id]
            obj << ["parentUserMenuName":it.parentUserMenu?.text,"dateCreated":it.dateCreated,"lastUpdated":it.lastUpdated]
            obj << ["moduleCtrl":it.module?.ctrl]
            if(it.getChildren()){
                def son = spendChild(it.getChildren())
                if(son) obj << ["children":son]
                obj << ["leaf":false]
            }else{
                obj << ["leaf":true]
            }
            child << obj
        }
        child
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def userMenu
        data.each {
            userMenu = UserMenu.get(it.id)
            userMenu.properties = it
            if(!userMenu.validate()) {
                render([success:false,errors: errorsToResponse(userMenu.errors)] as JSON)
                return
            }
        }
        userMenu.save(flush:true)
        render([success:true] as JSON )
    }

    @Transactional
    def dragSortIdx(){
        log.info(params.data)
        def data = JSON.parse(params.data)
        def idxs = data.idxs
        if(!data.id || !idxs){
            render(successFalse)
            return
        }
        // 先把当前对象保存
        def userMenu = UserMenu.get(data.id?.toLong())
        userMenu.properties["parentUserMenu"] = data.parentUserMenu
        userMenu.save(flush: true)

        def userMenus = [:]
        def searchClosure = {
            eq('user.id',userMenu.user?.id)
        }
        UserMenu.createCriteria().list(searchClosure).each {
            userMenus.put(it.id,it)
        }
        idxs.each{
            def id_idx = it.split("_")
            def id = id_idx[0].toLong()
            def idx = id_idx[1].toLong()
            if(userMenus.containsKey(id)){
                def um = userMenus.get(id)
                if(um.idx != idx) {
                    um.idx = idx
                    um.save(flush: true)
                }
            }
        }
        render(successTrue)
    }
}
