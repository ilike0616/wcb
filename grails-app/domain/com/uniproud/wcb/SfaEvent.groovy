package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class SfaEvent {
    static hasMany = [acceptors:Employee]
    /**
     * 所属用户
     */
    User user
    /**
     * 创建者
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 所属方案
     */
    Sfa sfa
    /**
     * 事件名称
     */
    String name
    /**
     * 流程排序
     */
    Integer index
    /**
     * 执行日期类型
     * 1-绝对日期
     * 2-相对日期
     * 3-循环
     */
    Integer dateType
    /**
     * 绝对日期类型的开始日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date startDate
    /**
     * 绝对日期类型的结束日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date endDate
    /**
     * 相对日期：基准日期字段
     */
    UserField dateField
    /**
     * 相对日期：1-提前；2-延后
     */
    Integer beforeEnd
    /**
     * 时间差周期:1-日；2-月；3-年
     */
    Integer diffPeriod = 1
    /**
     * 相对日期：延后几天/月开始
     */
    Integer difftime
    /**
     * 相对日期：持续天数
     */
    Integer lastingDays
    /**
     * 循环执行：基准日期字段
     */
    UserField dateFieldCycle
    /**
     * 循环执行：时间差周期:1-日；2-月；3-年
     */
    Integer diffPeriodCycle = 1
    /**
     * 循环执行：1-提前；2-延后
     */
    Integer beforeEndCycle
    /**
     * 循环执行：延后几天/月开始
     */
    Integer difftimeCycle
    /**
     * 循环执行：持续天数
     */
    Integer lastingDaysCycle
    /**
     * 间隔周期:1-日；2-月；3-年
     */
    Integer intervalPeriod = 1
    /**
     * 间隔日/月/年
     */
    Integer interval
    /**
     * 循环次数
     */
    Integer cycleTimes
    /**
     * 当日的提醒时间（时分）
     */
    Integer timeHour
    Integer timeMinute
    /**
     * 是否阴历
     */
    Boolean isLunar = false
    /**
     * 通知对象类型：1-所有者；2-客户；3-相关员工
     */
    Integer receiverType
    /**
     * 是否员工提醒
     */
    Boolean isNotify
    /**
     * 是否发送短信
     */
    Boolean isSms
    /**
     * 是否发送邮件
     */
    Boolean isEmail
    /**
     * 提醒消息主题模版
     */
    String notifySubjectTemplate
    /**
     * 提醒消息内容模版
     */
    String notifyContentTemplate
    /**
     * 短信主题模板
     */
    String smsSubjcetTemplate
    /**
     * 短信内容模板
     */
    String smsContentTemplate
    /**
     * 邮件主题模板
     */
    String emailSubjectTemplate
    /**
     * 邮件内容模板
     */
    String emailContentTemplate
    /**
     * 备注
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated


    static constraints = {
        user nullable:false
        employee nullable:true
        sfa nullable:false
    }

    static mapping = {
        table('t_sfa_event')
    }
}
