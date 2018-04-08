package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class Schedule extends ExtField{

    /**
     * 所属用户
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
     * 客户
     */
    Customer customer
    /**
     * 客户跟进
     */
    CustomerFollow customerFollow
    /**
     * 商机
     */
    SaleChance saleChance
    /**
     * 商机跟进
     */
    SaleChanceFollow saleChanceFollow
    /**
     * 主题
     */
    String subject
    /**
     * 计划开始时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date planStartDate
    /**
     * 计划结束时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date planEndDate
    /**
    * 任务状态
    */
    Integer taskState
    /**
    * 任务描述
    */
    String taskDesc
    /**
    * 关联类型
    */
    Integer relatedType
    /**
    * 任务总结
    */
    String taskSummary
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

    static hasMany = [files:Doc,files1:Doc]

    static constraints = {
        user nullable:false
        employee nullable:false
    }

    static mapping = {
        table('t_schedule')
    }
}

