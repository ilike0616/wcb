package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class FinanceInvoiceController {

    def baseService
    def invoiceService
    def modelService

    def list(){
        def extraCondition = {
            if(params.contractOrder){
                eq('contractOrder.id',params.contractOrder as Long)
            }
        }
        render modelService.getDataJSON('finance_invoice',extraCondition,true,true)
    }

    def detailList(){
        def emp = session.employee
        def isExcludeEmp = false
        def extraCondition = {
            if(params.object_id){
                eq("invoice.id",params.object_id?.toLong())
            }
            order("lastUpdated","desc")
        }
        if(params.object_id){
            isExcludeEmp = true
        }
        render modelService.getDataJSON('finance_invoice_detail',extraCondition,false,isExcludeEmp)
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
    def confirmInvoice(Invoice invoice){
        if(invoice == null){
            render baseService.error(params)
            return
        }
        invoice.invoiceMan = session.employee
        invoice.invoiceState = 2
        if(!invoice.invoiceDate){
            invoice.invoiceDate = new Date()
        }
        render baseService.save(params,invoice)
    }

    @Transactional
    def wrong(){
        def invoice = Invoice.get(params.id as Long)
        if (!invoice || invoice.invoiceState != 2 || invoice.wrongKind != 1) {
            render baseService.error(params)
            return
        }
        def wrong = new Invoice()
        wrong.properties = invoice
        wrong.files = null
        wrong.photos = null
        wrong.detail = null
        invoice.files?.each {
            def doc = new Doc(it.properties)
            wrong.addToFiles(doc)
        }
        invoice.photos?.each{
            def doc = new Doc(it.properties)
            wrong.addToPhotos(doc)
        }
        invoice.detail?.each {
            def d = new InvoiceDetail(it.properties)
            d.num = -it.num
            d.subTotal = -it.subTotal
            wrong.addToDetail(d)
        }
        wrong.properties['user'] = session.employee?.user
        wrong.properties['employee'] = session.employee
        wrong.subject = '更正:'+invoice.subject
        wrong.invoiceMoney = -invoice.invoiceMoney
        wrong.taxMoney = -invoice.taxMoney
        if (wrong.hasErrors()) {
            def json = [success: false, errors: errorsToResponse(wrong.errors)] as JSON
            render baseService.validate(params, json)
            return
        }
        baseService.save(params, wrong) //因invoice触发afterInsert事件时，明细尚未保存，不能正确判断开票状态，所以保存一次，然后触发afterUpdate事件进行判断
        wrong.wrongKind = 3
        invoice.wrongKind = 2
        baseService.save(params, invoice)       //触发afterUpdate事件进行判断
        render baseService.save(params, wrong)
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
