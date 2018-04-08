package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class FinanceExpenseController {

    def modelService
    def baseService

    def list() {
        def extraCondition = {
            eq("financeType", 2)
            if(params.fareClaims){
                eq('fareClaims.id',params.fareClaims as Long)
            }
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
        render modelService.getDataJSON('finance_expense', extraCondition,true,true)
    }

    @Transactional
    def insert(FinanceIncomeExpense expense) {
        if (expense == null) {
            render baseService.error(params)
            return
        }
        def user = session.employee?.user
        def employee = session.employee
        expense.properties['user'] = user
        expense.properties['employee'] = employee
        expense.properties['financeType'] = 2
        expense.properties['wrongKind'] = 1
        if (expense.hasErrors()) {
            def json = [success: false, errors: errorsToResponse(expense.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        if(expense.financeAccount && expense.financeAccount.balance < expense.money){
            render (accountBalanceNotEnough)
            return
        }
        baseService.save(params, expense)
        if(expense.fareClaims){
            def fare = expense.fareClaims
            fare.addToFinanceExpense(expense)
            fare.save flush: true
        }
        if (expense.auditor) { //创建审核任务
            def auditor = expense.auditor
            def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: expense.subject, type: 7, qz: 'expense', auditState: 1)
            audit.expense = expense
            if (!audit.validate()) {
                log.info audit.errors
                def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
                render baseService.validate(params, json)
                return
            }
            baseService.save(params, audit, 'work_audit')
            def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '').save()
            audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
            baseService.save(params, audit, 'work_audit')
            expense.audit = audit
        }
        render baseService.save(params, expense)
    }

    @Transactional
    def reAudit(FinanceIncomeExpense expense){
        User user = session.employee?.user
        Employee employee = session.employee
        def auditor = expense.auditor
        def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: expense.subject, type: 7, qz: 'expense', auditState: 1)
        audit.expense = expense
        if (!audit.validate()) {
            log.info audit.errors
            def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, audit, 'work_audit')
        expense.audit = audit
        //新建审核意见AuditOpinion
        def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
        baseService.save(params,nowAuditOpinion,'audit_opinion')
        audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
        baseService.save(params,audit,'work_audit')
        render baseService.save(params,expense)
    }

    @Transactional
    def update(FinanceIncomeExpense expense) {
        def mfv = baseService.getModifiedField(expense)
        if(mfv?.fieldName?.contains('money')){
            if(expense.financeAccount && expense.financeAccount.balance < expense.money){
                render (accountBalanceNotEnough)    //账户余额不足
                return
            }
        }
        if (mfv?.fieldName?.contains('auditor')) {
            def audit = expense.audit
            def auditor = expense.auditor
            if (auditor == null) {
                render baseService.error(params)
                return
            }
            audit.setAuditor(auditor)
            def nowAuditOpinion = audit.nowAuditOpinion
            nowAuditOpinion.setAuditor(auditor)
            nowAuditOpinion.save flush: true
            baseService.save(params, audit, 'work_audit')
        }
        render baseService.save(params, expense)
    }

    /**
     * 记账
     * @param income
     * @return
     */
    @Transactional
    def charge(FinanceIncomeExpense expense){
//        def expense = FinanceIncomeExpense.get(params.id as Long)
//        log.info expense.chargeState
        if(!expense || expense.chargeState != 1){
            render baseService.error(params)
            return
        }
        def account = expense.financeAccount
        if(account.balance < expense.money){
            render (accountBalanceNotEnough)
            return
        }
        expense.chargeState = 2
        expense.bookkeeper = session.employee
        if(params.financeDate){
            expense.financeDate = DateUtil.toDate(params.financeDate)
        }else{
            expense.financeDate = new Date()
        }
        account.balance -= expense.money
        expense.financeAccountBalance = account.balance
        baseService.save(params,account,'finance_account')
        render baseService.save(params,expense)
    }


    @Transactional
    def forbid(){
        def expense = FinanceIncomeExpense.get(params.id as Long)
        if(!expense || expense.chargeState != 1){
            render baseService.error(params)
            return
        }
        expense.chargeState = 3
        expense.bookkeeper = session.employee
        render baseService.save(params,expense)
    }

    @Transactional
    def wrong(){
        def expense = FinanceIncomeExpense.get(params.id as Long)
        if (!expense || expense.chargeState != 2 || expense.wrongKind != 1) {
            render baseService.error(params)
            return
        }
        def wrong = new FinanceIncomeExpense()
        wrong.properties = expense
        wrong.files = null
        wrong.photos = null
        expense.files?.each {
            def doc = new Doc(it.properties)
            wrong.addToFiles(doc)
        }
        expense.photos?.each{
            def doc = new Doc(it.properties)
            wrong.addToPhotos(doc)
        }
        wrong.properties['user'] = session.employee?.user
        wrong.properties['employee'] = session.employee
        wrong.financeType = 2
        wrong.wrongKind = 3     //红字更正记录
        wrong.subject = '更正:'+expense.subject
        wrong.money = -expense.money
        wrong.financeDate = new Date()
        if (wrong.hasErrors()) {
            println wrong.errors
            def json = [success: false, errors: errorsToResponse(wrong.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        def account = expense.financeAccount
        account.balance -= wrong.money
        wrong.financeAccountBalance = account.balance
        baseService.save(params, account, 'finance_account')
        expense.wrongKind = 2   //更正源标记
        baseService.save(params, expense)
        render baseService.save(params, wrong)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        FinanceIncomeExpense.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
            if(it.chargeState == 2){
                render ([success:false,msg:"记录已记账，不能删除！"] as JSON)
                return
            }
        }
        FinanceIncomeExpense.executeUpdate("update FinanceIncomeExpense set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where expense.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
