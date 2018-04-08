package com.uniproud.wcb
/**
 * 用于记录数据模型和视图是否发生过变化
 * 每次发生变化ver 自动加一
 */
class UserModuleVersion {
    /**
     * 所属企业
     */
    User user
    /**
     * 所属模块
     */
    Module module
    /**
     * 部门
     */
    Dept dept
    /**
     * 版本号
     */
    Long ver = 0
    /**
     *创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        user nullable: false
        module nullable: false
    }

    static mapping = {
        table('t_user_module_version')
    }
}
