package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getAuditorNull

@Transactional
class AuditService {

    def baseService
    def grailsApplication

    def checkBeforeAudit(AuditOpinion opinion){
        def audit = opinion.audit
        if(audit.auditState !=1 && audit.auditState!=2){
            return false
        }
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        if(audit.auditor != employee){
            return false
        }
        return true
    }

    def audit(params, AuditOpinion opinion) {
        def user = WebUtilTools.session?.employee?.user
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        baseService.save(params, opinion)
        def audit = Audit.get(opinion.audit.id)
        if (opinion.state == 2) {//通过
            audit.auditState = 3
        } else if (opinion.state == 3) {//通过，继续流转
            if (!opinion.nextAuditor) {
                return (auditorNull)
            }
//			audit.setNowAuditOpinion(nowAuditOpinion)	//待审意见记录

            audit.auditState = 2        //审核中
            //修改申请单的当前审核人
            if (audit.qz == "goout") {
                def goout = audit.goout
                goout.setAuditor(opinion.nextAuditor)
                goout.save()
            } else if (audit.qz == "trip") {
                def trip = audit.trip
                trip.setAuditor(opinion.nextAuditor)
                trip.save()
            } else if (audit.qz == "leave") {
                def leave = audit.leave
                leave.setAuditor(opinion.nextAuditor)
                leave.save()
            } else if (audit.qz == "overtime") {
                def overtime = audit.overtime
                overtime.setAuditor(opinion.nextAuditor)
                overtime.save()
            } else if (audit.qz == "fareClaims") {
                def fareClaims = audit.fareClaims
                fareClaims.setAuditor(opinion.nextAuditor)
                fareClaims.save()
            } else if (audit.qz == 'income') {
                def income = audit.income
                income.setAuditor(opinion.nextAuditor)
            } else if (audit.qz == 'expense') {
                def expense = audit.expense
                expense.setAuditor(opinion.nextAuditor)
            }
        } else if (opinion.state == 4) {//未通过
            audit.auditState = 4
        } else {
            return baseService.error(params)
        }
        audit.addToAuditOpinions(opinion)
        //如果此用户在审核人列表中则在增加
        if (!audit.auditors?.id.contains(employee.id)) {
            audit.addToAuditors(employee)
        }
        if (audit.hasErrors()) {
            println audit.errors
        }
        baseService.save(params, audit, 'work_audit')
        if(opinion.state == 2){
            //费用报销审核通过时生成财务查账单
            if (audit.qz == "fareClaims"){
                def fare = audit.fareClaims
                FinanceIncomeExpense expense = new FinanceIncomeExpense(user: user,employee: fare.employee,financeType:2,audit: audit,auditor: audit.auditor,subject: fare.subject,money: fare.money,owner: fare.owner)
                expense.linkType = 2
                expense.fareClaims = fare
                expense.oppositeAccount = fare.owner?.name
                if(expense.hasErrors()){
                    log.info expense.errors
                }
                baseService.save(params, expense,'finance_expense')
            }
        }else if (opinion.state == 3) {
            def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: opinion.nextAuditor, audit: audit, state: 1/*未审核*/, content: '')
            if (nowAuditOpinion.hasErrors()) {
                println nowAuditOpinion.errors
            }
            nowAuditOpinion.save flush: true
            audit.setAuditor(opinion.nextAuditor)        //修改当前审核人
            audit.nowAuditOpinion = nowAuditOpinion
            baseService.save(params, audit)
        }
        baseService.success(params)
    }

    def validateApplyStartDateAndEndDate(apply) {
        if (apply.startDate > apply.endDate) {
            return false
        }
        def className = apply?.getClass()?.getName()
        if (BusinessTripApply.createCriteria().list {
            if (className == 'com.uniproud.wcb.BusinessTripApply' && apply.id) {
                ne('id', apply.id)
            }
            eq('employee', apply.employee)
            or {
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    gt('startDate', apply.startDate)
                    lt('endDate', apply.endDate)
                }
            }
            ne('deleteFlag', true)
        }) {
            return false
        }
        if (GoOutApply.createCriteria().list {
            if (className == 'com.uniproud.wcb.GoOutApply' && apply.id) {
                ne('id', apply.id)
            }
            eq('employee', apply.employee)
            or {
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    gt('startDate', apply.startDate)
                    lt('endDate', apply.endDate)
                }
            }
            ne('deleteFlag', true)
        }) {
            return false
        }
        if (LeaveApply.createCriteria().list {
            if (className == 'com.uniproud.wcb.LeaveApply' && apply.id) {
                ne('id', apply.id)
            }
            eq('employee', apply.employee)
            or {
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    gt('startDate', apply.startDate)
                    lt('endDate', apply.endDate)
                }
            }
            ne('deleteFlag', true)
        }) {
            return false
        }
        if (OvertimeApply.createCriteria().list {
            if (className == 'com.uniproud.wcb.OvertimeApply' && apply.id) {
                ne('id', apply.id)
            }
            eq('employee', apply.employee)
            or {
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    lt('startDate', apply.startDate)
                    gt('endDate', apply.startDate)
                }
                and {
                    gt('startDate', apply.startDate)
                    lt('endDate', apply.endDate)
                }
            }
            ne('deleteFlag', true)
        }) {
            return false
        }
        return true
    }
}
