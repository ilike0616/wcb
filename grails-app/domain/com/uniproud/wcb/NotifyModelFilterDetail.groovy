package com.uniproud.wcb

class NotifyModelFilterDetail {
    static belongsTo = [NotifyModelFilter]
    /**
     * 所属用户
     */
    User user
    /**
     * 创建者
     * 如果是系统管理员创建，此字段可以为空
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 所属条件分组
     */
    NotifyModelFilter notifyModelFilter
    /**
     *  此条件中文描述
     */
    String name
    /**
     * 字段名
     */
    String fieldName
    /**
     * 字段类型
     */
    String dbType
    /**
     * 字段是否关联数据字典
     */
    Boolean isDict
    /**
     * 预期值的取值
     * 1，预期值为实际值
     * 2，预期值为字段名，需要从domain中去取
     */
    Integer expectType
    /**
     * 字段预期值
     */
    String expectValue
    /**
     * 运算符
     */
    String operator
    /**
     * 是否只要修改对应字段条件就满足
     */
    Boolean justEdit
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
        justEdit nullable:true
        name attributes:[text:'描述',must:true]
        fieldName attributes:[text:'字段名',must:true]
        dbType attributes:[text:'字段类型',must:true]
        expectType attributes:[text:'预期值类型',must:true]
        expectValue attributes:[text:'预期值',must:true]
        operator attributes:[text:'运算符',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_notify_model_filter_detail')
    }
}
