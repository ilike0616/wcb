package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class FinanceIncomeService {

    def baseService

    def updateContractOrder(FinanceIncomeExpense income) {
        if(income.financeType == 1 && income.contractOrder && income.contractOrder.discountMoney){    //入账
            def sum = 0
            def order = income.contractOrder
            FinanceIncomeExpense.createCriteria().list {
                eq("contractOrder",order)
                eq("chargeState",2)
                eq("deleteFlag",false)
            }?.each {
                if((it.audit == null || it.audit?.auditState != 4) && it.money) {
                    sum += it.money
                }
            }
            order.paidMoney = sum
            order.payingMoney = order.discountMoney - sum
            if(order.discountMoney > 0) {
                if (sum <= 0) {
                    order.paidState = 1
                } else if (0 < sum && sum < order.discountMoney) {
                    order.paidState = 2
                } else if (sum >= order.discountMoney) {
                    order.paidState = 3
                }
            }else{
                if(sum >= 0){
                    order.paidState = 1
                }else if(0 > sum && sum > order.discountMoney){
                    order.paidState = 2
                }else if(sum <= order.discountMoney){
                    order.paidState = 3
                }
            }
            order.save()
        }
    }

    def insert(FinanceIncomeExpense income,params){
        def user = WebUtilTools.session?.employee?.user
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        income.properties['user'] = user
        income.properties['employee'] = employee
        income.properties['financeType'] = 1
        income.properties['wrongKind'] = 1
        if (income.hasErrors()) {
            println income.errors
            def json = [success: false, errors: errorsToResponse(income.errors)] as JSON
            return baseService.validate(params, json)
        }
        if(income.contractOrder){
            def order = income.contractOrder
            if(order.payingMoney > 0 ? income.money > order.payingMoney : income.money < order.payingMoney){
                return ([success:false,msg:"付款金额不能大于订单应付金额，新增失败！"] as JSON)
            }
            order.addToFinanceIncome(income)
            order.save flush: true
            income.incomeKind = 1
            income.linkType = 1
        }
        baseService.save(params, income)
        if (income.auditor) { //创建审核任务
            def auditor = income.auditor
            def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: income.subject, type: 6, qz: 'income', auditState: 1)
            audit.income = income
            if (!audit.validate()) {
                log.info audit.errors
                def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
                return baseService.validate(params, json)
            }
            baseService.save(params, audit, 'work_audit')
            def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '').save()
            audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
            baseService.save(params, audit, 'work_audit')
            income.audit = audit
        }
        return baseService.save(params, income)
    }

    def update(FinanceIncomeExpense income,params){
        def mfv = baseService.getModifiedField(income)
        if (mfv?.fieldName?.contains('auditor')) {
            def audit = income.audit
            def auditor = income.auditor
            if (auditor == null) {
                return baseService.error(params)
            }
            audit.setAuditor(auditor)
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            nowAuditOpinion.save flush: true
            baseService.save(params, audit, 'work_audit')
        }
        return baseService.save(params, income)
    }

    @Deprecated
    def reAudit(FinanceIncomeExpense income,params){
        def user = WebUtilTools.session?.employee?.user
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        def auditor = income.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: income.subject, type: 6, qz: 'income', auditState: 1)
        audit.income = income
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            return baseService.validate(params, json)
        }
        baseService.save(params, audit, 'work_audit')
        income.audit = audit
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        return baseService.save(params,income)
    }
}
