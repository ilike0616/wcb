package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 市场活动
 * @author qiaoxu
 *
 */
class MarketActivity extends ExtField{
	static belongsTo = [User,Employee]
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
	 * 活动主题
	 */
	String name
	/**
	 * 目的
	 */
	String objective

	/**
	 * 所使用的微信公众账号
	 */
	Weixin weixin
	/**
	 * 二维码
	 */
	QRCode qrCode
	/**
	 * 活动类型
	 * 关联数据字典
	 * 1，促销活动
	 * 2，品牌活动
	 * 3，会议销售
	 * 4，搜索引擎
	 * 5，互联网广告
	 * 6，平面媒体广告
	 * 7，电视媒体广告
	 * 8，关系公关
	 * 9，电话营销
	 * 10，短信营销
	 * 11，邮件营销
	 */
	Integer type
	/**
	 *  开始时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date startDate
	/**
	 * 结束时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date endDate
	/**
	 * 地点
	 */
	String address
	/**
	 * 预计成本
	 */
	Double expectedCost
	/**
	 * 实际成本
	 */
	Double actualCost
	/**
	 * 预计收入
	 */
	Double expectedIncome
	/**
	 * 实际收入
	 */
	Double actualIncome
	/**
	 * 计划
	 */
	String plan
	/**
	 * 执行描述
	 */
	String execDesc
	/**
	 * 总结
	 */
	String summary
	/**
	 * 效果
	 */
	String effect
	/**
	 * 描述
	 */
	String remark
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated

	static hasMany = [files:Doc,photos:Doc,customers:MarketActivityCustomer]
	
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	
	static V = [
		list:[viewId:'MarketActivityList',title:'市场活动列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['market_activity_add','market_activity_update','market_activity_view','market_activity_delete'
                ,'market_activity_import','market_activity_export']//视图对应的操作
			],
		add:[viewId:'MarketActivityAdd',title:'添加市场活动',clientType:'pc',viewType:'form'],
		edit:[viewId:'MarketActivityEdit',title:'修改市场活动',clientType:'pc',viewType:'form'],
		view:[viewId:'MarketActivityView',title:'查看市场活动',clientType:'pc',viewType:'form'],
		madd:[viewId:'MarketActivityMadd',title:'添加市场活动',clientType:'mobile',viewType:'form'],
		medit:[viewId:'MarketActivityMedit',title:'修改市场活动',clientType:'mobile',viewType:'form'],
		mview:[viewId:'MarketActivityMview',title:'查看市场活动',clientType:'mobile',viewType:'form']
		]
	static list = ['name','type','startDate','endDate','address','actualCost','actualIncome']
	static add = ['name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','files']
	static edit = ['id','name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','files']
	static view = ['name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','dateCreated','lastUpdated','files','photos']
	static madd = ['name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','photos']
	static medit = ['id','name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','photos']
	static mview = ['name','objective','type','startDate','endDate','address','expectedCost','actualCost','expectedIncome','actualIncome',
			'plan','execDesc','summary','effect','remark','dateCreated','lastUpdated','photos']
	static pub = ['user','employee']
	static allfields = list+add+edit+view+pub+madd+medit
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"objective":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"plan":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"execDesc":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"summary":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"effect":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		]
		]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		name attributes:[text:'活动主题',must:true]
		objective attributes:[text:'活动目的',must:true]
		type attributes:[text:'活动类型',must:true,dict:29]
		startDate attributes:[text:'开始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		address attributes:[text:'地点',must:true]
		expectedCost attributes:[text:'预计成本',must:true]
		actualCost attributes:[text:'实际成本',must:true]
		expectedIncome attributes:[text:'预计收入',must:true]
		actualIncome attributes:[text:'实际收入',must:true]
		plan attributes:[text:'计划',must:true]
		execDesc attributes:[text:'执行描述',must:true]
		summary attributes:[text:'总结',must:true]
		effect attributes:[text:'效果',must:true]
		remark attributes:[text:'备注',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	static mapping = {
		table('t_market_actitvity')
		plan column:'mplan'
	}
}
