package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class WeixinCustomerController {
    def modelService
    def baseService

    def list() {
        def user = session.u
        def extraCondition = {
            if(params.weixin){
                eq('weixin.id',params.weixin as Long)
            }
            if(params.sex == '1'){
                'in'("sex",[1])    //男
            }else if(params.sex == '2') {
                'in'("sex", [2])   //女
            }
        }
        def json = modelService.getDataJSON('weixin_customer',extraCondition,true,true)
        render json
    }
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        TaskAssigned.executeUpdate("update WeixinCustomer set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
    @Transactional
    def update(WeixinCustomer weixin_customer) {
        render baseService.save(params,weixin_customer)
    }
    @Transactional
    def insert(WeixinCustomer weixin_customer) {
        println weixin_customer
        println params
        if(weixin_customer == null){
            render baseService.error(params)
            return
        }
        weixin_customer.properties["user"]= session.employee.user
        weixin_customer.properties["employee"] = session.employee
//        weixin_customer.properties["customer"] = Customer.findByUserAndEmployee()
        if (weixin_customer.hasErrors()){
            log.info weixin_customer.errors
            def json = [success:false,errors: errorsToResponse(weixin_customer.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params,weixin_customer)
    }
}
