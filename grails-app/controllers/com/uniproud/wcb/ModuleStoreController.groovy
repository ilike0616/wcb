package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class ModuleStoreController {
    def modelService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = ModuleStore.where{
            if(params.moduleId){
                module{
                    id == params.moduleId?.toLong()
                }
            }
        }
        def moduleStores = []
        query.list(params).each {
            moduleStores << [id:it.id,name:it.name,moduleName:it.module?.moduleName,moduleId:it.module.id,
                        store:it.store,'dateCreated':it.dateCreated,'lastUpdated':it.lastUpdated]
        }
        def json = [success:true,data:moduleStores, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(ModuleStore moduleStore) {
        if (moduleStore == null) {
            render(successFalse)
            return
        }
        if (!moduleStore.validate()) {
            render([success:false,errors: errorsToResponse(moduleStore.errors)] as JSON)
            return
        }
        moduleStore.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ModuleStore.executeUpdate("delete from ModuleStore where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

    @Transactional
    def update(ModuleStore moduleStore) {
        if (moduleStore == null) {
            render(successFalse)
            return
        }
        if (!moduleStore.validate()) {
            render([success:false,errors: errorsToResponse(moduleStore.errors)] as JSON)
            return
        }
        moduleStore.save flush: true
        render(successTrue)
    }
}
