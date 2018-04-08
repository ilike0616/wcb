package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 安装明细，
 * 有可能有需求要增加描述性产品（即不从产品表内选取，而直接手工输入产品名称，这种在实际当中是经常会碰到的）的安装
 */
class InstallOrderDetail extends ExtField{

    static belongsTo = [InstallOrder]
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
     * 所属安装单
     */
    InstallOrder installOrder
    /**
     * 产品
     */
    Product product
    /**
     * 产品名称，本字段主要用于描述性产品输入
     */
    String productName
    /**
     * 产品描述
     */
    String productDescription
    /**
     * 产品价格
     */
    Double price = 0
    /**
     * 产品数量
     */
    Double num = 0
    /**
     * 小计=数量*价格
     */
    Double totalPrice
    /**
     * 备注信息
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
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

    static constraints = {
        user nullable: false
    }
    static mapping = {
        table('t_install_order_detail')
    }
}
