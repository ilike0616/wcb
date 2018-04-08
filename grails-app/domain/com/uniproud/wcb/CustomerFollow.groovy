package com.uniproud.wcb

import org.grails.databinding.BindingFormat

@grails.validation.Validateable(nullable=true)
class CustomerFollow extends ExtField{
	static belongsTo = [User,Employee,Customer]
	 /**
	  * 所属用户
	  */
	User user
    /**
     * 所属员工
     */
	Employee employee
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
     * photos 现场拍照<OutsiteRecord>
     */
    static hasMany = [photos:OutsiteRecord,files:Doc,files1:Doc]
	/**
	 * 现成签到
	 */
	OutsiteRecord signon
	/**
	 * 现成签退
	 */
	OutsiteRecord signout
    /**
     * 跟进主题
     */	
	String subject
    /**
     * 跟进开始时间
     */	
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date startDate
    /**
     * 跟进结束时间
     */	
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date endDate
    /**
     * 跟进种类 1,电话拜访  2,上门拜访
     */	
	Integer followKind
    /**
     * 跟进内容
     */	
	String followContent
    /**
     * 跟进结果
     */	
	Integer followResult
    /**
     * 跟进前种类
     */	
	Integer beforeKind
    /**
     * 跟进后种类
     */	
	Integer afterKind
    /**
     * 跟进前阶段
     */	
	Integer beforePhase
    /**
     * 跟进后阶段
     */	
	Integer afterPhase
    /**
     * 签到时间
     */	
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date signonDate
    /**
     * 签退时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date signoffDate
    /**
     * 下次跟进时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date nextDate
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
			viewId:'CustomerFollowList',title:'客户跟进列表',clientType:'pc',viewType:'list',store:'CustomerFollowStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['customer_follow_add','customer_follow_update','customer_follow_view','customer_follow_delete',
            'customer_follow_import','customer_follow_export']//视图对应的操作
		],
        latestCustomerFollowList:[
                viewId:'LatestCustomerFollowList',title:'最新客户跟进',clientType:'pc',viewType:'list',
                ext:[],//扩展参数值最终进入ViewExtend
                opt:[]//视图对应的操作
        ],
		add:[viewId:'CustomerFollowAdd',title:'添加客户跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'CustomerFollowEdit',title:'修改客户跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'CustomerFollowView',title:'查看客户跟进',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'CustomerFollowMadd',title:'添加客户跟进',clientType:'mobile',viewType:'form'],
		medit:[viewId:'CustomerFollowMedit',title:'修改客户跟进',clientType:'mobile',viewType:'form'],
		mview:[viewId:'CustomerFollowMview',title:'查看客户跟进',clientType:'mobile',viewType:'form']
	]
	
	 static list = [
		'customer.name','subject','startDate','endDate','followKind',
		'followResult','afterKind','afterPhase',
		'signonDate','signoffDate','dateCreated'
	 ]

	 static latestCustomerFollowList = [
        'customer.name','subject','startDate'
	 ]

	 static add = [
			'customer.name','contact.name','customer.kind','subject','startDate','endDate',
			'followKind','followContent','followResult','beforeKind',
			'afterKind','beforePhase','afterPhase','files'
	  ]
	 static madd = add
	 
	 static edit = [
         'id','subject','customer.name','customer.kind','contact.name','startDate','endDate',
		 'followKind','followContent','followResult','beforeKind','afterKind',
		 'beforePhase','afterPhase','signonDate','signoffDate','files',
		 'dateCreated','lastUpdated'
	  ]
	 static medit = edit
	 
	 static view = [
		 'customer.id','customer.name','contact.id','id','subject','startDate','endDate',
		 'followKind','followContent','followResult','beforeKind','afterKind','beforePhase',
		 'afterPhase','signonDate','signoffDate','files',
		 'dateCreated','lastUpdated'
	  ]
	 static mview = view 
	 
	 static pub = ['user','employee','customer','contact','files','files1']
	 static allfields = list+add+edit+view+pub

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
			beforeKind:[
				add:[initName:'customer.kind'],
				edit:[initName:'customer.kind'],
				madd:[initName:'customer.kind'],
				medit:[initName:'customer.kind']
			],
			beforePhase:[
				add:[initName:'customer.phase'],
				edit:[initName:'customer.phase'],
				madd:[initName:'customer.phase'],
				medit:[initName:'customer.phase']
			]
    ]
    static constraints = {
    	user nullable: false
        employee nullable: false
        customer nullable: false
        id attributes:[text:'ID',must:true]
        subject attributes:[text:'跟进主题',must:true]
        startDate attributes:[text:'跟进开始时间',must:true]
        endDate attributes:[text:'跟进结束时间',must:true]
        followKind attributes:[text:'跟进种类',dict:9]
        followContent attributes:[text:'跟进内容',must:true]
        followResult attributes:[text:'跟进结果',dict:10,must:true]
        beforeKind attributes:[text:'跟进前种类',dict:1,must:true]
        afterKind attributes:[text:'跟进后种类',dict:1,must:true]
        beforePhase attributes:[text:'跟进前阶段',must:true,dict:2]
        afterPhase attributes:[text:'跟进后阶段',must:true,dict:2]
        signonDate attributes:[text:'签到时间',must:true]
        nextDate attributes:[text:'下次跟进时间',must:true]
        signoffDate attributes:[text:'签退时间',must:true]
        deleteFlag attributes:[text:'删除标志']
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
    	table("t_customer_follow")
    }
}
