package com.uniproud.wcb

class NewsMaterial {
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工（创建者）
     */
    Employee employee
    /**
     * 所属素材
     */
    Material material
    /**
     * 标题
     */
    String title
    /**
     * 关联的素材Id
     */
    Material thumbMedia
    /**
     * 上传图片的URL
     */
    String url
    /**
     * 素材文件 地址
     */
    Doc doc
    /**
     * 作者
     */
    String author
    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
     */
    String digest
    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    Boolean show_cover_pic
    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
     */
    String content
    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    String content_source_url
    /**
     * 排列顺序
     */
    Integer idx
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
        table("t_weixin_news_material")
    }
}
