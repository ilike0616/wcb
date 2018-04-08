package com.uniproud.wcb

import org.grails.databinding.BindingFormat

@grails.validation.Validateable(nullable=true)
class CloseCenter extends ExtField{
	 /**
	  * 所属用户
	  */
	User user
	/**
	 * 关闭者
	 */
	Employee owner
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 销售商机
	 */
	SaleChance saleChance
	/**
	 * 关闭类型
	 * 1：商机
	 */
	Integer closeType
	/**
	 * 关闭原因
	 */
	String closeReason
	/**
	 * 关闭结果
	 * 1：成功签约；2：项目失败
	 */
	Integer closeResult
	/**
	 * 是否反关闭
	 */
	Boolean isRevertClose = false
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

	static V = [
			list:[
					viewId:'CloseCenterList',title:'关闭中心列表',clientType:'pc',viewType:'list',
					ext:[],//扩展参数值最终进入ViewExtend
					opt:['close_center_view']//视图对应的操作
			],
			view:[viewId:'CloseCenterView',title:'查看关闭对象',clientType:'pc',viewType:'form',viewExtend:[]]
	]

	static list = [
			'owner.name','saleChance.subject','closeReason','closeResult','isRevertClose','dateCreated','lastUpdated'
	]
	static add = []
	static edit = []
	static view = [
			'owner.name','saleChance.subject','closeReason','closeResult','isRevertClose','dateCreated','lastUpdated'
	]

	static pub = ['user','employee','saleChance']
	static allfields = list+add+edit+view+pub

	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [:]

    static constraints = {
    	user nullable: false
        owner nullable: false
		saleChance nullable: true
        id attributes:[text:'ID',must:true]
		closeReason attributes:[text:'关闭原因']
		closeResult attributes:[text:'关闭结果',must:true,dict: 74]
		closeType attributes:[text:'关闭类型',must:true,dict: 75]
		isRevertClose attributes:[text:'是否反关闭',must:true,dict: 39]
        deleteFlag attributes:[text:'删除标志']
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
    	table("t_close_center")
    }
}
