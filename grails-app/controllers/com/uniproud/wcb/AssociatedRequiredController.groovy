package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@AdminAuthAnnotation
class AssociatedRequiredController {

    def baseService
    def modelService

    def list() {
        RequestUtil.pageParamsConvert(params)
        def query = AssociatedRequired.where {
            user.id == params.user as Long
            module.id ==  params.module as Long
            deleteFlag == false
        }
        def data = []
        def fields = modelService.getField('com.uniproud.wcb.AssociatedRequired');
        query.list(params).each {
            def userFields = []
            it.userFields?.each {uf->
                userFields << [id:uf.id,fieldName:uf.fieldName,text:uf.text]
            }
            data << [id:it.id,name:it.name,kind:it.kind,userFields:userFields]
        }
        def json = [success: true, data: data, total: query.count()] as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

    @Transactional
    def insert(){
        if(!params.user || !params.module || !params.userfields){
            render baseService.error(params)
            return
        }
        def user = User.get(params.user as Long)
        def module = Module.get(params.module as Long)
        def ar = new AssociatedRequired(user:user,module: module,name:params.name)
        params.userfields?.each{fieldName->
            ar.addToUserFields(UserField.findByUserAndModuleAndFieldName(user,module,fieldName))
        }
        ar.save flush: true
        render baseService.success(params)
    }

    @Transactional
    def update(AssociatedRequired ar){
        if(!ar){
            render baseService.error(params)
            return
        }
        ar.userFields?.clear()
        params.userfields?.each{fieldName->
            ar.addToUserFields(UserField.findByUserAndModuleAndFieldName(ar.user,ar.module,fieldName))
        }
        ar.save flush: true
        render baseService.success(params)
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        AssociatedRequired.executeUpdate("update AssociatedRequired set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
