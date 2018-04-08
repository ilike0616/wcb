package com.uniproud.wcb

class Sfa {
    static hasMany = [detail:SfaEvent]
    /**
     * 所属用户
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
     * SFA方案名称
     */
    String name
    /**
     * 关联的具体模块
     */
    Module module

    /**
     * 是否自动
     */
    Boolean auto = false
    /**
     * 状态
     * 1-设计中；2-已开始
     */
    Integer state = 1
    /**
     * 备注
     */
    String remark
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
        employee nullable:true
        module nullable:false
    }

    static mapping = {
        table('t_sfa')
    }
}
