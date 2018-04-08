package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class EmployeeNotifyModelController {

    def baseService

    def list() {
        def user = session?.employee?.user
        def emp = session?.employee
        def result = NotifyModel.createCriteria().list(params) {
            eq("user", user)
            eq("isAllowForbid", true)
            if (params.moduleId) {
                module {
                    eq('moduleId', params.moduleId)
                }
            }
        }
        def data = []
        result.each { nm ->
            def enm = EmployeeNotifyModel.findByEmployeeAndNotifyModel(emp, nm)
            if (enm) {
                data << [id: enm.id, notifyModel: [id: nm.id, name: nm.name, module: [moduleId: nm.module?.moduleId]], isRecv: enm.isRecv, subjectTemplate: enm.subjectTemplate, contentTemplate: enm.contentTemplate]
            } else {
                data << [id: null, notifyModel: [id: nm.id, name: nm.name, module: [moduleId: nm.module?.moduleId]], isRecv: nm.isDefaultRecv, subjectTemplate: nm.subjectTemplate, contentTemplate: nm.contentTemplate]
            }
        }
        def json = [success: true, data: data, total: result.totalCount] as JSON
        render json
    }

    def forbidList() {
        def user = session?.employee?.user
        def emp = session?.employee
        def result = NotifyModel.createCriteria().list(params) {
            eq("user", user)
            eq("isAllowForbid", false)
            if (params.moduleId) {
                module {
                    eq('moduleId', params.moduleId)
                }
            }
        }
        def data = []
        result.each { nm ->
            data << [id: null, notifyModel: [id: nm.id, name: nm.name, module: [moduleId: nm.module?.moduleId]], isRecv: nm.isDefaultRecv, subjectTemplate: nm.subjectTemplate, contentTemplate: nm.contentTemplate]
        }
        def json = [success: true, data: data, total: result.totalCount] as JSON
        render json
    }

    @Transactional
    def update(EmployeeNotifyModel empModel) {
        if (empModel == null) {
            render baseService.error(params)
            return
        }
        if (!empModel.id) {
            def user = session.employee?.user
            def emp = session.employee
            empModel.properties['user'] = user
            empModel.properties['employee'] = emp
            empModel.setNotifyModel(NotifyModel.get(params.notifyModel?.id as Long))
        }
        render baseService.save(params, empModel)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        EmployeeNotifyModel.executeUpdate("delete EmployeeNotifyModel where id in (:ids)", [ids: ids*.toLong()])
        render([success: true] as JSON)
    }
}
