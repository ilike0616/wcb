package com.uniproud.wcb

/**
 * Created by like on 2015-05-08.
 */
class SmsRecord {
    static belongsTo = [User, Employee]
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
     * 短信类型
     * 1，注册验证码
     * 2，系统提醒消息
     */
    Integer kind
    /**
     * 请求短息的IP
     */
    String ip
    /**
     * 接收短信的手机号
     */
    String mobile
    /**
     * 短信内容
     */
    String content
    /**
     * 发送结果
     */
    Integer result
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        user nullable:true
        employee nullable:true
        kind attributes:[text:'短信类型',must:true]
        ip attributes:[text:'IP',must:true]
        mobile attributes:[text:'手机号',must:true]
        content  attributes:[text:'短信类型',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_sms_record')
    }
}
