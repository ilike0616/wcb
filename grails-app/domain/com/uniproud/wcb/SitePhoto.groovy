package com.uniproud.wcb

class SitePhoto {

    static hasMany = [photos: Doc,photos1: Doc,photos2: Doc,photos3: Doc,photos4: Doc,photos5: Doc,photos6: Doc,photos7: Doc,photos8: Doc]
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
     * 客户
     */
    Customer customer
    /**
     * 拍照主题
     */
    String subject
    /**
     * 备注
     */
    String remark
    String remark1
    String remark2
    String remark3
    String remark4
    String remark5
    String remark6
    String remark7
    String remark8
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
        employee nullable:true
    }

    static mapping = {
        table('t_site_photo')
    }
}
