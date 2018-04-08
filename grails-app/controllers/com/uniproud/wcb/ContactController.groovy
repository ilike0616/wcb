package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class ContactController {
	
	def modelService
	def baseService
    def privilegeService

    def list(){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def extraCondition = {
            if(params.specialParam){
                eq("customer.id",params.specialParam.toLong())
            }else if(params.customer){
				eq('customer.id',params.customer as Long)
			}else{
                def userOperations = privilegeService.getEmployeePrivilegeOperations(Employee.get(emp?.id),'view')
                or {
                    eq("owner", emp)        //自己的客户
                    if(!params.onlySearchOwner || params.onlySearchOwner.toBoolean() == false){ // 默认查询包含下级
                        if (emps) {
                            inList('owner', emps)
                        }
                    }
                    if(userOperations && userOperations.contains("public_customer_view")){  //如果有公海客户权限，则可查询空闲、申请中、回收空闲客户的联系人
                        customer {
                            inList('customerState',[1,2,4])
                        }
                    }
                }
            }
        }
		render modelService.getDataJSON('contact',extraCondition,true,true)
    }

    @Transactional
    def insert(Contact contact) {
		if (contact == null) {
			render baseService.error(params)
			return
		}
		contact.properties["user"] = session.employee.user
		contact.properties["employee"] = session.employee
        contact.owner = contact.customer?.owner
        if(contact.kind == 1){
            def customer = contact.customer
            def contact1 = customer.contact
            if(contact1) {
                contact1.kind = 2
                contact1.save()
            }
            customer.contact = contact
            customer.save()
        }
		render baseService.save(params,contact)
    }

    @Transactional
    def update(Contact contact) {
        def mfv =  baseService.getModifiedField(contact)
        if(mfv?.fieldName?.contains('kind')){
            def customer = contact.customer     //客户
            if(contact.kind == 1) {
                def contact1 = customer.contact     //原主联系人
                if (contact1){
                    contact1.kind = 2
                    contact1.save()
                }
                customer.contact = contact
            }else if(contact.kind == 2){
                customer.contact = null
            }
            customer.save()
        }
//        chain(controller:'base',action: 'save',params:params,model:[domain:contact])
		render baseService.save(params,contact)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Contact.executeUpdate("update Contact set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
//		chain(controller:'base',action: 'success',params:params)
		render baseService.success(params)
    }

}
