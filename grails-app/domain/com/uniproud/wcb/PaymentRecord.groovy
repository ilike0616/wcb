package com.uniproud.wcb
/**
 * 扣费纪录表
 */
class PaymentRecord {
    /**
     * 扣款费用
     */
    User user
    /**
     * 部门
     */
    Dept dept
    /**
     * 扣款费用
     */
    BigDecimal amountFee
    /**
     * 扣费前 账号余额
     */
    BigDecimal preBalance
    /**
     * 扣费后 账号余额
     */
    BigDecimal postBalance
    /**
     * 使用人数
     */
    Long syNum
    /**
     * 扣费人数
     */
    Long kfNum
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static V = [
            list: [viewId: 'PaymentRecordList', title: '扣费记录', clientType: 'pc', viewType: 'list',
                   ext   : [],//扩展参数值最终进入ViewExtend
                   opt   : ['payment_record_view', 'payment_record_export']//视图对应的操作
            ],
            view: [viewId: 'PaymentRecordView', title: '查看扣费记录', clientType: 'pc', viewType: 'form'],
    ]

    static list = ['amountFee', 'preBalance', 'postBalance', 'syNum', 'kfNum', 'dateCreated']
    static view = ['amountFee', 'preBalance', 'postBalance', 'syNum', 'kfNum', 'dateCreated']

    static pub = ['user']

    static allfields = list + view + pub
    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [:]

    static constraints = {
        user nullable: false
        amountFee attributes: [text: '扣费金额', must: true]
        preBalance attributes: [text: '扣前余额', must: true]
        postBalance attributes: [text: '扣后余额', must: true]
        syNum attributes: [text: '使用人数', must: true]
        kfNum attributes: [text: '扣费人数', must: true]
        dateCreated attributes: [text: '创建时间', must: true]
        lastUpdated attributes: [text: '修改时间', must: true]
    }

    static mapping = {
        table('t_payment_record')
    }
}
