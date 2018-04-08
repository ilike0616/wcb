package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class OvertimeApplyController {

    def modelService
    def baseService
    def auditService

    def list() {
        def emp = session.employee
        def extraCondition = {
            if (params.auditType == '1') {//待审核
                audit {
                    inList("auditState", [1, 2])
                }
            } else if (params.auditType == '2') {//已审核
                audit {
                    inList("auditState", [3, 4])
                }
            }
        }
        render modelService.getDataJSON('overtime_apply', extraCondition)
    }

    @Transactional
    def insert(OvertimeApply overtime) {
        if (!auditService.validateApplyStartDateAndEndDate(overtime)) {
            render DateErr
            return
        }
        User user = session.employee?.user
        Employee employee = session.employee
        //新建audit
        def auditor = overtime.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: overtime.subject, type: 4, qz: 'overtime', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        //保存overtime
        overtime.properties['user'] = session.employee?.user
        overtime.properties['employee'] = session.employee
        overtime.properties['audit'] = audit
        if (!overtime.validate()) {
            log.info overtime.errors
            def json = [success: false, errors: errorsToResponse(overtime.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, overtime)
        audit.setOvertime(overtime)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params, nowAuditOpinion, 'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params, audit, 'work_audit')
        render baseService.success(params)
    }

    @Transactional
    def reAudit(OvertimeApply overtime){
        User user = session.employee?.user
        Employee employee = session.employee
        def auditor = overtime.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: overtime.subject, type: 4, qz: 'overtime', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        overtime.properties['audit'] = audit
        audit.setOvertime(overtime)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.save(params,overtime)
    }

    @Transactional
    def update(OvertimeApply overtime) {
        if (!auditService.validateApplyStartDateAndEndDate(overtime)) {
            render DateErr
            return
        }
        def audit = overtime.audit
        //修改审核人
        def auditorId = (params.get("auditor.id") ?: params.auditor) as Long
        if (auditorId && audit.auditState == 1 && auditorId != audit.auditor.id) {
            def auditor = Employee.load(auditorId)
            if (auditor == null) {
                render baseService.error(params)
                return
            }
            audit.setAuditor(auditor)
            audit.save flush: true
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            baseService.save(params, nowAuditOpinion, 'audit_opinion')
        }
        render baseService.save(params, overtime)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        OvertimeApply.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
        }
        OvertimeApply.executeUpdate("update OvertimeApply set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where overtime.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
