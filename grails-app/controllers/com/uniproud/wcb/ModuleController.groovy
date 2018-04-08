package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*
@Transactional(readOnly = true)
class ModuleController {

    @AdminAuthAnnotation
    @UserAuthAnnotation
    def list(){
        def userId = params.user
        if(params.u == 'user'){   // 用户层查询
            userId = session.employee?.user?.id
        }
        def query = Module.where {
            if (params.node && params.node != 'root' && params.node != 'NaN') {
                parentModule {
                    id == params.node
                }
            } else {
                parentModule == null
            }
            if(userId){
                users{
                    id == userId.toLong()
                }
            }
        }
        def modules = []
        def pm = 'parentModule'
        def mn = 'moduleName'
        query.list(sort:'id',order:'ASC').each {
            def leaf = true
            if(it.children) leaf = false
			def iconCls = it.iconCls
			if(!iconCls) iconCls=it.moduleId
            modules << [id:it.id,moduleId:it.moduleId,moduleName:it.moduleName,userTotal:it.users?.size(),expanded:true,
                        iconCls:iconCls,ctrl:it.ctrl,vw:it.vw,isMenu:it.isMenu,
                        parentModule:it.parentModule?.id,parentModuleName:it.parentModule?.moduleName,leaf:leaf,model:[id:it.model?.id,modelName:it.model?.modelName]]
        }
        def json = [success:true,data:modules] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AdminAuthAnnotation
    def moduleList(){
        def query = Module.where {
            parentModule != null
            if(params.user){
                users{
                    id == params.user?.toLong()
                }
            }
        }
        def modules = []
        query.list(sort:'id',order:'ASC').each {
            def leaf = true
            if(it.children) leaf = false
            def iconCls = it.iconCls
            if(!iconCls) iconCls=it.moduleId
            modules << [id:it.id,moduleId:it.moduleId,moduleName:it.moduleName,userTotal:it.users?.size(),expanded:true,
                        iconCls:iconCls,ctrl:it.ctrl,vw:it.vw,
                        parentModule:it.parentModule?.id,parentModuleName:it.parentModule?.moduleName,leaf:leaf,model:[id:it.model?.id,modelName:it.model?.modelName]]
        }
        def json = [success:true,data:modules] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 带复选框
     * @return
     */
    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def modulesForEditCheck(){
        def userModuleIds = []
        if(params.userId){
            def user = User.get(params.userId?.toLong())
            user.modules.each {
                userModuleIds.push(it.id)
            }
        }
        def query = Module.where {
            parentModule == null
        }
        def json = spendChild(query.list(sort:'id',order:'ASC'),true,userModuleIds) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @UserAuthAnnotation
    @Transactional
    def modulesForEdit(){
        def userModuleIds = []
        if(params.userId){
            def user = User.get(params.userId?.toLong())
            user.modules.each {
                userModuleIds.push(it.id)
            }
        }
        def query = Module.where {
            parentModule == null
        }
        def json = spendChild(query.list(sort:'id',order:'ASC'),params.checkModel,userModuleIds) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{ 
            render(json)
        }
    }

    def spendChild(moduleChilds,checkModel,userModuleIds){
        def child = []
        def obj = [:]
        moduleChilds.each {
            obj = [:]
            obj.put("id",it.getId())
            obj.put("text",it.getModuleName())
            obj.put("moduleId",it.getModuleId())
            obj.put("expanded",true)
			def iconCls = it.iconCls
			if(!iconCls) iconCls=it.moduleId
			obj.put("iconCls",iconCls)
            if(it.getChildren()){
                def son = spendChild(it.getChildren(),checkModel,userModuleIds)
                if(son)  obj.put("children", son)
                obj.put("leaf",false)
            }else{
                obj.put("leaf",true)
            }
            if(checkModel){
                if(userModuleIds.size > 0 && userModuleIds.contains(it.getId())){
                    obj.put("checked",true)
                }else{
                    obj.put("checked",false)
                }
            }
            child << obj
        }
        child
    }

    @AdminAuthAnnotation
    @Transactional
    def insert(Module moduleInstance) {
        if (moduleInstance == null) {
            render([success:false] as JSON)
            return
        }
        if (!moduleInstance.validate()) {
            render([success:false,errors: errorsToResponse(moduleInstance.errors)] as JSON)
            return
        }
        moduleInstance.save flush: true
        render([success:true] as JSON)
    }

    @AdminAuthAnnotation
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Module.executeUpdate("delete Module where id in (:ids)",[ids:ids*.toLong()])
        render([success:true] as JSON )
    }


    @AdminAuthAnnotation
    @Transactional
    def update(Long id) {
        def moduleInstance = Module.get(id)
        moduleInstance.properties = params
        log.info moduleInstance.validate()
        if(!moduleInstance.validate()) {
            render([success:false,errors: errorsToResponse(moduleInstance.errors)] as JSON)
            return
        }
        moduleInstance.save flush: true
        render([success:true] as JSON)
    }

    @Transactional
    def save() {
        log.info params
        def data = JSON.parse(params.data)

        def moduleInstance
        data.each {
            moduleInstance = Module.get(it.id)
            moduleInstance.properties = it
            log.info moduleInstance.validate()
            if(!moduleInstance.validate()) {
                render([success:false,errors: errorsToResponse(moduleInstance.errors)] as JSON)
                return
            }
        }
        moduleInstance.save(flush:true)
        render([success:true] as JSON )
    }
}
