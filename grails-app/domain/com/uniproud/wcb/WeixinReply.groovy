package com.uniproud.wcb
/**
 * 微信回复规则
 */
class WeixinReply {
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 微信公众号
     */
    Weixin weixin
    /**
     * 回复规则名称
     */
    String name
    /**
     * 回复规则的关键字
     */
    static hasMany = [keywords:WeixinReplyKeyword]
    /**
     * 关联数据字典
     * 1、text 文本消息，，图片等语音等消息，不做筛选
     * 2、event
     */
    Integer msgType
    /**
     * 关联数据字典
     * 1、subscribe(订阅)、
     * 2、unsubscribe(取消订阅)
     * 3、location(上报地理位置事件)
     */
    Integer eventType
    /**
     * 是否从素材中选择消息
     * true 是
     * false 否
     * 如果是false 返回的消息直接读取content字段
     */
    Boolean selectionMaterial = false
    /**
     * 文本消息
     */
    String content
    
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
     * 删除标志 true  标识数据删除
     */
    Boolean deleteFlag = false

    static constraints = {
        user nullable: false
    }

    static mapping = {
        table('t_weixin_reply')
        content type: 'text'
    }
}
