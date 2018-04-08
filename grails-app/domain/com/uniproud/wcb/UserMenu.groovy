package com.uniproud.wcb

class UserMenu {
	/**
	 * 所属用户
	 */
	User user
	/**
	 * 所属模块
	 */
	Module module
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 名称
	 */
	String text 
	/**
	 * 显示顺序
	 */
	Long idx 
	/**
	 * 图标
	 */
	String iconCls
	/**
	 * 上级模块
	 */
	UserMenu parentUserMenu
	
	static hasMany = [children:UserMenu]
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated
	
    static constraints = {
		user nullable: false
		module nullable:false
		text nullable:false
    }
	
	static mapping = {
		table('t_user_menu')
        children sort:'idx'
	}
}
