package com.uniproud.wcb

class AddBalance {

//    /**
//     * 充值类型
//     * 1.为代理商充值
//     * 2.为企业充值
//     */
//    Integer type
    /**
     * 充值类型，由系统自动判定，不能让充值者选择
     * 1.管理员为代理商充值
     * 2.管理员为用户充值
     * 3.代理商为代理商充值
     * 4.代理商为企业充值
     * 5.企业在线充值
     */
    Integer kind
    /**
     * 部门
     */
    Dept dept
    /**
     * 操作管理员
     * 如果充值方式（kind）为1，此字段不能为空
     */
    Administrator createAdmin
    /**
     * 操作管理员
     * 如果充值方式（kind）为2或者3，此字段不能为空
     */
    Agent createAgent
    /**
     * 被充值账号
     * 如果充值方式（kind）为1或者2，此字段不能为空
     */
    Agent agent
    /**
     * 被充值账号
     * 如果充值方式（kind）为3或者4，此字段不能为空
     */
    User user
    /**
     * 充值前 账号余额
     */
    BigDecimal preBalance = 0
    /**
     * 充值后账号余额
     */
    BigDecimal postBalance = 0
    /**
     * 充值金额
     */
    BigDecimal balance = 0
    /**
     * 实际充值金额
     */
    BigDecimal realAddBalance = 0
    /**
     * 充值类型
     * 1：充值
     * 2：退费
     */
    Integer type
    /**
     * 备注信息
     */
    String remark
    /**
     * 充值时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        kind nullable: false
        balance nullable: false
    }

    static mapping = {
        table('t_add_balance')
    }
}
