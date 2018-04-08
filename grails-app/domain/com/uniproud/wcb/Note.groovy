package com.uniproud.wcb
/**
 * 随笔
 * 与其他模块关联
 */
class Note {
    static belongsTo = [User,Employee,Customer]
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
     * 关联的客户
     */
    Customer customer
    /**
     * 标签
     * 1，汇报记录
     * 2，打电话
     * 3，见面拜访
     * 4，活动
     * 5，商务宴请
     */
    Integer tag
    /**
     * 随笔内容
     */
    String content
    /**
     * 位置 通过坐标获取的位置
     */
    String location
    /**
     * 对象经度
     */
    String longtitude
    /**
     * 对象维度
     */
    String latitude
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * ats @的员工
     * zs 点赞的员工
     * fiels上传的文件
     * voices 发送的语音
     */
    static hasMany = [ats:Employee,zs:NoteZs,files:Doc,voices:Doc,comments:Comment]

    static constraints = {
        user nullable:false
        employee nullable:false
        longtitude attributes:[text:'经度',must:true]
        latitude attributes:[text:'维度',must:true]
        location attributes:[text:'位置',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_note')
    }
}
