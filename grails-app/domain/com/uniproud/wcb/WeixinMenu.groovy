package com.uniproud.wcb
/**
 * 微信菜单管理表
 */
class WeixinMenu {
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工（创建者）
     */
    Employee employee
    /**
     * 用户所关注的微信公众号
     */
    Weixin weixin
    /**
     * 菜单名称
     */
    String name
    /**
     * 父级菜单
     */
    WeixinMenu parentMenu
    /**
     * 事件类型
     * 1、打开网页  view
     * 2、发送消息  click
     */
    int type
    /**
     * 显示顺序
     */
    int idx
    /**
     *相对应的网址
     */
    String url
    /**
     * 是否从素材中选择消息
     * true 是
     * false 否
     * 如果是false 返回的消息直接读取content字段
     */
    Boolean selectionMaterial = false
    /**
     * 推送文本消息
     */
    String content
    /**
     * 消息推送
     * 和素材表进行关联
     */
    Material material
    /**
     * 子菜单
     */
    static hasMany = [children:WeixinMenu]
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
        name maxSize: 16
    }

    static mapping = {
        table('t_weixin_menu')
        content type: 'text'
    }
}
