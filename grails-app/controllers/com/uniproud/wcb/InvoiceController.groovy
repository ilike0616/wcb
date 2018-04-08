package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class InvoiceController {

    def baseService
    def invoiceService
    def modelService

    def list(){
        def extraCondition = {
            if(params.contractOrder){
                eq('contractOrder.id',params.contractOrder as Long)
            }
        }
        render modelService.getDataJSON('invoice',extraCondition)
    }

    def detailList(){
        def emp = session.employee
        def isExcludeEmp = false
        def isPaging = true
        def extraCondition = {
            if(params.object_id){
                eq("invoice.id",params.object_id?.toLong())
            }
            order("lastUpdated","desc")
        }
        if(params.object_id){
            isExcludeEmp = true
            isPaging = false
        }
        render modelService.getDataJSON('invoice_detail',extraCondition,isPaging,isExcludeEmp)
    }

    @Transactional
    def insert(Invoice invoice){
        if (invoice == null) {
            render baseService.error(params)
            return
        }
        render invoiceService.insert(params,invoice)
    }

    @Transactional
    def update(){
        render invoiceService.update(params)
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        Invoice.createCriteria().list {
            'in'("id",ids*.toLong())
        }?.each {
            if(it.audit && (it.audit?.auditState == 2 || it.audit?.auditState == 3)){
                render ([success:false,msg:"正在审核或已通过，不能删除！"] as JSON)
                return
            }
            if(it.invoiceState == 2){
                render ([success:false,msg:"记录已开票，不能删除！"] as JSON)
                return
            }
        }
        Invoice.executeUpdate("update Invoice set deleteFlag=true where id in (:ids)", [ids: ids*.toLong()])
        InvoiceDetail.executeUpdate("update InvoiceDetail set deleteFlag=true where invoice.id in (:ids)", [ids: ids*.toLong()])
        Audit.executeUpdate("update Audit set deleteFlag=true where invoice.id in (:ids)", [ids: ids*.toLong()])
        render baseService.success(params)
    }
}
