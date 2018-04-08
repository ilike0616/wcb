package com.uniproud.wcb
/**
 * 微信关注者列表
 */
class WeixinCustomer {
    /**
     * 所属用户
    */
    User user
    /**
     * 用户所关注的微信公众号
     */
    Weixin weixin
    /**
     * 关联的客户信息
     */
    Customer customer
    /**
     * 关联的联系人
     */
    Contact contact
    /**
     * 部门
     */
    Dept dept
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    Integer subscribe
    /**
     * 用户的标识，对当前公众号唯一
     */
    String openid
    /**
     * 用户的昵称
     */
    String nickname
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    Integer sex
    /**
     * 用户所在城市
     */
    String city
    /**
     * 用户所在国家
     */
    String country
    /**
     * 用户所在省份
     */
    String province
    /**
     * 用户的语言，简体中文为zh_CN
     */
    String language
    /**
     * 用户头像，最后一个数值代表正方形头像大小
     * （有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    String headimgurl
    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    Date subscribeTime
    /**
     * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
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
        user nullable: false
    }

    static mapping = {
        table('t_weixin_customer')
    }
}
