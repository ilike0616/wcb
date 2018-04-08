package com.uniproud.wcb
/**
 * 销售目标
 */
class SaleAim extends ExtField{
    static belongsTo = [User,Employee]
    /**
     * 企业
     */
    User user

    /**
     * 创建者
     */
    Employee employee

    /**
     * 所有者
     */
    Employee owner
    /**
     * 部门
     */
    Dept dept

    /**
     * 目标年月
     */
    String aimYearMonth

    /**
     * 分类,表示是员工还是部门目标
     * 1：员工；2：部门
     */
    Integer aimType
    /**
     * 客户数
     */
    Integer aimCustomer

    /**
     * 客户跟进
     */
    Integer aimCustomerFollow

    /**
     * 销售额
     */
    Double aimOrderMoney

    /**
     * 销售毛利
     */
    Double aimOrderProfit

    /**
     * 回款额
     */
    Double aimPayMoney

    /**
     * 充值时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        user nullable: false
        employee nullable: false
        owner nullable: false
        dept nullable: false
    }

    static mapping = {
        table('t_sale_aim')
    }
}
