package com.uniproud.wcb

import org.grails.databinding.BindingFormat

@grails.validation.Validateable(nullable=true)
class SaleChanceFollow extends ExtField{
	static belongsTo = [User,Employee,SaleChance,Customer]
	static hasMany = [files:Doc,files1:Doc]
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
     * 跟进商机
     */
	SaleChance saleChance
	/**
	 * 客户
	 */
	Customer customer
	/**
	 * 联系人
	 */
	Contact contact
    /**
     * 跟进主题
     */	
	String subject
    /**
     * 跟进时间
     */	
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date followDate
	/**
	 * 跟进结束时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date endDate
	/**
	 * 跟进方式
	 */
	Integer followKind
	/**
	 * 跟进前阶段
	 */
	Integer beforePhase
	/**
	 * 跟进后阶段
	 */
	Integer afterPhase
    /**
     * 跟进内容
     */	
	String followContent
	/**
	 * 下次跟进时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date nextFollowDate
	/**
	 * 下次跟进结束时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date nextEndDate
	/**
	 * 下次跟进方式
	 */
	Integer nextFollowKind
	/**
	 * 下次跟进描述
	 */
	String nextFollowDesc
	/**
	 * 可能性
	 */
    Integer possibility
	/**
	 * 预计签单日期
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date foreseeDate
	/**
	 * 预计签单金额
	 */
	Double foreseeMoney
	/**
	 * 是否领导已评阅
	 */
	Boolean isReview = false
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
			viewId:'SaleChanceFollowList',title:'商机跟进列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['sale_chance_follow_add','sale_chance_follow_update','sale_chance_follow_view','sale_chance_follow_delete',
            'sale_chance_follow_import','sale_chance_follow_export']//视图对应的操作
		],
		latestSaleChanceFollowList:[
				viewId:'LatestSaleChanceFollowList',title:'最新商机跟进',clientType:'pc',viewType:'list',
				ext:[],//扩展参数值最终进入ViewExtend
				opt:[]//视图对应的操作
		],
		add:[viewId:'SaleChanceFollowAdd',title:'添加商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'SaleChanceFollowEdit',title:'修改商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'SaleChanceFollowView',title:'查看商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'SaleChanceFollowMadd',title:'添加商机跟进',clientType:'mobile',viewType:'form'],
		medit:[viewId:'SaleChanceFollowMedit',title:'修改商机跟进',clientType:'mobile',viewType:'form'],
		mview:[viewId:'SaleChanceFollowMview',title:'查看商机跟进',clientType:'mobile',viewType:'form']
	]
	
	 static list = [
		'customer.name','saleChance.subject','contact.name','subject','followDate','followKind','afterPhase',
		'possibility','nextFollowDate','nextFollowKind','employee.name'
	 ]

	static latestSaleChanceFollowList = [
			'customer.name','saleChance.subject','subject','followDate','followKind','afterPhase','employee.name'
	]
	 static add = [
			'subject','customer.name','contact.name','saleChance.subject','followDate','endDate','followKind',
			'beforePhase','afterPhase','followContent','nextFollowDate','nextEndDate','nextFollowKind','nextFollowDesc',
			'possibility'
	  ]
	 static madd = add
	 
	 static edit = [
         'id','subject','customer.name','contact.name','saleChance.subject','followDate','endDate','followKind',
		 'beforePhase','afterPhase','followContent','nextFollowDate','nextEndDate','nextFollowKind','nextFollowDesc',
		 'possibility','dateCreated','lastUpdated'
	  ]
	 static medit = edit
	 
	 static view = [
		 'subject','customer.name','contact.name','saleChance.subject','followDate','endDate','followKind',
		 'beforePhase','afterPhase','followContent','nextFollowDate','nextEndDate','nextFollowKind','nextFollowDesc',
		 'customer.id','contact.id','saleChance.id','possibility','dateCreated','lastUpdated'
	  ]
	 static mview = view 
	 
	 static pub = ['user','employee','customer','contact','saleChance','files','files1']
	 static allfields = list+add+edit+view+pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [
            "customer.name":[
                    add:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer',initName:'saleChance.customer.name'],
                    edit:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
					madd:[xtype:'selectListField',target:'customerSelectList',nextSelectListField:'contact.name'],
					medit:[xtype:'selectListField',target:'customerSelectList',nextSelectListField:'contact.name']
            ],
            "contact.name":[
                    add:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer',initName:'saleChance.contact.name'],
                    edit:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
					madd:[xtype:'selectListField',target:'contactSelectList'],
					medit:[xtype:'selectListField',target:'contactSelectList']
            ],
            "saleChance.subject":[
                    add:[xtype:'baseSpecialTextfield',store:'SaleChanceStore',viewId:'SaleChanceList',hiddenName:'saleChance',paramName : 'customer'],
                    edit:[xtype:'baseSpecialTextfield',store:'SaleChanceStore',viewId:'SaleChanceList',hiddenName:'saleChance',paramName : 'customer'],
					madd:[xtype:'selectListField',target:'saleChanceSelectList'],
					medit:[xtype:'selectListField',target:'saleChanceSelectList']
            ],
			"beforePhase":[
				add:[initName:'saleChance.followPhase'],
				edit:[initName:'saleChance.followPhase'],
				madd:[initName:'saleChance.followPhase'],
				medit:[initName:'saleChance.followPhase']
			],
			"followContent":[
					add: [xtype: 'textarea',colspan:2,width:500,grow:true],
					edit: [xtype: 'textarea',colspan:2,width:500,grow:true],
					view: [xtype: 'textarea',colspan:2,width:500,grow:true]
			],
			"nextFollowDesc":[
					add: [xtype: 'textarea',colspan:2,width:500,grow:true],
					edit: [xtype: 'textarea',colspan:2,width:500,grow:true],
					view: [xtype: 'textarea',colspan:2,width:500,grow:true]
			]

    ]
    static constraints = {
    	user nullable: false
        employee nullable: false
        customer nullable: false
		contact nullable: true
		saleChance nullable: false
        id attributes:[text:'ID',must:true]
        subject attributes:[text:'跟进主题',must:true]
		followDate attributes:[text:'跟进时间',must:true]
        endDate attributes:[text:'跟进结束时间',must:true]
        followKind attributes:[text:'跟进方式',must: true,dict:47]
		beforePhase attributes:[text:'跟进前阶段',must:true,dict:48]
		afterPhase attributes:[text:'跟进后阶段',must:true,dict:48]
        followContent attributes:[text:'跟进内容',must:true]
		nextFollowDate attributes:[text:'下次跟进时间',must:true]
        nextEndDate attributes:[text:'下次跟进结束时间',must:true]
		nextFollowKind attributes:[text:'下次跟进方式',must:true,dict:47]
		nextFollowDesc attributes:[text:'下次跟进描述',must:true]
		possibility attributes:[text:'可能性',must:true,dict: 52]
		foreseeDate attributes:[text:'预计签单时间']
		foreseeMoney attributes:[text:'预计签单金额']
        deleteFlag attributes:[text:'删除标志']
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
    	table("t_sale_chance_follow")
    }
}
