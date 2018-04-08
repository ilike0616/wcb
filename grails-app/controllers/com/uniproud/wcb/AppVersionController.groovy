package com.uniproud.wcb

import com.uniproud.wcb.AppVersion
import com.uniproud.wcb.RequestUtil
import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

@Transactional(readOnly = true)
@AdminAuthAnnotation
class AppVersionController {

    def baseService
    def modelService
    def appVersionService

    def list() {
        RequestUtil.pageParamsConvert(params)
        params.sort = "dateCreated"
        params.order = "desc"
        def query = AppVersion.where {
            deleteFlag == false
        }
        def apps = []
        def fields = modelService.getField('com.uniproud.wcb.AppVersion');
        query.list(params).each {
            def app = [:]
            fields.each { field ->
                app << [(field.fieldName): it."${field.fieldName}"]
            }
            apps << app
        }
        def json = [success: true, data: apps, total: query.count()] as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

    @Transactional
    def insert(AppVersion app) {
        if (app == null) {
            render(successFalse)
            return
        }
        def appPackage = params.appPackage
        appVersionService.saveAppPackage(app, appPackage)
        app.administrator = session.admin
        if (!app.validate()) {
            render([success: false, errors: errorsToResponse(app.errors)] as JSON)
            return
        }
        app.save flush: true
        render(successTrue)
    }

    @Transactional
    def update(AppVersion app) {
        if (app == null) {
            render(successFalse)
            return
        }
//        def appPackage = params.appPackage
//        appVersionService.updateAppPackage(app.platform, appPackage)
        if (!app.validate()) {
            render([success: false, errors: errorsToResponse(app.errors)] as JSON)
            return
        }
        app.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        AppVersion.executeUpdate("update AppVersion set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        render(successTrue)
    }
    @NormalAuthAnnotation
    def newApp(){
        def app = AppVersion.where{
            eq('platform',params.platform)
            eq('edition',params.edition)
        }.find(order:'DESC',sort: 'id',max:1)
        def data = [version:app.appVersion,remark:app.remark]
        if(params.platform=='Android'){
            data << [url:"http://cloud.35crm.com/store/Android/${app.edition}/${app.appName}"]
        }else if(params.platform=='IOS'){
            if(app.edition>=99){
                data << [url: "itms-services://?action=download-manifest&url=https://cloud.35crm.com/store/IOS/${app.edition}/new/upcrm.plist"]
            }else {
                data << [url: "itms-services://?action=download-manifest&url=https://cloud.35crm.com/store/IOS/${app.edition}/new/35crm.plist"]
            }
        }
        render ([success:true,data: data] as JSON)
    }
}
