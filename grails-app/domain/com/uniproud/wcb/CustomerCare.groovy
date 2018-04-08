package com.uniproud.wcb
/**
 * 客户关怀
 */
class CustomerCare  extends ExtField{
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工(创建者)
     */
    Employee employee
    /**
     * 所有者
     */
    Employee owner
    /**
     * 客户
     */
    Customer customer
    /**
     * 联系人
     */
    Contact contact
    /**
     * 关怀内容
     */
    String content
    /**
     * 关怀方式
     * 关联数据字典
     */
    Integer kind
    /**
     * 关怀反馈
     */
    String feedBack

    static hasMany = [files:Doc,files1:Doc]
    /**
     * 备注
     */
    String remark
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
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
        table('t_customer_care')
        contact type: 'text'
        feedBack type: 'text'
    }
}
