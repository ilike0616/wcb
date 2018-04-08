package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

@AdminAuthAnnotation
@Transactional(readOnly = true)
class EditionController {

    def modelService

    def list() {
        RequestUtil.pageParamsConvert(params)
        def query = Edition.where {
            deleteFlag == false
            if(params.tplUser=='1'){//只查询关联企业模板的的版本
                templateUser != null
            }
        }
        def data = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Edition');
        query.list(sort: "kind", order: "asc").each {
            def domain = [:]
            fields.each { field ->
                domain << [(field.fieldName): it."${field.fieldName}"]
            }
            domain << [templateUser:it.templateUser?.id,templateUserName:it.templateUser?.name]
            data << domain
        }
        def json = [success: true, data: data, total: query.count()] as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

    @Transactional
    def insert(Edition edition){
        if (edition == null) {
            render(successFalse)
            return
        }
        if (!edition.validate()) {
            render([success:false,errors: errorsToResponse(edition.errors)] as JSON)
            return
        }
        if(edition.templateUser){
            edition.templateUser.isTemplate = true
        }
        edition.save flush: true
        render(successTrue)
    }

    @Transactional
    def update(Edition edition){
        if (edition == null) {
            render(successFalse)
            return
        }
        if (!edition.validate()) {
            render([success:false,errors: errorsToResponse(edition.errors)] as JSON)
            return
        }
        if(edition.templateUser){
            edition.templateUser.isTemplate = true
            edition.templateUser.edition = edition
        }
        edition.save flush: true
        render(successTrue)
    }
}
