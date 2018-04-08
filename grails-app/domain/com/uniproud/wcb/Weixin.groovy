package com.uniproud.wcb
/**
 * 微信公众号
 */
class Weixin {
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
     * 微信公众号名称
     */
    String name
    /**
     * 原始ID 接收微信消息时该值存放在 ToUserName 中
     */
    String ysid
    /**
     * 第三方用户唯一凭证
     * 微信平台分配
     */
    String appid
    /**
     * 第三方用户唯一凭证密钥，即appsecret
     * 微信平台分配
     */
    String secret
    /**
     * token  用户自己指定
     */
    String token
    /**
     * accessToken是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。正常情况下access_token有效期为7200秒，重复获取将导致上次获取的access_token失效
     */
    String accessToken
    /**
     * 凭证有效时间，单位：秒
     */
    Integer expiresin = 0
    /**
     * 凭证有效激活开始时间
     */
    Date startExpires = new Date()
    /**
     * 凭证过期结束时间
     */
    Date endExpires = new Date()
    /**
     * 状态  1,禁用 2，已激活  3，待审查
     */
    Integer state
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
        user nullable: false
    }
    static mapping = {
        table('t_weixin')
    }
}
