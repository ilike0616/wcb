package com.uniproud.wcb

import grails.transaction.Transactional

/**
 * 销售开票
 */
class Invoice extends ExtField{

    static hasMany = [files:Doc,photos:Doc,detail:InvoiceDetail]
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 订单
     */
    ContractOrder contractOrder
    /**
     * 客户
     */
    Customer customer
    /**
     * 客户联系人
     */
    Contact contact
    /**
     * 经手人
     */
    Employee invoiceMan
    /**
     * 所有者
     */
    Employee owner
    /**
     * 和审核任务表的关联
     * audit.auditOpinion 可查看审核意见列表
     * audit.nowAuditOpinion 当前待审卡点
     * audit.auditState 审核进度  1.待审核  2.审核中3.审核通过  4.审核未通过
     */
    Audit audit
    /**
     * 当前审核人
     */
    Employee auditor
    /**
     * 开票主题
     */
    String subject
    /**
     * 开票流水号
     */
    String invoiceSno
    /**
     * 抬头（缺省填写订单中关联的客户名，可以修改）
     */
    String head
    /**
     * 开票日期
     */
    Date invoiceDate
    /**
     * 开票种类
     */
    Integer invoiceKind
    /**
     * 开票额
     */
    Double invoiceMoney
    /**
     * 出票状态：1-待出票；2-已出票
     */
    Integer invoiceState = 1
    /**
     * 税率
     */
    Double taxRate
    /**
     * 应缴税额
     */
    Double taxMoney
    /**
     * 备注
     */
    String remark
    /**
     * 红字更正:1-未更正；2-红字源纪录；3-红字更正记录
     */
    Integer wrongKind = 1
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
        invoiceService.updateContractOrderInvoiceInfo(this)
    }
    @Transactional
    def afterUpdate(){
        invoiceService.updateContractOrderInvoiceInfo(this)
    }
    @Transactional
    def afterDelete(){
        invoiceService.updateContractOrderInvoiceInfo(this)
    }

    static constraints = {
        user nullable:false
        employee nullable:false
        invoiceMoney validator:{val,obj,errors ->
            if(obj.contractOrder){
                def money = 0
                Invoice.createCriteria().list {
                    ne("id", obj.id)
                    eq("contractOrder", obj.contractOrder)
                    eq("deleteFlag",false)
                }?.each {
                    if(!it.audit || it.audit?.auditState != 4) {
                        money += it.invoiceMoney
                    }
                }
                log.info "$val - (${obj.contractOrder.discountMoney} - $money >= 1 :"+(val - (obj.contractOrder.discountMoney - money) >= 1)
                if(obj.contractOrder.discountMoney > 0) {
                    if (val - (obj.contractOrder.discountMoney - money) >= 1) {
                        errors.rejectValue('invoiceMoney', '开票金额大于订单的剩余可开票金额！')
                    }
                }else{
                    if(obj.contractOrder.discountMoney - money - val >= 1){
                        errors.rejectValue('invoiceMoney', '开票金额大于订单的剩余可开票金额！')
                    }
                }
            }
        }
    }

    static mapping = {
        table("t_invoice")
    }
}
