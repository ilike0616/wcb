package com.uniproud.wcb
/**
 * 客户回访
 *  客户回访一般是针对客户接受了用户的服务或者下了订单后，需要给客户做回访
 *  所以，回访一般是由某次订购或服务发生后，客服主动给客户做的回访活动
 *  目的是为了获取客户对服务或订购产品的满意度
 */
class CustomerRevisit extends ExtField{
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
     * 关联客户
     */
    Customer customer
    /**
     * 关联联系人
     */
    Contact contact
    /**
     * 回访主题
     */
    String subject
    /**
     * 业务类型
     * 关联数据字典 ，，此数据字典需要设置为系统级数据字典，不允许修改
     * 1、产品
     * 2、订单
     *  2015年12月16日09:43:30 本次暂时考虑 产品和订单两种业务，剩下几种以后需要补充
     * 3、维修单
     * 4、安装单
     * 5、保养合同
     */
    Integer revisitType
    /**
     * 产品
     * 如果revisitType=1
     * product 就必须要填写
     */
    Product product
    /**
     * 订单
     * 如果revisitType=2
     * contractOrder 就必须要填写
     */
    ContractOrder contractOrder
    /**
     * 回访时间
     * 默认值为当前时间
     */
    Date revisitDate
    /**
     * 回访方法
     * 关联数据字典
     */
    Integer revisitMethod
    /**
     * 回访内容
     */
    String revisitContent
    /**
     * 客户反馈
     */
    String revisitFeedBack
    /**
     * 客户满意度
     * 关联数据字典
     * 1、一般
     * 2、满意
     * 3、很满意
     * 4、不满意
     */
    Integer customerSatisFaction
    /**
     * 备注信息
     */
    String remark
    /**
     * files 关联的文件
     */
    static hasMany = [files:Doc,files1:Doc]
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 删除标志
     */
    Boolean deleteFlag = false
    static constraints = {
        user nullable:false
        employee nullable:false
    }

    static mapping = {
        table('t_customer_revisit')
    }
}
