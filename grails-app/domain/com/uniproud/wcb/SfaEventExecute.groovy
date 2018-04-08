package com.uniproud.wcb

class SfaEventExecute {

    static hasMany = [employees: Employee]
    /**
     * 企业
     */
    User user
    /**
     * 部门
     */
    Dept dept
    /**
     *
     */
    SfaExecute sfaExecute
    /**
     * 对应事件
     */
    SfaEvent sfaEvent
    /**
     * 执行状态：1-待执行；2-已执行；3-已禁止
     */
    Integer state = 1
    /**
     * 通知的客户
     */
    Customer customer
    /**
     * 执行时间
     */
    Date executeDate
    /**
     * 是否发送提醒
     */
    Boolean isNotify = false
    /**
     * 提醒结果
     */
    Integer notifyResult
    /**
     * 提醒消息主题
     */
    String notifySubject
    /**
     * 提醒消息内容
     */
    String notifyContent
    /**
     * 是否发送短信
     */
    Boolean isSms = false
    /**
     * 短信发送结果
     */
    Integer smsResult
    /**
     * 短信主题
     */
    String smsSubject
    /**
     * 短信内容
     */
    String smsContent
    /**
     * 是否发送邮件
     */
    Boolean isEmail = false
    /**
     * 邮件发送结果
     */
    Integer emailResult
    /**
     * 邮件主题
     */
    String emailSubject
    /**
     * 邮件内容
     */
    String emailContent
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated


    static constraints = {
        user nullable:false
    }

    static mapping = {
        table('t_sfa_event_execute')
    }
}
