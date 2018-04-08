package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class InvoiceService {

    def baseService

    /**
     * 维护订单的开票状态和开票金额
     * @param invoice
     * @return
     */
    def updateContractOrderInvoiceInfo(Invoice invoice) {
        def contractOrder = invoice.contractOrder
        if(contractOrder) {
            def money = 0
            Invoice.createCriteria().list {
                eq("contractOrder", contractOrder)
                eq("invoiceState",2)
                eq("deleteFlag", false)
            }?.each {
                if((!it.audit || it.audit?.auditState != 4) && it.invoiceMoney) {
                    money += it.invoiceMoney
                }
            }
            invoice.detail?.each{
                updateContractOrderDetailInvoiceNum(it)
            }
            def flag = true     //是否所有明细都已开票
            contractOrder?.detail?.each {
                if(it.num != it.invoiceNum){
                    flag = false
                }
            }
            if(flag){
                contractOrder.invoiceMoney = contractOrder.discountMoney
                contractOrder.invoiceState = 3
                contractOrder.save()
            }else{
                contractOrder.invoiceMoney = money
                if (money != 0) {
                    contractOrder.invoiceState = 2
                } else {
                    contractOrder.invoiceState = 1
                }
                contractOrder.save()
            }
        }
    }
    /**
     * 维护订单明细的已开票数量字段
     * @param detail
     * @return
     */
    def updateContractOrderDetailInvoiceNum(InvoiceDetail detail){
        def od = detail.contractOrderDetail
        def num = 0
        InvoiceDetail.createCriteria().list {
            eq("contractOrderDetail", od)
            invoice{
                eq("invoiceState",2)    //开票记录已开票
            }
            eq("deleteFlag", false)
        }?.each {
            if((!it.invoice.audit || it.invoice.audit != 4) && it.num){
                num += it.num
            }
        }
        od.invoiceNum = num
        od.save()
    }

    /**
     * 新增开票
     * @param params
     * @param invoice
     * @return
     */
    def insert(params,Invoice invoice){
        def user = WebUtilTools.session?.employee?.user
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        invoice.properties['user'] = user
        invoice.properties['employee']= employee
        if (invoice.hasErrors()) {
            def json = [success:false,errors: errorsToResponse(invoice.errors)] as JSON
            return baseService.validate(params,json)
        }
        def result = baseService.save(params,invoice)
        if(!JSON.parse(result.toString()).success){
            return result
        }
        if(params.detail != null) {
            def detail = JSON.parse(params.detail)
            detail.each {
                InvoiceDetail od = new InvoiceDetail()
                od.properties = it
                if(params.contractOrder){ // 订单的开票
                    od.properties["contractOrder"] = invoice.contractOrder
                    od.properties["contractOrderDetail"] = it.id
                }
                od.properties['user'] = user
                od.properties['employee']= employee
                if(it['product.id']){
                    od.product = Product.get(it['product.id']?.toLong())
                }else if(it.product?.id){
                    od.product = Product.get(it.product?.id?.toLong())
                }
                if (od.hasErrors()) {
                    println od.errors
                    def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                    return baseService.validate(params,json)
                }
                od.save()
                invoice.addToDetail(od)
            }
            result = baseService.save(params,invoice)
            if(!JSON.parse(result.toString()).success){
                return result
            }
        }
        if (invoice.auditor) { //创建审核任务
            def auditor = invoice.auditor
            def audit = new Audit(user: user, employee: employee, auditor: auditor, subject: invoice.subject, type: 8, qz: 'invoice', auditState: 1)
            audit.invoice = invoice
            if (!audit.validate()) {
                log.info audit.errors
                def json = [success: false, errors: errorsToResponse(audit.errors)] as JSON
                return baseService.validate(params, json)
            }
            baseService.save(params, audit, 'work_audit')
            def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '').save()
            audit.setNowAuditOpinion(nowAuditOpinion)    //待审意见记录
            baseService.save(params, audit, 'work_audit')
            invoice.audit = audit
        }
        return baseService.save(params,invoice)
    }

    def update(params){
        def user = WebUtilTools.session?.employee?.user
        def employee = Employee.get(WebUtilTools.session?.employee?.id)
        Invoice invoice = Invoice.get(params.id as Long)
        if(params.detail){
            def detail = JSON.parse(params.detail)
            detail.each {
                InvoiceDetail od = InvoiceDetail.get(it.id)
                if(od) {
                    od.properties = it
                    od.properties['user'] = user
                    od.properties['employee'] = employee
                    if (it['product.id']) {
                        od.product = Product.get(it['product.id']?.toLong())
                    } else if (it.product?.id) {
                        od.product = Product.get(it.product?.id?.toLong())
                    }
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success: false, errors: errorsToResponse(od.errors)] as JSON
                        return baseService.validate(params, json)
                    }
                    baseService.save(params, od, 'invoice_detail')
                }
            }
        }
        if(params.dels){
            def ids = JSON.parse(params.dels)
            log.info "------------------------------dels------------------------${ids}"
            if(ids.size()>0)
                InvoiceDetail.executeUpdate("update InvoiceDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        }
        if (params.auditor && invoice.auditor?.id != (params.auditor as Long)) {  //修改了审核人
            def audit = invoice.audit
            def auditor = Employee.get(params.auditor as Long)
            if (auditor == null) {
                return baseService.error(params)
            }
            if(!audit){//之前未指定审核人
                audit = new Audit(user: user, employee: employee, auditor: auditor, subject: invoice.subject, type: 8, qz: 'invoice', invoice: invoice, auditState: 1)
                baseService.save(params, audit, 'work_audit')
                def nowAuditOpinion = new AuditOpinion(user: user, employee: employee, auditor: auditor, audit: audit, state: 1/*未审核*/, content: '')
                nowAuditOpinion.save flush: true
                audit.nowAuditOpinion = nowAuditOpinion
            }else{
                audit.setAuditor(auditor)
                def nowAuditOpinion = audit.nowAuditOpinion
                nowAuditOpinion.setAuditor(auditor)
                nowAuditOpinion.save flush: true
            }
            baseService.save(params, audit, 'work_audit')
        }
        params.detail = null
        invoice.properties = params
        return baseService.save(params,invoice)
    }

}
