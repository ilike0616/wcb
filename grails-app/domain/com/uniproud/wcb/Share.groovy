package com.uniproud.wcb

class Share {
    /**
     * 所属公司
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
     * 分享摘要
     */
    String subject
    /**
     * 分享内容
     */
    String content
    /**
     * 1：自填url；2：html内容
     */
    Integer urlType
    /**
     * 分享的Url
     */
    String url
    /**
     * html内容
     */
    String htmlContent
    /**
     * 图片
     */
    Doc pic
    /**
     * 阅读次数
     */
    Integer readTimes = 0
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static hasMany = [shareDetails:ShareDetail]

    static constraints = {
        user nullable:false
        employee nullable:false
        htmlContent  size: 0..65535
    }
	
    static mapping = {
        table('t_share')
    }
}
