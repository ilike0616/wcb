package com.uniproud.wcb

import org.grails.databinding.BindingFormat
/**
 * 当安装单为 预派模式和抢接模式的时候， 预接单人员存放在此数据模型中
 */
class InstallAllocateDetail extends ExtField{
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
     * 所有者
     */
    Employee owner
    /**
     * 关联安装单
     */
    InstallOrder installOrder
    /**
     * 待接单人
     */
    Employee receivedMan
    /**
     * 接单时间
     */
    Date  receivedDate
    /**
     * 是否已接单
     */
    Boolean isRecved = false
    /**
     * 是否已关闭
     */
    Boolean isClose = false
    /**
     * 备注
     */
    String remark
    /**
     *创建时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date dateCreated
    /**
     *修改时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date lastUpdated

    Boolean deleteFlag = false

    static constraints = {
        user nullable:false
    }

    static mapping = {
        table('t_install_allocate_detail')
    }
}
