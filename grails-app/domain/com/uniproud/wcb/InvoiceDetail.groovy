package com.uniproud.wcb

import grails.transaction.Transactional

/**
 * 开票明细
 */
class InvoiceDetail extends ExtField{
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 所属开票
     */
    Invoice invoice
    /**
     * 关联订单
     */
    ContractOrder contractOrder
    /**
     * 关联的订单明细
     */
    ContractOrderDetail contractOrderDetail
    /**
     * 关联产品
     */
    Product product
    /**
     * 开票单位
     */
    Integer unit
    /**
     * 开票数量
     */
    Double num
    /**
     * 销售价
     */
    Double price
    /**
     * 小计
     */
    Double subTotal
    /**
     * 备注
     */
    String remark
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false

    def invoiceService
    @Transactional
    def afterInsert(){
        invoiceService.updateContractOrderDetailInvoiceNum(this)
    }
    @Transactional
    def afterUpdate(){
        invoiceService.updateContractOrderDetailInvoiceNum(this)
    }
    @Transactional
    def afterDelete(){
        invoiceService.updateContractOrderDetailInvoiceNum(this)
    }

    static constraints = {
        user nullable:false
        employee nullable:false
        invoice nullable:false
        product nullable:false
    }

    static mapping = {
        table("t_invoice_detail")
    }
}
