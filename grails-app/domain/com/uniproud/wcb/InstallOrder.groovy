package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class InstallOrder extends ExtField{

    static hasMany = [detail:InstallOrderDetail,allocates:InstallAllocateDetail,photos:OutsiteRecord,files:Doc,files1:Doc]

    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 所有者
     */
    Employee owner
    /**
     *  关联的销售订单种子，因为有可能需要从销售那边委派售后安装单过来
     */
    ContractOrder contractOrder
    /**
     * 厂商、商城即单子提供渠道方的SEED
     */
    String providerSeed
    /**
     * 主题
     */
    String subject
    /**
     * 订单号
     */
    String orderNo
    /**
     * 订单类型，扩展用
     */
    Integer orderType
    /**
     * 客户
     */
    Customer customer
    /**
     * 安装状态：1-待派单；2-处理中；3-已完成
     */
    Integer installState = 1
    /**
     * 因为有可能和客户地址不一致，所以增加此字段
     */
    String installAddress
    /**
     * 公开金额
     */
    Double publicMoney = 0
    /**
     * 实际金额
     */
    Double discountMoney = 0
    /**
     * 折扣率（％）
     */
    Double discountRate = 100
    /**
     * 付款方式
     * 1.现金
     * 2.支票
     * 3.转账
     * 4.托收
     */
    Integer payMethod
    /**
     * 收款状态:1-未收款；2-部分收款；3-已收款
     */
    Integer paidState = 1
    /**
     * 收款金额
     */
    Double paidMoney = 0
    /**
     * 待收款金额
     */
    Double payingMoney = 0
    /**
     * 开票状态：1-未开票；2-部分开票；3-已开票
     * @return
     */
    Integer invoiceState = 1
    /**
     * 已开票金额
     */
    Double invoiceMoney = 0
    /**
     *  关联的审核任务
     */
    Audit audit
    /**
     * 当前审核人
     */
    Employee auditor
    /**
     * 预约时间
     */
    Date appointmentDate
    /**
     * 安装时间
     */
    Date installDate
    /**
     * 派单模式
     * 1、指派模式
     * 2、预派模式
     * 3、抢接模式
     */
    Integer allocateKind
    /**
     * 派单人
     */
    Employee allocateMan
    /**
     * 派单时间
     */
    Date allocateDate
    /**
     * 接单人
     */
    Employee receivedMan
    /**
     * 接单时间
     */
    Date receivedDate
    /**
     * 做单人，缺省情况下做单人=接单人，允许转单
     */
    Employee executeMan
    /**
     * 回访人
     */
    Employee revisitMan
    /**
     * 回访时间
     */
    Date revisitDate
    /**
     * 现成签到
     */
    OutsiteRecord signon
    /**
     * 现成签退
     */
    OutsiteRecord signout
    /**
     * 签到时间
     */
    Date signonDate
    /**
     * 签退时间
     */
    Date signoffDate
    /**
     * 描述
     */
    String description
    /**
     * 备注信息
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     *创建时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date dateCreated
    /**
     *修改时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date lastUpdated

    static constraints = {
        user nullable: false
    }

    static mapping = {
        table('t_install_order')
    }
}
