package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 数据对象明细表
 * @author shqv
 */
class SaleChanceDetail extends ExtField{
	static belongsTo = [SaleChance]
	/**
	 * 所属用户
	 */
	User user
	/**
	 * 所属员工
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
	 * 所属订单
	 */
	SaleChance saleChance
	/**
	 * 产品
	 */
	Product product
	/**
	 * 产品价格
	 */
	Double price
	/**
	 * 产品数量
	 */
	Integer num
	/**
	 * 小计=数量*价格
	 */
	Double totalPrice
	/**
	 * 扩展字段1
	 */
	Integer extNum1
	/**
	 * 扩展字段2
	 */
	Integer extNum2
	/**
	 * 扩展字段3
	 */
	Integer extNum3
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
		employee nullable: false
		saleChance nullable: false
	}

	static mapping = {
        table('t_sale_chance_detail')
    }
}
