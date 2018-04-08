package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import grails.util.GrailsUtil
import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@UserAuthAnnotation
@Transactional(readOnly = true)
class CustomerController {
    def modelService
    def baseService
    def grailsApplication

    @UserAuthAnnotation
    @AdminAuthAnnotation
    def list() {
        def extraCondition = {
            eq("customerType", 2)
        }
        render modelService.getDataJSON('customer', extraCondition)
    }

    @NormalAuthAnnotation
    def test(){
        log.info '----------1-----------------'
        def extraCondition = {
            eq("customerType", 2)
        }
        def json = modelService.getDataJSON('customer', extraCondition)
        render (view: "../index",model: [list:json])
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
        customer.properties["customerType"] = 2 // 我的客户
        customer.properties["customerState"] = 3 // 已分配
        customer.properties["allocatee"] = session.employee
        customer.properties["allocatedDate"] = new Date()
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
        def contact = Contact.createCriteria().list {
            customer {
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(contact.size() > 0){
            render ([success:false,msg:"您要删除的数据已关联联系人，不能删除！"] as JSON)
            return
        }
        def follow = CustomerFollow.createCriteria().list {
            customer {
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(follow.size() > 0){
            render ([success:false,msg:"您要删除的数据已关联跟进记录，不能删除！"] as JSON)
            return
        }
        def saleChance = SaleChance.createCriteria().list {
            customer {
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(saleChance.size() > 0){
            render ([success:false,msg:"您要删除的数据已关联销售商机记录，不能删除！"] as JSON)
            return
        }
        def order = ContractOrder.createCriteria().list {
            customer {
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(order.size() > 0){
            render ([success:false,msg:"您要删除的数据已关联订单记录，不能删除！"] as JSON)
            return
        }
        Customer.executeUpdate("update Customer set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }

    @UserAuthAnnotation
    @Transactional
    def searchTransferObj(){
        def transferModuleIds = ['contact','sale_chance','contract_order']
        def modules = Module.createCriteria().list {
            inList('moduleId',transferModuleIds)
        }
        def transferObjs = []
        UserMenu.createCriteria().list {
            inList('module',modules)
            eq('user',session.employee?.user)
        }.each {
            transferObjs << [
                    boxLabel: it.text, name: 'field',inputValue: it?.module?.moduleId
            ]
        }

        def json = [data:transferObjs] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @UserAuthAnnotation
    @Transactional
    def transfer(){
        if(!params.customerIds || !params.owner){
            render baseService.error(params)
            return
        }

        // 转移客户
        def owner = params.owner as Long
        def customerIds = JSON.parse(params.customerIds) as List
        def ids = []
        customerIds.each {
            ids << it.toLong()
        }
        Customer.executeUpdate("update Customer set owner=${params.owner} where id in (:ids)",[ids:ids])

        // 额外转移对象
        def transferModules = []
        if(params.transferModules){
            transferModules = JSON.parse(params.transferModules) as List
        }
        if(transferModules){
            transferModules.each {m->
                def module = Module.findByModuleId(m)
                def mainDomain = grailsApplication.getClassForName(module?.getModel()?.modelClass)
                def fns = Field.where{
                    model{
                        modelClass == mainDomain.name
                    }
                }.list()?.fieldName
                def isContainsOwner = fns.contains('owner')?true:false
                def setValue = "employee"
                if(isContainsOwner == true){
                    setValue = "owner"
                }
                mainDomain.executeUpdate("update ${module.model.modelName} set $setValue=${params.owner} where customer.id in (:ids)",[ids:ids])
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
