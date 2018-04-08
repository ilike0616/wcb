package com.uniproud.wcb
/**
 * 微信群发消息表
 */
class WeixinSendMsg {
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
//    /**
//     * 群发接收者
//     */
//    static hasMany = [weixinCustomers:WeixinCustomer]
    /**
     * 群发的消息Id
     * 微信平台返回
     */
    String msgId
    /**
     * 发送状态
     */
    String msgStatus
    /**
     * group_id下粉丝数；
     * 或者openid_list中的粉丝数
     */
    Long totalCount
    /**
     * 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount
     */
    Long filterCount
    /**
     * 发送成功的粉丝数
     */
    Long sentCount
    /**
     * 发送失败的粉丝数
     */
    Long errorCount
    /**
     * 关联的素材
     */
    Material material
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 是否是全部发送
     */
    Boolean isAll

    static constraints = {
        user nullable: false
    }

    static mapping = {
        table("t_weixin_send_msg")
    }
}
