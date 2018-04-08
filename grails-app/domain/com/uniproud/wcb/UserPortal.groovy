package com.uniproud.wcb
/**
 * 企业所使用的portal
 */
class UserPortal {
    /**
     * 所属用户
     */
    User user
    /**
     * 关联portal
     */
    Portal portal
    /**
     * 部门
     */
    Dept dept
    /**
     * 标题
     */
    String title
    /**
     * 显示高度
     */
    Integer height
    /**
     * 显示顺序
     */
    Integer idx
    /**
     * 默认是否显示
     */
    Boolean isShow = true
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
        portal nullable:false
    }

    static mapping = {
        table('t_user_portal')
    }
}
