package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
class MarketActivityCustomerController {
    def modelService
    def baseService
    def list() {
        println 'MarketActivityCustomer  list'
        def user = session.u
        println params
        def extraCondition = {
            if(params.marketActivity){
                eq('marketActivity.id',params.marketActivity as Long)
            }
            if(params.signType == '1'){
                'in'("signType",[1])    //手工签到
            }else if(params.sex == '2') {
                'in'("signType", [2])   //微信签到
            }
        }
        def json = modelService.getDataJSON('market_activity_customer',extraCondition,true)
        render json
    }
    def delete() {
        def ids = JSON.parse(params.ids) as List
        TaskAssigned.executeUpdate("update MarketActivityCustomer set deleteFlag=true, confirmFlag = false where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    def update(MarketActivityCustomer market_activity_customer) {
        render baseService.save(params,market_activity_customer)
    }

    def insert(MarketActivityCustomer market_activity_customer) {
        println params
        if(market_activity_customer == null){
            render baseService.error(params)
            return
        }
        market_activity_customer.properties["user"]= session.employee.user
        market_activity_customer.properties["employee"] = session.employee
        market_activity_customer.setDept(WebUtilTools.session.employee.dept)
        if (market_activity_customer.hasErrors()){
            log.info market_activity_customer.errors
            def json = [success:false,errors: errorsToResponse(market_activity_customer.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params,market_activity_customer)
    }
}
