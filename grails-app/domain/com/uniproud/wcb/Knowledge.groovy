package com.uniproud.wcb
/**
 * 知识库表
 */
class Knowledge extends ExtField{
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 知识库分类
     */
    KnowledgeKind knowledgeKind
    /**
     * 主题
     */
    String subject
    /**
     * 问题描述
     */
    String description
    /**
     * 关键词
     */
    String keyword
    /**
     * 问题解答
     */
    String answer
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
    }

    static mapping = {
        table('t_knowledge')
        description type:'text'
        answer type: 'text'
    }
}
