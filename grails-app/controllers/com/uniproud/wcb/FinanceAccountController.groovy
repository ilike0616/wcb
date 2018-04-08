package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class FinanceAccountController {

    def modelService
    def baseService

    def list() {
        def extraCondition = {
            if(params.state){
                eq('state',params.state as Integer)
            }
        }
        render modelService.getDataJSON('finance_account', extraCondition,true,true)
    }

    @Transactional
    def insert(FinanceAccount financeAccount) {
        financeAccount.properties['user'] = session.employee?.user
        financeAccount.properties['employee'] = session.employee
        financeAccount.setBalance(financeAccount.initBalance)  //创建时当前余额等于期初余额
        render baseService.save(params, financeAccount)
    }

    @Transactional
    def update(FinanceAccount financeAccount) {
        if(financeAccount.state == 1){
            financeAccount.balance = financeAccount.initBalance
        }else{
            def mfv = baseService.getModifiedField(financeAccount)
            if(mfv?.fieldName?.contains('initBalance') || mfv?.fieldName?.contains('balance')){
                render baseService.error(params)
                return
            }
        }
        render baseService.save(params, financeAccount)
    }

    @Transactional
    def enable() {
        def ids = JSON.parse(params.ids) as List
        FinanceAccount.executeUpdate("update FinanceAccount set state=2 where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }

    @Transactional
    def disable() {
        def ids = JSON.parse(params.ids) as List
        FinanceAccount.executeUpdate("update FinanceAccount set state=3 where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        def records = FinanceIncomeExpense.createCriteria().list {
            financeAccount{
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(records.size() > 0){
            render ([success:false,msg:"您要删除的账户已关联出入账记录，不能删除！"] as JSON)
            return
        }
        FinanceAccount.executeUpdate("update FinanceAccount set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
