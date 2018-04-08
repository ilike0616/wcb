package com.uniproud.wcb
/**
 * 微信消息接收者
 */
class SendMsgWeixinCustomer {
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工（创建者）
     */
    Employee employee
    /**
     * 所属微信公众号
     */
    Weixin weixin
    /**
     * 关联消息
     */
    WeixinSendMsg sendMsg
    /**
     * 接收者微信号
     */
    WeixinCustomer weixinCustomer
    /**
     * 接收者关联客户
     */
    Customer customer
    /**
     * 接收者联系人
     */
    Contact contact
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    static constraints = {
        user nullable: false
    }

    static mapping = {
        table('t_weixin_send_msg_customer')
    }
}
