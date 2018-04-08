package com.uniproud.wcb
/**
 * 客户投诉
 * 1客户因为购买的产品或服务发生问题，可能会发起投诉，
 * 所以，投诉肯定是由客户发起的，投诉需要关联到产品或订单或维修单或安装单或保养单；
 * （目前暂时有产品、订单和安装单）其他的暂时种类暂不考虑
 */
class CustomerComplaints  extends ExtField{
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工(创建者)
     */
    Employee employee
    /**
     * 客户
     */
    Customer customer
    /**
     * 联系人
     */
    Contact contact
    /**
     * 投诉主题
     */
    String subject
    /**
     * 投诉时间
     */
    Date complaintDate
    /**
     * 投诉内容
     */
    String content
    /**
     * 投诉种类
     *  关联数据字典
     */
    Integer kind
    /**
     * 处理结果
     *  关联数据字典
     */
    Integer processResult
    /**
     * 投诉类型
     * 1、产品
     * 2、订单
     * 3、安装单
     * 页面中须根据此字段选择的类型，动态更改页面展示的内容
     */
    Integer type
    /**
     * 产品
     */
    Product product
    /**
     * 订单
     */
    ContractOrder contractOrder
    /**
     * 安装单
     */
    InstallOrder installOrder
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 删除标志 true  标识数据删除
     */
    Boolean deleteFlag = false
    static constraints = {
        user nullable:false
    }

    static mapping = {
        table('t_customer_complaints')
        contact type: 'text'
    }
}
