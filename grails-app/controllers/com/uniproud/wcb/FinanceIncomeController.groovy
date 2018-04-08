package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class FinanceIncomeController {

    def modelService
    def baseService
    def financeIncomeService

    def list() {
        def extraCondition = {
            eq("financeType", 1)
            if(params.contractOrder){
                eq('contractOrder.id',params.contractOrder as Long)
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
        render modelService.getDataJSON('finance_income', extraCondition,true,true)
    }

    @Transactional
    def insert(FinanceIncomeExpense income) {
        if (income == null) {
            render baseService.error(params)
            return
        }
        render financeIncomeService.insert(income,params)
    }

    @Deprecated
    @Transactional
    def reAudit(FinanceIncomeExpense income){
        if(!income.auditor){
            render baseService.error(params)
            return
        }
        render financeIncomeService.reAudit(income,params)
    }

    @Transactional
    def update(FinanceIncomeExpense income) {
//        if(income.chargeState == 2){
//            render ([success:false,msg:"该记录已入账，不能修改！"] as JSON)
//            return
//        }
        render financeIncomeService.update(income,params)
    }

    /**
     * 记账
     * @param income
     * @return
     */
    @Transactional
    def charge(FinanceIncomeExpense income) {
        if (!income || income.chargeState != 1) {
            render baseService.error(params)
            return
        }
        income.chargeState = 2
        income.bookkeeper = session.employee
        if (params.financeDate) {
            income.financeDate = DateUtil.toDate(params.financeDate)
        } else {
            income.financeDate = new Date()
        }
        def account = income.financeAccount
        account.balance += income.money
        income.financeAccountBalance = account.balance
        baseService.save(params, account, 'finance_account')
        render baseService.save(params, income)
    }

    @Transactional
    def forbid() {
        def income = FinanceIncomeExpense.get(params.id as Long)
        if (!income || income.chargeState != 1) {
            render baseService.error(params)
            return
        }
        income.chargeState = 3
        income.bookkeeper = session.employee
        render baseService.save(params, income)
    }

    /**
     * 红字更正
     * @return
     */
    @Transactional
    def wrong() {
        def income = FinanceIncomeExpense.get(params.id as Long)
        if (!income || income.chargeState != 2 || income.wrongKind != 1) {
            render baseService.error(params)
            return
        }
        def wrong = new FinanceIncomeExpense()
        wrong.properties = income
        wrong.files = null
        wrong.photos = null
        income.files?.each {
            def doc = new Doc(it.properties)
            wrong.addToFiles(doc)
        }
        income.photos?.each{
            def doc = new Doc(it.properties)
            wrong.addToPhotos(doc)
        }
        wrong.properties['user'] = session.employee?.user
        wrong.properties['employee'] = session.employee
        wrong.financeType = 1
        wrong.wrongKind = 3
        wrong.subject = '更正:'+income.subject
        wrong.money = -income.money
        wrong.financeDate = new Date()
        if (wrong.hasErrors()) {
            def json = [success: false, errors: errorsToResponse(wrong.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        def account = income.financeAccount
        account.balance += wrong.money
        wrong.financeAccountBalance = account.balance
        baseService.save(params, account, 'finance_account')
        income.wrongKind = 2
        baseService.save(params, income)
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
        Audit.executeUpdate("update Audit set deleteFlag=true where income.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }

}
