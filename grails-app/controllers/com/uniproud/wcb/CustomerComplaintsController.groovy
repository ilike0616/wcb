package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
@UserAuthAnnotation
@Transactional(readOnly = true)
class CustomerComplaintsController {

    def modelService
    def baseService

    def list() {
        def extraCondition = {
            if(params.customer){
                eq('customer.id',params.customer as Long)
            }
        }
        render modelService.getDataJSON('customer_complaints',extraCondition)
    }

    @Transactional
    def insert(CustomerComplaints customerComplaints) {
        if (customerComplaints == null) {
            render baseService.error(params)
            return
        }

        customerComplaints.properties['user'] = session.employee?.user
        customerComplaints.properties['employee'] = session.employee
        params.moduleId = 'customer_complaints'
        render baseService.save(params,customerComplaints)
    }
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        CustomerComplaints.executeUpdate("update CustomerComplaints set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
    @Transactional
    def update(CustomerComplaints customerComplaints) {
        render baseService.save(params,customerComplaints)
    }
}
