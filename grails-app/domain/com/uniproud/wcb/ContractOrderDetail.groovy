package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 数据对象明细表
 * @author shqv
 */
class ContractOrderDetail extends ExtField{
	static belongsTo = [ContractOrder]
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
    ContractOrder contractOrder
	/**
	 * 商机
	 */
	SaleChance saleChance
	/**
	 * 商机明细
	 */
	SaleChanceDetail saleChanceDetail
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
	Double num = 0
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
	/**
	 * 已开票数量
	 */
	Double invoiceNum = 0
	/**
	 * 允许新增的开票数量（辅助判断字段，不存数据库）
	 * @return
	 */
	Double getAllowAddInvoiceNum(){
		def num = 0
		if(!this.num)
			return num
		InvoiceDetail.createCriteria().list {
			eq("contractOrderDetail", this)
			eq("deleteFlag", false)
		}?.each {
			if((!it.invoice.audit || it.invoice.audit != 4) && it.num){
				num += it.num
			}
		}
		this.num - num
	}

	static transients = ['allowAddInvoiceNum']

	static V = [
		list:[
			viewId:'ContractOrderDetailList',title:'订单明细',clientType:'pc',viewType:'list',store:'ContractOrderDetailStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['contract_order_detail_view','contract_order_detail_export']//视图对应的操作
		],
		editList:[
			viewId:'ContractOrderDetailEditList',title:'订单明细',clientType:'pc',viewType:'list',store:'ContractOrderDetailStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['contract_order_detail_view']//视图对应的操作
		],
		view:[viewId:'ContractOrderDetailView',title:'查看订单明细',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'ContractOrderDetailMadd',title:'添加订单明细',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ContractOrderDetailMedit',title:'修改订单明细',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ContractOrderDetailMview',title:'查看订单明细',clientType:'mobile',viewType:'form']
	]

	static list = ['contractOrder.id', 'contractOrder.subject', 'product.name', 'price', 'num', 'totalPrice', 'remark']
	static editList = ['contractOrder.id', 'product.id', 'product.name', 'price', 'num', 'totalPrice', 'remark']
	static view = ['product.name', 'price', 'num', 'totalPrice', 'remark']
	static madd = ['product.name', 'price', 'num', 'remark']
	static medit = ['id', 'product.name', 'price', 'num', 'remark']
	static mview = ['product.name', 'price', 'num', 'remark']

	static pub = ['user', 'employee', 'contractOrder', 'product']
	static allfields = list+editList+view+madd+medit+mview+pub

	//默认插入 userFiledExtend中的参数
	static FE = [
			pc:[
				price:[initName:'product.salePrice']
			]
		]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		'product.name':[
            editList:[xtype:'baseDetailTextField',store:'ProductStore',viewId:'ProductList',hiddenName:'product'],
			madd:[xtype:'selectListField',target:'productSelectList'],
			medit:[xtype:'selectListField',target:'productSelectList']
		]
	]

	static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable: false
		employee nullable: false
        contractOrder nullable: false
		product nullable: false
		price attributes:[text:'价格',must:true]
		num attributes:[text:'数量',must:true]
		totalPrice attributes:[text:'小计',must:true]
		extNum1 attributes:[text:'扩展字段1',must:true]
		extNum2 attributes:[text:'扩展字段2',must:true]
		extNum3 attributes:[text:'扩展字段3',must:true]
		remark attributes:[text:'备注',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
	}

	static mapping = {
        table('t_contract_order_detail')
    }
}
