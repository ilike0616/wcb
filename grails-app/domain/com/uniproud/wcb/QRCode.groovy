package com.uniproud.wcb
/**
 * 二维码管理
 */
class QRCode {
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
     * 所使用的微信公众账号
     */
    Weixin weixin
    /**
     * 二维码类型
     * 1、临时二维码
     * 2、永久二维码
     */
    Integer type
    /**
     * 二维码文件管理
     */
    Doc doc
    /**
     * 临时二维码用这个
     */
    Integer sceneId
    /**
     * 永久二维码使用这个字段
     */
    String sceneStr
    /**
     * 到期时间
     */
    Date dateExpire
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated
    static constraints = {

    }

     static mapping = {
         table('t_qr_code')
     }
}
