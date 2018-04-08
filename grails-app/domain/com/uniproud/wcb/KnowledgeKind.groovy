package com.uniproud.wcb
/**
 * 服务知识库分类表
 * 此数据结构为树状结构
 */
class KnowledgeKind  extends ExtField{
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
     * 父节点，如果无父节点则为null
     */
    KnowledgeKind parentKind
    /**
     * 分类类型，为以后扩展用
     */
    Integer kindType
    /**
     * 分类名称
     */
    String name
    /**
     * children 产品分类的子元素
     */
    static hasMany = [children : KnowledgeKind,files:Doc,files1:Doc]
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false

    static constraints = {
        user nullable:false
    }
    static mapping = {
        table('t_knowledge_kind')
    }
}
