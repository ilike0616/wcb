package com.uniproud.wcb
/**
 * 市场活动邀请客户列表
 * 市场活动应该支持邀请客户，很多应用场合，邀请客户只需要邀请客户就可以，直接把客户的主联系人邀请过来就可以；
 * 模拟一个应用场景：用户发起一次市场活动，然后让销售人员一个个打电话邀请，那么销售人员就一个个增加对应的邀请客户，
 */
class MarketActivityCustomer  extends ExtField{
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
     * 关联的市场活动
     */
    MarketActivity marketActivity
    /**
     * 客户
     */
    Customer customer
    /**
     * 联系人
     */
    Contact contact
    /**
     * 微信关注者
     */
    WeixinCustomer weixinCustomer
    /**
     * 达到时间
     */
    Date reachDate
    /**
     * 离开时间
     */
    Date departDate
    /**
     * 签到时间
     */
    Date signDate
    /**
     * 是否确认参加
     */
    Boolean confirmFlag
    /**
     * 签到方式
     *  微信签到
     * 1、手工签到
     * 2、微信签到
     */
    Integer signType
    /**
     * 是否已签到
     */
    Boolean signFlag = false
    /**
     * files 关联的文件
     */
    static hasMany = [files:Doc,files1:Doc]
    /**
     * 描述
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
        employee nullable:false
    }

    static mapping = {
        table('t_market_activity_customer')
    }
}
