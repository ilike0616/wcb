package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class FareClaimsController {

    def modelService
    def baseService

    def list() {
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
            if(params.customer){
                eq("customer.id",params.customer as Long)
            }
            if(params.saleChance){
                eq("saleChance.id",params.saleChance as Long)
            }
            if(params.marketActivity){
                eq("marketActivity.id",params.marketActivity as Long)
            }
            if(params.businessTripApply){
                eq("businessTripApply.id",params.businessTripApply as Long)
            }
            if(params.goOutApply){
                eq("goOutApply.id",params.goOutApply as Long)
            }
        }
        render modelService.getDataJSON('fare_claims', extraCondition)
    }

    def detailList() {
        if (params.object_id) {
            if (params.object_id?.toLong() > 0) {
                def emp = session.employee
                def extraCondition = {
                    eq("fareClaims.id", params.object_id?.toLong())
                    eq('deleteFlag', false)
                }
                render modelService.getDataJSON('fare_claims_detail', extraCondition,true,true)
            } else {
                render baseService.success(params)
            }
        } else {
            def emp = session.employee
            def extraCondition = {}
            render modelService.getDataJSON('fare_claims_detail', extraCondition)
        }
    }

    @Transactional
    def insert(FareClaims fareClaims) {
        if (fareClaims == null) {
            render baseService.error(params)
            return
        }
        def user = session.employee?.user
        def employee = session.employee
        fareClaims.properties['user'] = user
        fareClaims.properties['employee'] = employee
        if (fareClaims.hasErrors()) {
            println fareClaims.errors
            def json = [success: false, errors: errorsToResponse(fareClaims.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        if (params.detail != null) {
            def detail = JSON.parse(params.detail)
            //遍历明细、保存
            detail.each {
                FareClaimsDetail od = new FareClaimsDetail(it)
                od.properties['user'] = user
                od.properties['employee'] = employee
                if (od.hasErrors()) {
                    println od.errors
                    def json = [success: false, errors: errorsToResponse(od.errors)] as JSON
                    render baseService.validate(params, json)
                    return
                }
                od.save()
                fareClaims.addToDetail(od)
            }
        }
        baseService.save(params, fareClaims)
        if(fareClaims.auditor){ //创建审核任务
            def auditor = fareClaims.auditor
            def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: fareClaims.subject, type: 5, customer: fareClaims.customer, contact: fareClaims.contact, qz: 'fareClaims', auditState: 1)
            audit.fareClaims = fareClaims
            if (!audit.validate()) {
                log.info audit.errors
                def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
                render baseService.validate(params, json)
                return
            }
            baseService.save(params,audit,'work_audit')
            def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '').save()
            audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
            baseService.save(params,audit,'work_audit')
            fareClaims.audit = audit
        }
        render baseService.save(params, fareClaims)
    }

    @Transactional
    def reAudit(FareClaims fareClaims){
        User user = session.employee?.user
        Employee employee = session.employee
        def auditor = fareClaims.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: fareClaims.subject, type: 5, customer: fareClaims.customer, contact: fareClaims.contact, qz: 'fareClaims', auditState: 1)
        audit.fareClaims = fareClaims
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        fareClaims.audit = audit
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.save(params,fareClaims)
    }

    @Transactional
    def update() {
        FareClaims fareClaims = FareClaims.get(params.id as Long)
        bindData(fareClaims, params, [exclude: 'detail'])
        if (params.detail) {
            def detail = JSON.parse(params.detail)
            detail.each {
                if (it.id) {
                    FareClaimsDetail od = FareClaimsDetail.get(it.id)
                    bindData(od, it, [exclude: ['user','employee']])
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success: false, errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params, json)
                        return
                    }
                    baseService.save(params, od, 'fare_claims_detail')
                } else {
                    FareClaimsDetail od = new FareClaimsDetail(it)
                    od.properties['user'] = session.employee?.user
                    od.properties['employee'] = session.employee
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success: false, errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params, json)
                        return
                    }
                    od.fareClaims = fareClaims
                    baseService.save(params, od, 'fare_claims_detail')
                }
            }
        }
        if (params.dels) {
            def ids = JSON.parse(params.dels)
            if (ids.size() > 0)
                FareClaimsDetail.executeUpdate("update FareClaimsDetail set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        }
        def audit = fareClaims.audit
        def auditorId = (params.get("auditor.id")?:params.auditor) as Long
        if(auditorId && audit.auditState == 1 && auditorId != audit.auditor.id){//修改审核人
            def auditor = Employee.load(auditorId)
            if (auditor == null) {
                render baseService.error(params)
                return
            }
            audit.setAuditor(auditor)
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            nowAuditOpinion.save flush: true
        }
        audit.customer = fareClaims.customer
        audit.contact = fareClaims.contact
        baseService.save(params,audit,'work_audit')
        render baseService.save(params, fareClaims)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        FareClaims.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
        }
        FareClaims.executeUpdate("update FareClaims set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        FareClaimsDetail.executeUpdate("update FareClaimsDetail set deleteFlag=true where fareClaims.id in (:ids)",[ids:ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where fareClaims.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
