package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class ContractOrderService {
    def baseService

    def updateSaleChance(ContractOrder contractOrder) {
        def sum = 0.0
        def saleChance = contractOrder.saleChance
        if(saleChance){
            ContractOrder.createCriteria().list {
                eq("saleChance",saleChance)
                eq("deleteFlag",false)
            }?.each {
                sum += it.discountMoney?:0
            }
            saleChance.properties["orderMoney"] = sum
            saleChance.save()
        }
    }

    def updateCustomerLatestOrderDate(ContractOrder contractOrder) {
        if(contractOrder.customer){
            def customer = Customer.get(contractOrder.customer.id)
            customer.latestOrderDate = new Date()
            customer.save()
        }
    }

    def updatePaidState(ContractOrder order){
        def sum = order.paidMoney
        order.payingMoney = order.discountMoney - sum
        if(order.discountMoney > 0) {
            if(sum <= 0){
                order.paidState = 1
            }else if(0<sum && sum<order.discountMoney){
                order.paidState = 2
            }else if(sum >= order.discountMoney){
                order.paidState =3
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
    }

    def updateInvoiceState(ContractOrder order){
        def state = 1
        if(Math.abs(order.invoiceMoney - order.discountMoney) < 1){
            state = 3
        }else if(order.invoiceMoney != 0){
            state = 2
        }
        order.invoiceState = state
    }

}
