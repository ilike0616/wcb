package com.uniproud.wcb

import grails.transaction.Transactional
import org.grails.databinding.BindingFormat

@grails.validation.Validateable(nullable=true)
class SaleChance extends ExtField{
	static belongsTo = [User,Employee,Customer]
	static hasMany = [competitors:Competitor,files:Doc,files1:Doc,detail:SaleChanceDetail,contractOrders:ContractOrder]
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
     * 跟进客户
     */
	Customer customer
	/**
	 * 联系人
	 */
	Contact contact
	/**
	 * 产品
	 */
	Product product
    /**
     * 商机主题
     */	
	String subject
	/**
	 * 商机编号
	 */
	String chanceNo
	/**
	 * 商机分类
	 * 1、分类一	   2、分类二		3、分类三
	 */
	Integer chanceKind
	/**
	 * 商机状态
	 * 1、跟踪	2、成功	3、失败	4、搁置	5、失效
	 */
	Integer chanceState
	/**
	 * 商机来源
	 * 1、电话来访	2、客户介绍	3、独立开发	4、媒体宣传	5、老客户
	 * 6、代理商		7、合作伙伴	8、公开招标	9、互联网		10、其他
	 */
	Integer chanceSource
	/**
	 * 预计签单时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date foreseeDate
	/**
	 * 预计金额
	 */
	Double foreseeMoney
	/**
	 * 发现日期
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date foundDate
	/**
	 * 负责人
	 */
	Employee principalMan
	/**
	 * 客户需求
	 */
	String customerRequire
	/**
	 * 可能性
	 */
	Integer possibility
	/**
	 * 跟进阶段
	 */
	Integer followPhase
	/**
	 * 商机跟进时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date followDate
	/**
	 * 跟进次数
	 */
	Integer followNum
	/**
	 * 跟进结果
	 */
	String followRemark
	/**
	 * 关闭者
	 */
	Employee closeMan
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
	 * 关闭时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date closeDate
	/**
	 * 是否关闭
	 */
	Boolean isClosed
	/**
	 * 是否共享
	 */
	Boolean isShared
	/**
	 * 付款方式
	 */
	Integer payMethod
	/**
	 * 付款条件
	 */
	Integer payCondition
	/**
	 * 位置
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
	/**
	 * 可能金额
	 */
	Double possibilityMoney
	/**
	 * 信心指数
	 */
	Integer confidenceIndex
	/**
	 * 下次跟进日期
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date nextFollowDate
	/**
	 * 实际金额
	 */
	Double discountMoney
	/**
	 * 折扣率
	 */
	Double discountRate
	/**
	 * 签单额
	 */
	Double orderMoney
	/**
	 * 停留天数
	 */
	Integer stayDays
	static transients = ['stayDays']
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
			viewId:'SaleChanceList',title:'商机列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['sale_chance_add','sale_chance_update','sale_chance_view','sale_chance_delete',
            'sale_chance_import','sale_chance_export','sale_chance_follow','sale_chance_close',
			'sale_chance_locus']//视图对应的操作
		],
		latestSaleChanceList:[viewId:'LatestSaleChanceList',title:'最新商机',clientType:'pc',viewType:'list',
							  ext:[],opt:[]
		],
		add:[viewId:'SaleChanceAdd',title:'添加商机',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'SaleChanceEdit',title:'修改商机',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'SaleChanceView',title:'查看商机',clientType:'pc',viewType:'form',viewExtend:[]],
		close:[viewId:'SaleChanceClose',title:'关闭商机',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'SaleChanceMadd',title:'添加商机',clientType:'mobile',viewType:'form'],
		medit:[viewId:'SaleChanceMedit',title:'修改商机',clientType:'mobile',viewType:'form'],
		mview:[viewId:'SaleChanceMview',title:'查看商机',clientType:'mobile',viewType:'form']
	]
	
	 static list = [
	 		'chanceNo','subject','customer.name','foreseeDate','foreseeMoney','followPhase',
			'possibility','followDate','stayDays','chanceState','employee.name'
	 ]
	static latestSaleChanceList = [
			'chanceNo','subject','customer.name','foreseeDate','foreseeMoney','possibility','chanceState','employee.name'
	]

	 static add = [
	 		'chanceNo','subject','customer.name','contact.name','chanceKind','chanceState','chanceSource',
			'foreseeDate','foundDate','foreseeMoney','principalMan.name','followPhase','confidenceIndex',
			'customerRequire','possibility','payMethod','payCondition','discountMoney',
			'discountRate','competitors.name'
	  ]
	 static madd = add
	 
	 static edit = [
         'id','chanceNo','subject','customer.name','contact.name','chanceKind','chanceState','chanceSource',
		 'foreseeDate','foundDate','foreseeMoney','principalMan.name','followPhase','confidenceIndex',
		 'customerRequire','possibility','payMethod','payCondition','discountMoney','discountRate',
		 'competitors.name','dateCreated','lastUpdated'
	  ]
	 static medit = edit
	 
	 static view = [
			 'chanceNo','subject','customer.name','contact.name','chanceKind','chanceState','chanceSource',
			 'foreseeDate','foundDate','foreseeMoney','principalMan.name','followPhase','confidenceIndex',
			 'customerRequire','possibility','payMethod','payCondition','discountMoney','discountRate',
			 'competitors.name','followDate','followNum','followRemark','closeMan.name','closeReason','closeDate',
			 'customer.id','contact.id','product.id','principalMan.id','isClosed',
			 'isShared','location','possibilityMoney','nextFollowDate','stayDays','dateCreated','lastUpdated'
	  ]
	 static mview = view

	static close = [
			'closeReason','id'
	]
	 
	 static pub = ['user','employee','customer','contact','principalMan','product','closeMan','competitors','files','files1']
	 static allfields = list+add+edit+view+pub+close

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
			"principalMan.name":[
					list: [fieldLabel: '负责人'],
					add:[fieldLabel:'负责人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'principalMan'],
					edit:[fieldLabel:'负责人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'principalMan'],
					view: [fieldLabel: '负责人'],
					madd:[xtype:'selectListField',target:'employeeSelectList',label:'负责人'],
					medit:[xtype:'selectListField',target:'employeeSelectList',label:'负责人']
			],
			"competitors.name":[
					add:[fieldLabel:'竞争者',xtype:'baseMultiSelectTextareaField',store:'CompetitorStore',viewId:'CompetitorList',hiddenName:'competitors',colspan:2],
					edit:[fieldLabel:'竞争者',xtype:'baseMultiSelectTextareaField',store:'CompetitorStore',viewId:'CompetitorList',hiddenName:'competitors',colspan:2],
					view:[fieldLabel:'竞争者',xtype:'textarea',colspan:2,width: 500],
                    madd:[xtype:'selectListField',target:'competitorSelectList',selectMode:'MULTI'],
                    medit:[xtype:'selectListField',target:'competitorSelectList',lselectMode:'MULTI'],
                    mview:[xtype:'selectListField',target:'competitorSelectList',selectMode:'MULTI'],
			],
			"closeMan.name":[
					list:[fieldLabel:'关闭者'],
					view:[fieldLabel:'关闭者']
			],
			"product.name":[
					add:[fieldLabel:'产品',xtype:'baseSpecialTextfield',store:'ProductStore',viewId:'ProductList',hiddenName:'product'],
					edit:[fieldLabel:'产品',xtype:'baseSpecialTextfield',store:'ProductStore',viewId:'ProductList',hiddenName:'product'],
					madd:[label:'产品',xtype:'selectListField',target:'productSelectList'],
					medit:[label:'产品',xtype:'selectListField',target:'productSelectList']
			],
			"customerRequire":[
			        add: [xtype: 'textarea',colspan:2,width:500,grow:true],
					edit: [xtype: 'textarea',colspan:2,width:500,grow:true],
					view: [xtype: 'textarea',colspan:2,width:500,grow:true]
			],
			"closeReason":[
			        close: [xtype: 'textarea',colspan:2,width:500,grow:true]
			],
			"stayDays":[
			        list: [xtype: 'columnCalculate',dataIndex:'followDate',calType:1]
			]
    ]
    static constraints = {
    	user nullable: false
        employee nullable: false
        id attributes:[text:'ID',must:true]
        chanceNo attributes:[text:'商机编号',must:true]
        subject attributes:[text:'商机主题',must:true]
		chanceKind attributes:[text:'商机分类',dict:49]
		chanceState attributes:[text:'商机状态',dict:50]
        chanceSource attributes:[text:'商机来源',dict:51]
		foreseeDate attributes:[text:'预计签单日期',must:true]
        foundDate attributes:[text:'发现日期',must:true]
		foreseeMoney attributes:[text:'预计签单金额',must:true]
		customerRequire attributes:[text:'客户需求',must:true]
        possibility attributes:[text:'可能性',must:true,dict: 52]
		followPhase attributes:[text:'跟进阶段',must:true,dict:48]
        followDate attributes:[text:'跟进日期']
		followNum attributes:[text:'跟进次数']
		followRemark attributes:[text:'跟进结果']
		closeReason attributes:[text:'关闭原因']
		closeDate attributes:[text:'关闭日期']
		isClosed attributes:[text:'是否关闭',must: true]
		isShared attributes:[text:'是否共享']
		payMethod attributes:[text:'付款方式',must:true,dict: 53]
		payCondition attributes:[text:'付款条件',must:true,dict: 54]
		location attributes:[text:'位置']
		longtitude attributes:[text:'经度']
		latitude attributes:[text:'维度']
		possibilityMoney attributes:[text:'可能金额']
		confidenceIndex attributes:[text:'信心指数',must:true,dict: 55]
		nextFollowDate attributes:[text:'下次跟进时间',must:true]
		discountMoney attributes:[text:'实际金额',must:true]
		discountRate attributes:[text:'折扣率',must:true]
		closeResult attributes:[text:'关闭结果',must:true,dict: 74]
		stayDays attributes:[text:'停留天数']
		orderMoney attributes:[text:'签单额']
        deleteFlag attributes:[text:'删除标志']
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
    	table("t_sale_chance")
    }
}
