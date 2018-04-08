package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class EmployeeLimitController {

    def baseService
    def modelService

    def list() {
        def user = session.employee?.user
        def result = EmployeeLimit.createCriteria().list(params) {
            eq("user", user)
            limiter {
                eq('id', params.employee as Long)
            }
            ne('deleteFlag',true)
        }
        def data = []
        result.each { rs ->
            def depts = []
            rs.depts?.each {dept->
                depts << [id:dept.id,name:dept.name]
            }
            def emps = []
            rs.emps?.each {emp->
                emps << [id:emp.id,name: emp.name]
            }
            data << [id:rs.id,depts:depts,emps:emps,employee:[id: rs.employee?.id,name: rs.employee?.name],
                     module:[id: rs.module?.id,moduleId:rs.module?.moduleId,moduleName:rs.module?.moduleName],
                     limiter:[id:rs.limiter?.id,name: rs.limiter?.name],type:rs.type,remark:rs.remark,dateCreated:rs.dateCreated,lastUpdated:rs.lastUpdated]
        }
        def json = [success: true, data: data, total: result?.totalCount] as JSON
        render json
    }

    def detailList(){
        def result = EmployeeLimitDetail.createCriteria().list(params){
                eq("employeeLimit.id",params.object_id?.toLong())
                eq('deleteFlag', false)
                order('dateCreated',"asc")
            }
        def data = []
        result.each { rs ->
            data << [id: rs.id,fieldName:rs.fieldName,fieldText:rs.fieldText,relation:rs.relation,opt:rs.opt,paramValue:rs.paramValue,paramValueText:rs.paramValueText,
                     beforeBracket:rs.beforeBracket,afterBracket:rs.afterBracket,fromDate:rs.fromDate,toDate:rs.toDate]
        }
        def json = [success: true, data: data, total: result?.totalCount] as JSON
        render json
    }

    @Transactional
    def insert(EmployeeLimit employeeLimit) {
        def employee = session.employee
        def user = employee?.user
        if (employeeLimit == null) {
            render baseService.error(params)
            return
        }
        if(params.depts){
            def depts = params.depts.split(",")
            employeeLimit.properties["depts"] = depts*.toLong()
        }
        if(params.emps){
            def emps = params.emps.split(",")
            employeeLimit.properties["emps"] = emps*.toLong()
        }
        employeeLimit.properties["user"] = user
        employeeLimit.properties["employee"] = employee
        employeeLimit.module = Module.findByModuleId(params.moduleId)
        employeeLimit.limiter = Employee.get(params.limiter as Long)
        if (employeeLimit.hasErrors()) {
            def json = [success:false,errors: errorsToResponse(employeeLimit.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        employeeLimit.save flush: true
        if(params.detail != null){
            def detail = JSON.parse(params.detail)
            detail.each {
                EmployeeLimitDetail eld = new EmployeeLimitDetail()
                bindData(eld,it,[exclude:'id'])
                eld.properties['user'] = user
                eld.properties['employee']= employee
                eld.employeeLimit = employeeLimit
                eld.save()
                employeeLimit.addToDetail(eld)
            }
            employeeLimit.save flush: true
        }
        render baseService.success(params)
    }

    @Transactional
    def update() {
        EmployeeLimit employeeLimit = EmployeeLimit.get(params.id as Long)
        if(params.detail){
            def detail = JSON.parse(params.detail)
            detail.each {
                if(it.id){
                    log.info "==========="
                    log.info it
                    EmployeeLimitDetail od = EmployeeLimitDetail.get(it.id)
                    bindData(od,it,[exclude:['user','employee','employeeLimit']])
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params,json)
                        return
                    }
                    od.save flush: true
                }else{
                    EmployeeLimitDetail od = new EmployeeLimitDetail(it)
                    od.properties['user'] = session.employee?.user
                    od.properties['employee']= session.employee
                    od.employeeLimit = employeeLimit
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params,json)
                        return
                    }
                    od.save flush: true
                    employeeLimit.addToDetail(od)
                }
            }
        }
        if(params.dels){
            def ids = JSON.parse(params.dels)
            if(ids.size()>0)
                EmployeeLimitDetail.executeUpdate("delete from EmployeeLimitDetail where id in (:ids)",[ids:ids*.toLong()])
        }
        bindData(employeeLimit, params,[exclude:'detail'])
        employeeLimit.save flush: true
        render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        EmployeeLimit.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {el->
            el.detail?.each {detail->
                detail.delete()
            }
            el.delete()
        }
        render baseService.success(params)
    }
}
