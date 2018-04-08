package com.uniproud.wcb
/**
 * 系统消息模型表的，筛选条件组表
 * 消息模型和此模型的关系是一对一关系
 * 筛选条件，应是树结构，支持多级条件
 * 条件的之间的关系，可以是and或者or
 */
class NotifyModelFilter {
    /**
     * 与消息模型表进行关联
     * 控制权交有 NotifyModel
     */
    static belongsTo = [NotifyModel]
    /**
     * 所属用户
     */
    User user
    /**
     * 部门
     */
    Dept dept
    /**
     * 创建者
     * 如果是系统管理员创建，此字段可以为空
     */
    Employee employee
    /**
     * 所属的条件模型
     */
    NotifyModel notifyModel
    /**
     * 关联的具体模块
     */
    Module module
    /**
     * 次条件组的整体中文描述
     */
    String name
    /**
     * 子条件的关系：and或者or
     * 1.and
     * 2.or
     */
    Integer childRelation
    /**
     * 父节点
     */
    NotifyModelFilter parentNotifyModelFilter
    /**
     * 自关联
     */
    static hasMany = [children:NotifyModelFilter,filterDetail:NotifyModelFilterDetail]
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
        employee nullable:true
        notifyModel nullable:false
        name attributes:[text:'描述',must:true]
        childRelation attributes:[text:'子条件关系',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_notify_model_filter')
    }
}
