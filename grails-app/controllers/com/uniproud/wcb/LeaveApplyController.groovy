package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class LeaveApplyController {

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
        render modelService.getDataJSON('leave_apply', extraCondition)
    }

    @Transactional
    def insert(LeaveApply leave) {
        if (!auditService.validateApplyStartDateAndEndDate(leave)) {
            render DateErr
            return
        }
        User user = session.employee?.user
        Employee employee = session.employee
        //新建audit
        def auditor = leave.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: leave.subject, type: 3, qz: 'leave', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params,audit,'work_audit')
        //保存leave
        leave.properties['user'] = session.employee?.user
        leave.properties['employee'] = session.employee
        leave.properties['audit'] = audit
        if (!leave.validate()) {
            log.info leave.errors
            def json = [success: false, errors: errorsToResponse(leave.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params,leave)
        audit.setLeave(leave)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.success(params)
    }

    @Transactional
    def reAudit(LeaveApply leave){
        User user = session.employee?.user
        Employee employee = session.employee
        def auditor = leave.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: leave.subject, type: 3, qz: 'leave', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params,audit,'work_audit')
        leave.properties['audit'] = audit
        audit.setLeave(leave)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.save(params,leave)
    }

    @Transactional
    def update(LeaveApply leave) {
        if (!auditService.validateApplyStartDateAndEndDate(leave)) {
            render DateErr
            return
        }
        def audit = leave.audit
        //修改审核人
        def auditorId = (params.get("auditor.id")?:params.auditor) as Long
        if (auditorId && audit.auditState == 1 && auditorId != audit.auditor.id) {
            def auditor = Employee.load(auditorId)
            if (auditor == null) {
                render baseService.error(params)
                return
            }
            audit.setAuditor(auditor)
            baseService.save(params,audit,'work_audit')
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            baseService.save(params,nowAuditOpinion,'audit_opinion')
        }
        render baseService.save(params, leave)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        LeaveApply.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
        }
        LeaveApply.executeUpdate("update LeaveApply set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where leave.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
