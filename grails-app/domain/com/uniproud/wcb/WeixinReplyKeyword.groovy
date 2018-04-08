package com.uniproud.wcb
/**
 * 微信回复关键字
 */
class WeixinReplyKeyword {
    /**
     * 关联的回复规则
     */
    WeixinReply weixinReply
    /**
     * 关键字
     */
    String keyword
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        weixinReply nullable: false
        keyword nullable: false
    }

    static mapping = {
        table('t_weixin_reply_keyword')
    }
}
