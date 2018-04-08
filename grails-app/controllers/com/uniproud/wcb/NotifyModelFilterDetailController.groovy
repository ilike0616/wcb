package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@UserAuthAnnotation
class NotifyModelFilterDetailController {

    def modelService
    def baseService

    def list() {
        def user = session.employee?.user
        def result = NotifyModelFilterDetail.createCriteria().list(params) {
            eq("user", user)
            notifyModelFilter {
                eq('id', params.notifyModelFilter as Long)
            }
        }
        def filter = NotifyModelFilter.get(params.notifyModelFilter as Long)
        def data = []
        result.each { rs ->
            def userField = UserField.findByUserAndModuleAndFieldName(user, filter.module, rs.fieldName)
            def expectText = rs.expectValue
            if (rs.isDict) {
                expectText = DataDictItem.findByUserAndDictAndItemId(user, userField.dict, rs.expectValue)?.text
            }
            def dbTypeName
            switch (rs.dbType){
                case 'java.lang.Integer':
                case 'java.lang.Long':
                case 'java.lang.Double' :
                case 'java.math.BigDecimal':
                    dbTypeName = '数字'
                    break
                case 'java.lang.String' :
                    dbTypeName = '字符'
                    break
                case 'java.util.Date' :
                    dbTypeName = '日期'
                    break
                case 'java.lang.Boolean' :
                    dbTypeName = '真假'
                    expectText = rs.expectValue as Boolean ? "是":"否"
                    break
                case 'com.uniproud.wcb.Doc' :
                    dbTypeName = '文件'
                    break
            }
            data << [id: rs.id, name: rs.name, fieldName: rs.fieldName, fieldText: userField?.text, dbType: rs.dbType, dbTypeName:dbTypeName, isDict: rs.isDict,
                     expectType: rs.expectType, expectValue: rs.expectValue, expectText: expectText, operator: rs.operator, dateCreated: rs.dateCreated, lastUpdated: rs.lastUpdated]
        }
        def json = [success: true, data: data, total: result.totalCount] as JSON
        render json
    }

    def loadFieldList() {
        def module = Module.findByModuleId(params.moduleId)
        def user = session.employee?.user
        def fields = UserField.createCriteria().list {
            eq("user.id", user.id)
            eq("module.id", module.id)
            //isNotNull("dict")
            ne('dbType', 'com.uniproud.wcb.Doc')
            not { ilike('fieldName', 'id') }
            not { ilike('fieldName', 'user') }
            not { ilike('fieldName', '%.%') }
            order('text')
        }
        def list = []
        fields.each {
            list << [fieldName: it.fieldName, fieldText: it.text, dbType: it.dbType, dict: it.dict?.id]
        }
        def json = [success: true, data: list, total: list.size()] as JSON
        render json
    }

    @Transactional
    def insert(NotifyModelFilterDetail detail) {
        if (detail == null || params.notifyModelFilter == null) {
            render baseService.error(params)
            return
        }
        detail.setUser(session.employee?.user)
        detail.setEmployee(session.employee)
        detail.setNotifyModelFilter(NotifyModelFilter.get(params.notifyModelFilter as Long))
        if (!detail.validate()) {
            log.info detail.errors
            def json = [success: false, errors: errorsToResponse(detail.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        render baseService.save(params, detail)
    }

    @Transactional
    def update(NotifyModelFilterDetail detail) {
        render baseService.save(params, detail)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        NotifyModelFilterDetail.executeUpdate("delete NotifyModelFilterDetail where id in (:ids)", [ids: ids*.toLong()])
        render([success: true] as JSON)
    }
}
