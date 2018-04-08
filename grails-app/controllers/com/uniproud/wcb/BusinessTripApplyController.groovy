package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class BusinessTripApplyController {

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
        render modelService.getDataJSON('business_trip_apply', extraCondition)
    }

    @Transactional
    def insert(BusinessTripApply trip) {
        if (!auditService.validateApplyStartDateAndEndDate(trip)) {
            render DateErr
            return
        }
        User user = session.employee?.user
        Employee employee = session.employee
        //新建audit
        def auditor = trip.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: trip.subject, type: 2, customer: trip.customer, contact: trip.contact, qz: 'trip', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        //保存trip
        trip.properties['user'] = session.employee?.user
        trip.properties['employee'] = session.employee
        trip.properties['audit'] = audit
        if (!trip.validate()) {
            log.info trip.errors
            def json = [success: false, errors: errorsToResponse(trip.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, trip)
        audit.setTrip(trip)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params, nowAuditOpinion, 'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params, audit, 'work_audit')
        render baseService.success(params)
    }

    @Transactional
    def reAudit(BusinessTripApply trip){
        User user = session.employee?.user
        Employee employee = session.employee
        def auditor = trip.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: trip.subject, type: 2, customer: trip.customer, contact: trip.contact, qz: 'trip', auditState: 1)
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        trip.properties['audit'] = audit
        audit.setTrip(trip)
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.save(params,trip)
    }

    @Transactional
    def update(BusinessTripApply trip) {
        if (!auditService.validateApplyStartDateAndEndDate(trip)) {
            render DateErr
            return
        }
        def audit = trip.audit
        //修改审核人
        def auditorId = (params.get("auditor.id") ?: params.auditor) as Long
        if (auditorId && audit.auditState == 1 && auditorId != audit.auditor.id) {
            def auditor = Employee.load(auditorId)
            if (auditor == null) {
                render baseService.error(params)
                return
            }
            audit.setAuditor(auditor)
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            baseService.save(params, nowAuditOpinion, 'audit_opinion')
        }
        audit.customer = trip.customer
        audit.contact = trip.contact
        baseService.save(params, audit, 'work_audit')
        render baseService.save(params, trip)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        BusinessTripApply.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
        }
        BusinessTripApply.executeUpdate("update BusinessTripApply set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where trip.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
