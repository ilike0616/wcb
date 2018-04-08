package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
@UserAuthAnnotation
@Transactional(readOnly = true)
class CustomerCareController {

    def modelService
    def baseService

    def list() {
        def extraCondition = {
            if(params.customer){
                eq('customer.id',params.customer as Long)
            }
        }
        render modelService.getDataJSON('customer_care',extraCondition)
    }

    @Transactional
    def insert(CustomerCare customerCare) {
        if (customerCare == null) {
            render baseService.error(params)
            return
        }

        customerCare.properties['user'] = session.employee?.user
        customerCare.properties['employee'] = session.employee
        params.moduleId = 'customer_care'
        render baseService.save(params,customerCare)
    }
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        CustomerCare.executeUpdate("update CustomerCare set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
    @Transactional
    def update(CustomerCare customerCare) {
        render baseService.save(params,customerCare)
    }
}
