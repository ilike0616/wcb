package com.uniproud.wcb
/**
 * 员工设置的
 */
class EmployeePortal {
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
     * 启用的portal
     */
    UserPortal userPortal
    /**
     * 显示顺序
     */
    Integer idx
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
        employee nullable: false
        userPortal nullable:false
    }

    static mapping = {
        table('t_employee_portal')
    }
}
