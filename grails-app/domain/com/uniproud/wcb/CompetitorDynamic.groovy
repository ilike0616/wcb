package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 对手动态
 * @author qiaoxu
 *
 */
class CompetitorDynamic extends ExtField{
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
	 * 竞争对手
	 */
	Competitor competitor
	/**
	 * 对手产品
	 */
	CompetitorProduct product
	/**
	 * 分类
	 */
	Integer kind
	/**
	 * 动态内容
	 */
	String content
	/**
	 * 起始时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date startDate
	/**
	 * 结束时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date endDate
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated

	static hasMany = [files:Doc,photos:Doc]
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	
	static V = [
		list:[viewId:'CompetitorDynamicList',title:'对手动态列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['competitor_dynamic_add','competitor_dynamic_update','competitor_dynamic_view','competitor_dynamic_delete'
                ,'competitor_dynamic_import','competitor_dynamic_export']//视图对应的操作
			],
		add:[viewId:'CompetitorDynamicAdd',title:'添加对手动态',clientType:'pc',viewType:'form'],
		edit:[viewId:'CompetitorDynamicEdit',title:'修改对手动态',clientType:'pc',viewType:'form'],
		view:[viewId:'CompetitorDynamicView',title:'查看对手动态',clientType:'pc',viewType:'form'],
		madd:[viewId:'CompetitorDynamicMadd',title:'添加对手动态',clientType:'mobile',viewType:'form'],
		medit:[viewId:'CompetitorDynamicMedit',title:'修改对手动态',clientType:'mobile',viewType:'form'],
		mview:[viewId:'CompetitorDynamicMview',title:'查看对手动态',clientType:'mobile',viewType:'form']
		]
	static list = ['competitor.name','kind','content','startDate','endDate','dateCreated']
	static add = ['competitor.name','kind','content','startDate','endDate','files']
	static edit = ['id','competitor.name','kind','content','startDate','endDate','files']
	static view = ['competitor.name','kind','content','startDate','endDate','dateCreated','lastUpdated','files','photos']
	static madd = ['competitor.name','kind','content','startDate','endDate','photos']
	static medit = ['id','competitor.name','kind','content','startDate','endDate','photos']
	static mview = ['competitor.name','kind','content','startDate','endDate','dateCreated','lastUpdated','photos']
	static pub = ['user','employee','competitor','product']
	static allfields = list+add+edit+view+pub+madd+medit
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"competitor.name":[
			add:[xtype:'baseSpecialTextfield',store:'CompetitorStore',viewId:'CompetitorList',hiddenName:'competitor'],
			edit:[xtype:'baseSpecialTextfield',store:'CompetitorStore',viewId:'CompetitorList',hiddenName:'competitor'],
			madd:[xtype:'selectListField',target:'competitorSelectList'],
			medit:[xtype:'selectListField',target:'competitorSelectList']
		],
		"content":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		]
		]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		competitor nullable:false	
		kind attributes:[text:'分类',must:true,dict:32]
		content attributes:[text:'动态内容',must:true]
		startDate attributes:[text:'起始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table("t_competitor_dynamic")
	}
}
