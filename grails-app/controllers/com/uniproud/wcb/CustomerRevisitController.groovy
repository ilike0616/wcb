package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.transaction.Transactional
import grails.converters.JSON

@UserAuthAnnotation
@Transactional(readOnly = true)
class CustomerRevisitController {

    def modelService
    def baseService

    def list() {
        def extraCondition = {
            if(params.customer){
                eq('customer.id',params.customer as Long)
            }
        }
        render modelService.getDataJSON('customer_revisit',extraCondition)
    }

    @Transactional
    def insert(CustomerRevisit customerRevisit) {
        if (customerRevisit == null) {
            render baseService.error(params)
            return
        }

        customerRevisit.properties['user'] = session.employee?.user
        customerRevisit.properties['employee'] = session.employee
        params.moduleId = 'customer_revisit'
        render baseService.save(params,customerRevisit)
    }
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        CustomerRevisit.executeUpdate("update CustomerRevisit set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
    @Transactional
    def update(CustomerRevisit customerRevisit) {
        render baseService.save(params,customerRevisit)
    }

}
