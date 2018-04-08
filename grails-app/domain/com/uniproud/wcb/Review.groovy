package com.uniproud.wcb

class Review {
    static hasMany = [replys:Comment,files:Doc,photos:Doc]
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 查看者
     */
    Employee owner
    /**
     * 部门
     */
    Dept dept
    /**
     * 关联模块
     */
    Module module
    /**
     * 关联对象的主键ID
     */
    Integer linkId
    /**
     * 评论主题
     */
    String subject
    /**
     * 评论内容
     */
    String content
    /**
     * 是否已读
     */
    Boolean isRead = false
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated

    static constraints = {
        user nullable:false
        employee nullable:false
    }

    static mapping = {
        table('t_review')
    }
}
