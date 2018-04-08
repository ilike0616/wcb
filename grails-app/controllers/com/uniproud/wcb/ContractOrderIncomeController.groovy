package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class ContractOrderIncomeController {

    def baseService
    def financeIncomeService
    def modelService

    def list() {
        def extraCondition = {
            eq("financeType", 1)
            isNotNull("contractOrder")
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
        render modelService.getDataJSON('contract_order_income', extraCondition)
    }

    @Transactional
    def insert(FinanceIncomeExpense income) {
        if (income == null || !income.contractOrder) {
            render baseService.error(params)
            return
        }
        income.incomeKind = 1
        render financeIncomeService.insert(income,params)
    }

    @Transactional
    def update(){
        def income = FinanceIncomeExpense.get(params.id as Long)
        if(income.contractOrder && params.money && income.money != (params.money as Double)){
            if((params.money as Double) - income.money > income.contractOrder?.payingMoney){
                render ([success:false,msg:"付款金额不能大于订单应付金额，修改失败！"] as JSON)
                return
            }
        }
        income.properties = params
        render financeIncomeService.update(income,params)
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
