package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class AdministratorController {
    def modelService
    def listForAdmin(){
        RequestUtil.pageParamsConvert(params)
        def query = Administrator.where{
            deleteFlag == false
        }
        def admins = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Administrator');
        query.list(params).each {
            def admin = [:]
            fields.each{field->
                admin << [(field.fieldName):it."${field.fieldName}"]
            }
            admins << admin
        }
        def json = [success:true,data:admins, total: query.count()] as JSON
        println json
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Administrator admin) {
        log.info params
        if (admin == null) {
            render(successFalse)
            return
        }
        if (!admin.validate()) {
            log.info(admin.errors)
            render([success:false,errors: errorsToResponse(admin.errors)] as JSON)
            return
        }
        admin.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Administrator.executeUpdate("update Administrator set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

    @Transactional
    def update(Administrator admin) {
        if (admin == null) {
            render(successFalse)
            return
        }
        if (!admin.validate()) {
            render([success:false,errors: errorsToResponse(admin.errors)] as JSON)
            return
        }
        admin.save flush: true
        render(successTrue)
    }

    @Transactional
    def initPassword() {
        def ids = JSON.parse(params.ids) as List
        Administrator.executeUpdate("update Administrator set password='000000' where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

}
