package com.uniproud.wcb
/**
 * 竞争对手
 * @author qiaoxu
 */
class Competitor extends ExtField{
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
	 * 对手名称
	 */
	String name 
	/**
	 * 规模
	 * 关联数据字典
	 * 1，0-20人
	 * 2，20-50人
	 * 3，50-100人
	 * 4，100-200人
	 * 5，200-500人
	 * 6，500-1000人
	 * 7，大于1000人
	 */
	Integer scale
	/**
	 * 竞争力
	 * 关联数据字典
	 * 1，一般竞争力
	 * 2，潜在竞争力
	 * 3，弱竞争力
	 * 4，强竞争力
	 */
	Integer power
	/**
	 * 优势
	 */
	String strength
	/**
	 * 劣势
	 */
	String weakneness
	/**
	 * 机会
	 */
	String opportunity
	/**
	 * 威胁 
	 */
	String threat
	/**
	 * 对策
	 */
	String policy
	/**
	 * 销售分析
	 */
	String salesAnalysis
	/**
	 * 市场分析
	 */
	String marketAnalysis
	/**
	 * 备注 
	 */
	String remark
	/**
	 * 位置 通过坐标获取的位置
	 */
	String location
	/**
	 * 对象经度
	 */
	String longtitude
	/**
	 * 对象维度
	 */
	String latitude
	
//	Point point
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
		list:[viewId:'CompetitorList',title:'竞争对手列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['competitor_add','competitor_update','competitor_view','competitor_delete','competitor_import'
                ,'competitor_export']//视图对应的操作
			],
		add:[viewId:'CompetitorAdd',title:'添加竞争对手',clientType:'pc',viewType:'form'],
		edit:[viewId:'CompetitorEdit',title:'修改竞争对手',clientType:'pc',viewType:'form'],
		view:[viewId:'CompetitorView',title:'查看竞争对手',clientType:'pc',viewType:'form'],
		madd:[viewId:'CompetitorMadd',title:'添加竞争对手',clientType:'mobile',viewType:'form'],
		medit:[viewId:'CompetitorMedit',title:'修改竞争对手',clientType:'mobile',viewType:'form'],
		mview:[viewId:'CompetitorMview',title:'查看竞争对手',clientType:'mobile',viewType:'form']
	]
	static list = ['name','scale','power','location','dateCreated']
	static add = ['name','scale','power','strength','weakneness','opportunity','threat','policy','salesAnalysis','marketAnalysis','location','remark','files']
	static edit = ['id','name','scale','power','strength','weakneness','opportunity','threat','policy','salesAnalysis','marketAnalysis','location','remark','files']
	static view = ['name','scale','power','strength','weakneness','opportunity','threat','policy'
		,'salesAnalysis','marketAnalysis','remark','location','longtitude','latitude','dateCreated','lastUpdated','files','photos']
	static madd = ['name','scale','power','strength','weakneness','opportunity','threat','policy','salesAnalysis','marketAnalysis','location','remark','photos']
	static medit = ['id','name','scale','power','strength','weakneness','opportunity','threat','policy','salesAnalysis','marketAnalysis','location','remark','photos']
	static mview = ['name','scale','power','strength','weakneness','opportunity','threat','policy'
		,'salesAnalysis','marketAnalysis','remark','location','longtitude','latitude','dateCreated','lastUpdated','photos']
	static pub = ['user','employee']
	static allfields = list+add+edit+view+pub+madd+medit
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"strength":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"weakneness":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"opportunity":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"threat":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"policy":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"salesAnalysis":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"marketAnalysis":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		]
		]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		name attributes:[text:'对手名称',must:true]
		scale attributes:[text:'规模',must:true,dict:30]
		power attributes:[text:'竞争力',must:true,dict:31]
		strength attributes:[text:'优势',must:true]
		weakneness attributes:[text:'劣势',must:true]
		opportunity attributes:[text:'机会',must:true]
		threat attributes:[text:'威胁',must:true]
		policy attributes:[text:'对策',must:true]
		salesAnalysis attributes:[text:'销售分析',must:true]
		marketAnalysis attributes:[text:'市场分析',must:true]
		remark attributes:[text:'备注',must:true]
		location attributes:[text:'位置',must:true]
		longtitude attributes:[text:'精度',must:true]
		latitude attributes:[text:'维度',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_competitor')
	}
}
