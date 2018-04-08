package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@UserAuthAnnotation
class NotifyModelFilterController {

    def modelService
    def baseService

    def list() {
        def user = session.employee?.user
        def emp = session.employee
        // 过滤自己及下属子对象
        def filterIds = []
        if (params.filter) {
            def data = JSON.parse(params.filter)
            def filterName = data[0]?.property
            if (filterName == 'filterSelfAndChildrenId') {
                def e = NotifyModelFilter.get(data[0].value?.toLong())
                filterIds.push(e.id)
                filterIds.addAll(modelService.getChildrenIds(e.children))
            }
        }
        def searchClosure = {
            if (params.node && params.node != 'root' && params.node != 'NaN') {
                eq('parentNotifyModelFilter.id', params.node.toLong())
            } else {
                isNull('parentNotifyModelFilter')
                notifyModel{
                    eq("id",params.notifyModel as Long)
                }
            }
            if (user) {
                eq('user.id', user.id)
            }
            if (filterIds && filterIds.size() > 0) {
                not { 'in'("id", filterIds) }
            }
            eq('deleteFlag', false)
        }
        def query = NotifyModelFilter.createCriteria().list(searchClosure)
        def fields = modelService.getFieldWithOutDot("com.uniproud.wcb.NotifyModelFilter")
        def json = modelService.spendChild(query, fields, false) as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }
    /**
     * 新增、修改是选择上级节点
     * @return
     */
    def treeForEdit() {
        def user = session.employee?.user
        def query = NotifyModelFilter.where {
            parentNotifyModelFilter == null
            user == user
            notifyModel.id == params.notifyModel as Long
            deleteFlag == false
        }
        def json = spendChild(query.list(sort: 'id', order: 'ASC')) as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

    def spendChild(filters) {
        def child = []
        def obj = [:]
        filters.each {
            obj = [:]
            obj.put("id", it.id)
            obj.put("name", it.name)
            obj.put("expanded", true)
            if (it.getChildren()) {
                def son = spendChild(it.getChildren())
                if (son) obj.put("children", son)
                obj.put("leaf", false)
            } else {
                obj.put("leaf", true)
            }
            child << obj
        }
        child
    }

    @Transactional
    def insert(NotifyModelFilter filter) {
        if (filter == null || params.notifyModel == null || params.moduleId == null) {
            render baseService.error(params)
            return
        }
        filter.properties['user'] = session.employee?.user
        filter.properties['employee'] = session.employee
        filter.properties['module'] = Module.findByModuleId(params.moduleId)
        def notifyModel = NotifyModel.get(params.notifyModel as Long)
        if (notifyModel.notifyModelFilter && notifyModel.notifyModelFilter?.deleteFlag == false) {//模型已有对应的条件组，两者一对一，新添加的只能是子条件组
            if (params.parentNotifyModelFilter && params.parentNotifyModelFilter != 'root') {
                filter.properties['parentNotifyModelFilter'] = NotifyModelFilter.get(params.parentNotifyModelFilter as Long)
            } else {
                render baseService.error(params)
                return
            }
        } else {
            filter.properties['notifyModel'] = notifyModel
        }
        filter.save flush: true
        if (!notifyModel.notifyModelFilter  || notifyModel.notifyModelFilter?.deleteFlag) {
            notifyModel.setNotifyModelFilter(filter)            //维护NotifyModel和NotifyModelFilter的关系
            notifyModel.save flush: true
        }
        render baseService.success(params)
    }

    @Transactional
    def update(NotifyModelFilter filter) {
        if (filter == null || params.notifyModel == null) {
            render baseService.error(params)
            return
        }
        def notifyModel = NotifyModel.get(params.notifyModel as Long)
        if (notifyModel.notifyModelFilter?.id != filter.id) {
            if (params.parentNotifyModelFilter && params.parentNotifyModelFilter != 'root') {
                filter.properties['parentNotifyModelFilter'] = NotifyModelFilter.get(params.parentNotifyModelFilter as Long)
            } else {
                render baseService.error(params)
                return
            }
        } else {
            filter.properties['parentNotifyModelFilter'] = null
        }
        render baseService.save(params, filter)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        NotifyModelFilter.executeUpdate("update NotifyModelFilter set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render([success: true] as JSON)
    }

}
