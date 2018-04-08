package com.uniproud.wcb
/**
 * 竞品信息
 * @author qiaoxu
 *
 */
class CompetitorProduct extends ExtField{
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
	 * 产品名称
	 */
	String name
	/**
	 * 产品型号
	 */
	String model
	/**
	 * 产品描述
	 */
	String remark
	/**
	 * 销售价格
	 */
	Double salePrice
	/**
	 * 成本价格
	 */
	Double costPrice
	/**
	 * 优势
	 */
	String strength
	/**
	 * 劣势
	 */
	String weakneness
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
		list:[viewId:'CompetitorProductList',title:'竞争产品列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['competitor_product_add','competitor_product_update','competitor_product_view','competitor_product_delete'
                ,'competitor_product_import','competitor_product_export']//视图对应的操作
			],
		add:[viewId:'CompetitorProductAdd',title:'添加竞争产品',clientType:'pc',viewType:'form'],
		edit:[viewId:'CompetitorProductEdit',title:'修改竞争产品',clientType:'pc',viewType:'form'],
		view:[viewId:'CompetitorProductView',title:'查看竞争产品',clientType:'pc',viewType:'form'],
		madd:[viewId:'CompetitorProductMadd',title:'添加竞争产品',clientType:'mobile',viewType:'form'],
		medit:[viewId:'CompetitorProductMedit',title:'修改竞争产品',clientType:'mobile',viewType:'form'],
		mview:[viewId:'CompetitorProductMview',title:'查看竞争产品',clientType:'mobile',viewType:'form']
		]
	
	static list = ['competitor.name','name','model','salePrice','costPrice','dateCreated']
	static add = ['competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','files']
	static edit = ['id','competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','files']
	static view = ['competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','dateCreated','lastUpdated','files','photos']
	static madd = ['competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','photos']
	static medit = ['id','competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','photos']
	static mview = ['competitor.name','name','model','salePrice','costPrice','strength','weakneness','remark','dateCreated','lastUpdated','photos']
	static pub = ['user','employee','competitor']
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
		"strength":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"weakneness":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		]
		]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		competitor nullable:false
		name attributes:[text:'产品名称',must:true]
		model attributes:[text:'规格型号',must:true]
		remark attributes:[text:'产品描述',must:true]
		salePrice attributes:[text:'销售价',must:true]
		costPrice attributes:[text:'成本价',must:true]
		strength attributes:[text:'优势',must:true]
		weakneness attributes:[text:'劣势',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }

	static mapping = {
		table("t_competitor_product")
	}
}
