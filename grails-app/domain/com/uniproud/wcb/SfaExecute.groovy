package com.uniproud.wcb

class SfaExecute {
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
     * 方案
     */
    Sfa sfa
    /**
     * 关联模块
     */
    Module module
    /**
     * 关联ID
     */
    Long linkId
    /**
     * 1-启用;2-停用
     */
    Integer state = 1
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
    }

    static mapping = {
        table('t_sfa_execute')
    }
}
