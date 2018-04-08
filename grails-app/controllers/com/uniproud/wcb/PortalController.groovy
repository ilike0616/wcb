package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class PortalController {
    def modelService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = Portal.where{
            if(params.moduleId){
                module{
                    id == params.moduleId?.toLong()
                }
            }
        }
        def portals = []
        query.list(params).each {
            portals << ['id':it.id,'title':it.title,'type':it.type,'height':it.height,'xtype':it.xtype,
                        'dateCreated':it.dateCreated,'lastUpdated':it.lastUpdated,viewId:it.viewId,
                        viewStore:it.viewStore]
        }
        def json = [success:true,data:portals, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Portal portal) {
        log.info params
        if (portal == null) {
            render(successFalse)
            return
        }
        if (!portal.validate()) {
            log.info(admin.errors)
            render([success:false,errors: errorsToResponse(portal.errors)] as JSON)
            return
        }
        portal.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Portal.executeUpdate("delete from Portal where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

    @Transactional
    def update(Portal portal) {
        if (portal == null) {
            render(successFalse)
            return
        }
        if (!portal.validate()) {
            render([success:false,errors: errorsToResponse(portal.errors)] as JSON)
            return
        }
        portal.save flush: true
        render(successTrue)
    }
}
