package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class SitePhotoController {

    def baseService
    def modelService

    def list(Integer max) {
        def extraCondition = {
        }
        render modelService.getDataJSON('site_photo', extraCondition)
    }

    @Transactional
    def insert(SitePhoto sitePhoto) {
        if(sitePhoto == null){
            render baseService.error(params)
            return
        }
        sitePhoto.properties["user"] = session.employee.user
        sitePhoto.properties["employee"] = session.employee
        render baseService.save(params,sitePhoto)
    }

    @Transactional
    def update(SitePhoto sitePhoto) {
        render baseService.save(params,sitePhoto)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        SitePhoto.executeUpdate("update SitePhoto set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

}
