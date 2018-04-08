package com.uniproud.wcb

import grails.transaction.Transactional
import org.grails.databinding.BindingFormat
/**
 * 合同订单
 * @author shqv
 */
class ContractOrder extends ExtField{
	static belongsTo = [SaleChance]
	static hasMany = [files:Doc,photos:Doc,detail:ContractOrderDetail,financeIncome:FinanceIncomeExpense]
	/**
	 * 所属用户
	 */
	User user
	/**
	 * 创建者
	 */
	Employee employee
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 所属商机
	 */
	SaleChance saleChance
	/**
	 * 对象主题
	 */
	String subject
    /**
     * 订单编号
     */
    String orderNo
    /**
     * 分类
     * 1.A产品销售
     * 2.B产品销售
     * 3.C产品销售
     * 4.D产品销售
     * 5.E产品销售
     */
    Integer kind
	/**
	 * 订单日期
	 */
	@BindingFormat('yyyy-MM-dd')
    Date startDate
    /**
     * 发货日期
     */
    @BindingFormat('yyyy-MM-dd')
    Date endDate
    /**
     * 公开金额
     */
    Double sumMoney
    /**
     * 实际金额
     */
    Double discountMoney = 0
    /**
     * 折扣率（％）
     */
    Double discountRate = 100
	/**
	 * 收款状态:1-未收款；2-部分收款；3-已收款
	 */
	Integer paidState = 1
	/**
	 * 收款金额
	 */
	Double paidMoney = 0
	/**
	 * 待收款金额
	 */
	Double payingMoney = 0
    /**
     * 付款方式
     * 1.现金
     * 2.支票
     * 3.转账
     * 4.托收
     */
    Integer payMethod
	/**
	 * 开票状态：1-未开票；2-部分开票；3-已开票
	 * @return
	 */
	Integer invoiceState = 1
	/**
	 * 已开票金额
	 */
	Double invoiceMoney = 0
    /**
     * 所有者
     */
    Employee owner
    /**
     * 快递公司
     * 1.顺丰
     * 2.EMS
     */
    Integer expressDeliveryCompany
    /**
     * 运单号
     */
    String expressDeliveryNo
    /**
     * 状态
     * 1.款待排产
     * 2.正生产
     * 3.排产待发货
     * 4.已发货待开船
     * 5.已开船等退单
     * 6.退单已到
     */
    Integer executeStatus
    /**
     * 发货状态
     * 1.无需发货
     * 2.待发货
     * 3.发货审核
     * 4.部分发货
     * 5.全部发货
     */
    Integer goodsStatus
	/**
	 * 关联客户
	 */
	Customer customer
	/**
	 * 关联联系人
	 */
	Contact contact
	/**
	 * 关联对象
	 */
	OnsiteObject onsiteObject
	/**
	 * 关联的跟进记录，
	 * 此字段，留做备有，
	 */
	ObjectFollow objectFollow

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

	static transients = ['allowAddIncomeMoney','allowAddInvoiceMoney']
	/**
	 * 运行新增的剩余收款金额（辅助判断字段，不存数据库）
	 * @return
	 */
	Double getAllowAddIncomeMoney(){
		def money = 0
		if(!this.discountMoney){
			return money
		}
		FinanceIncomeExpense.createCriteria().list {
			eq("contractOrder", this)
			eq("financeType",1)
			ne("chargeState",3) //排除禁止的
			eq("deleteFlag", false)
		}?.each {
			if(!it.audit || it.audit?.auditState != 4) {
				if(it.money) {
					money += it.money
				}
			}
		}
		this.discountMoney - money
	}

	Double getAllowAddInvoiceMoney(){
		def money = 0
		if(!this.discountMoney)
			return money
		Invoice.createCriteria().list {
			eq("contractOrder", this)
			eq("deleteFlag", false)
		}?.each {
			if(!it.audit || it.audit?.auditState != 4) {
				if(it.invoiceMoney)
					money += it.invoiceMoney
			}
		}
		this.discountMoney - money
	}

	def contractOrderService
	def afterInsert(){
		contractOrderService.updateSaleChance(this)
		contractOrderService.updateCustomerLatestOrderDate(this)
		this.payingMoney = this.discountMoney
	}

	def afterUpdate(){
		contractOrderService.updateSaleChance(this)
		contractOrderService.updatePaidState(this)
		contractOrderService.updateInvoiceState(this)
	}

	static V = [
		list:[
			viewId:'ContractOrderList',title:'订单管理',clientType:'pc',viewType:'list',store:'ContractOrderStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['contract_order_add', 'contract_order_update', 'contract_order_view', 'contract_order_delete','contract_order_import'
                ,'contract_order_export']//视图对应的操作
		],
		latestContractOrderList:[
			viewId:'LatestContractOrderList',title:'最新订单',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:[]//视图对应的操作
		],
		view:[viewId:'ContractOrderView',title:'查看订单',clientType:'pc',viewType:'form',viewExtend:[]],
		add:[viewId:'ContractOrderAdd',title:'添加订单',clientType:'pc',viewType:'form'],
		edit:[viewId:'ContractOrderEdit',title:'修改订单',clientType:'pc',viewType:'form'],
		madd:[viewId:'ContractOrderMadd',title:'添加订单',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ContractOrderMedit',title:'修改订单',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ContractOrderMview',title:'查看订单',clientType:'mobile',viewType:'form']
	]

	static list = ['subject', 'startDate', 'customer.name', 'contact.name', 'employee.name', 'dateCreated', 'remark']
	static latestContractOrderList = ['subject', 'startDate', 'customer.name', 'contact.name', 'employee.name', 'dateCreated', 'remark']
	static add = ['subject', 'startDate', 'customer.name', 'contact.name', 'remark', 'detail']
	static edit = ['id', 'subject', 'startDate', 'customer.name', 'contact.name', 'remark', 'detail']
	static view = ['id', 'subject', 'startDate', 'customer.name', 'contact.name', 'employee.name', 'dateCreated', 'remark', 'detail']
	static madd = ['subject', 'startDate', 'customer.name', 'contact.name', 'remark']
	static medit = ['id', 'subject', 'startDate', 'customer.name', 'contact.name', 'remark']
	static mview = ['subject', 'startDate', 'customer.name', 'contact.name', 'employee.name', 'dateCreated', 'remark']

	static pub = ['user', 'employee', 'customer', 'contact', 'onsiteObject', 'objectFollow', 'financeIncome']
	static allfields = list+add+edit+view+madd+medit+mview+pub

	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"customer.name":[
			add:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
			edit:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
			madd:[xtype:'selectListField',target:'customerSelectList',nextSelectListField:'contact.name'],
			medit:[xtype:'selectListField',target:'customerSelectList',nextSelectListField:'contact.name']
		],
		"contact.name":[
			add:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
			edit:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
			madd:[xtype:'selectListField',target:'contactSelectList'],
			medit:[xtype:'selectListField',target:'contactSelectList']
		],
		detail:[
			add:[title:'产品明细',listView:'ContractOrderDetailEditList'],
			edit:[title:'产品明细',listView:'ContractOrderDetailEditList'],
			view:[title:'产品明细',listView:'ContractOrderDetailEditList']
		]
	]

	static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable: false
		employee nullable: false
		customer nulable:true
		contact nulable:true
		onsiteObject nulable:true
		subject attributes:[text:'订单主题',must:true]
        orderNo attributes:[text:'订单编号',must:true]
        kind attributes:[text:'分类',must:true,dict:56]
		startDate attributes:[text:'订单日期',must:true]
        endDate attributes:[text:'发货日期',must:true]
        sumMoney attributes:[text:'公开金额',must:true]
        discountMoney attributes:[text:'实际金额',must:true]
        discountRate attributes:[text:'折扣率',must:true]
        payMethod attributes:[text:'付款方式',must:true,dict:42]
        owner attributes:[text:'所有者',must:true]
        expressDeliveryCompany attributes:[text:'快递公司',must:true,dict:43]
        expressDeliveryNo attributes:[text:'运单号',must:true]
        executeStatus attributes:[text:'状态',must:true,dict:44]
        goodsStatus attributes:[text:'发货状态',must:true,dict:45]
		remark attributes:[text:'备注',must:true]
		deleteFlag attributes:[text:'删除标志',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
	}
	static mapping = {
        table('t_contract_order')
    }
}
