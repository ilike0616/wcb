package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

import javax.validation.constraints.Null

@UserAuthAnnotation
@Transactional(readOnly = true)
class PublicCustomerController {
	def modelService
	def baseService

    def list(){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        emps << emp
        def extraCondition = {
            or{
                'in'("customerState",[1,2,4])
                and{
                    eq("customerState",3)
                    inList("owner", emps)
                }
            }
        }
        render modelService.getDataJSON('public_customer',extraCondition,true,true)
    }

    @Transactional
    def insert() {
        Customer customer = new Customer()
        bindData(customer,params,[exclude:['contact']])
        if (customer == null) {
            render baseService.error(params)
            return
        }
        customer.properties["user"] = session.employee?.user
        customer.properties["employee"] = session.employee
        customer.properties["customerType"] = 1
        def result = baseService.save(params, customer)
        if(!JSON.parse(result.toString()).success){
            return result
        }
        if(params.contact){
            Contact contact = new Contact()
            bindData(contact,params,[],"contact")
            contact.user = session.employee?.user
            contact.employee = session.employee
            contact.owner = customer.owner
            contact.dept = customer.dept
            contact.customer = customer
            contact.kind = 1
            def resultContact = baseService.save(params,contact,'contact')
            if(!JSON.parse(resultContact.toString()).success){
                return result
            }
            customer.contact = contact
        }
        render baseService.save(params, customer)
    }

    @Transactional
    def update() {
        Customer customer = Customer.get(params.id as Long)
        if(!customer.contact && params.contact) {
            Contact contact = new Contact()
            bindData(contact,params,[],"contact")
            contact.user = session.employee?.user
            contact.employee = session.employee
            contact.owner = customer.owner
            contact.dept = customer.dept
            contact.customer = customer
            contact.kind = 1
            def resultContact = baseService.save(params,contact,'contact')
            if(!JSON.parse(resultContact.toString()).success){
                return result
            }
            bindData(customer,params,[exclude:['contact']])
            customer.contact = contact
        }else if(customer.contact && params.contact){
            bindData(customer,params)
        }
        render baseService.save(params, customer)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Customer.executeUpdate("update Customer set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }

    /**
     * 分配客户
     */
    @Transactional
    def allocation(){
        if(!params.owner || !params.customers){
            render baseService.error(params)
            return
        }

        def owner = Employee.get(params.owner?.toLong())
        def customerList = JSON.parse(params.customers) as List
        customerList.each {customerId->
            def customer = Customer.get(customerId.toLong())
            customer.properties["allocatee"] = session.employee
            customer.properties["allocatedDate"] = new Date()
            customer.properties["owner"] = owner
            customer.properties["customerType"] = 2 // 我的客户
            customer.properties["customerState"] = 3 // 已分配
            customer?.contact?.each {
                it.owner = owner
                it.save()
            }
        }

        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }

    /**
     * 回收客户
     */
    @Transactional
    def recover(){
        println(params)
        if(!params.customers){
            render baseService.error(params)
            return
        }

        def customerList = JSON.parse(params.customers) as List
        customerList.each {customerId->
            def customer = Customer.get(customerId.toLong())
            customer.properties["allocatee"] = null
            customer.properties["allocatedDate"] = null
            customer.properties["owner"] = null
            customer.properties["customerType"] = 1 // 公海客户
            customer.properties["customerState"] = 4 // 空闲中
            customer?.contact?.each {
                it.owner = null
                it.save()
            }
        }

        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }

    /**
     * 申请客户
     */
    @Transactional
    def apply(){
        println(params)
        if(!params.customers){
            render baseService.error(params)
            return
        }

        def customerList = JSON.parse(params.customers) as List
        customerList.each {customerId->
            def customer = Customer.get(customerId.toLong())
            customer.properties["owner"] = session.employee
            customer.properties["customerState"] = 2 // 申请中
        }

        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }

    /**
     * 申请审核
     */
    @Transactional
    def applyAudit(){
        println(params)
        if(!params.customers){
            render baseService.error(params)
            return
        }

        def allocatee
        def allocatedDate
        def customerType = 1
        def customerState = 1
        if(params.isPass == '1') {  // 审核通过
            allocatee = session.employee
            allocatedDate = new Date()
            customerType = 2
            customerState = 3
        }
        def customerList = JSON.parse(params.customers) as List
        customerList.each {customerId->
            def customer = Customer.get(customerId.toLong())
            if(params.isPass == '1') { // 通过
                customer.properties["allocatee"] = allocatee
                customer.properties["allocatedDate"] = allocatedDate
                customer.properties["customerType"] = 2 // 我的客户
                customer.properties["customerState"] = 3 // 已分配
            }else{
                customer.properties["owner"] = null
                customer.properties["customerState"] = 1 // 空闲中
            }
        }

        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }
}
