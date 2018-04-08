package com.uniproud.wcb
/**
 * 素材管理
 */
class Material {
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
     * 视频素材的标题
     */
    String title
    /**
     * 素材类型
     * 1、图文消息 news
     * 2、图片 image
     * 3、语音 voice
     * 4、视频 video
     * 5、文本消息 text
     */
    String type
    /**
     * 文本消息的内容
     */
    String content
    /**
     * 视频素材的描述
     */
    String introduction
    /**
     * 新增的图片素材的图片URL 微信公众平台的下载地址
     */
    String url
    /**
     * 素材文件 地址
     */
    Doc doc
    /**
     * 微信的mediaId
     */
    String mediaId
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
        title maxSize: 64
        content maxSize: 600
    }

    static mapping = {
        table("t_weixin_material")
    }
}
