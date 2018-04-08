package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class Contact extends ExtField{
	static belongsTo = [User,Employee,Customer,CustomerFollow]
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
	/*
	 *所属客户
	 */
	Customer customer
	/*
	 *联系人名称
	 */
	String name
	/**
	 * 联系人类型.2:次联系人,1:主联系人
	 */
	Integer kind
	/*
	 *1:男,2:女
	 */
	Integer gender
	/*
	 *称谓
	 */
	String appellAtion
	/**
	 *传真
	 */
	String fax
	/**
	 *固定电话1
	 */
	String phone1
	/**
	 *固定电话2
	 */
	String phone2
	/**
	 *联系人手机1
	 */
	String mobile1
	/**
	 *联系人手机2
	 */
	String mobile2
	/**
	 * Email
	 */
	String email1
	/**
	 * Email
	 */
	String email2
	/**
	* QQ 号码
	 */
	String qq
	/**
	 * 微信号
	 */
	String webchat
	/**
	 * 国家
	 */
	String country
	/**
	 * 省
	 */
	String province
	/**
	 * 城市
	 */
	String city
	/**
	 * 地址
	 */
	String address
	/**
	 * 邮编
	 */
	String zipCode

	/**
	 * 1:身份证,2:营业执照,3:其它
	 */
	Integer certificateKind
	/**
	 * 证件编号
	 */
	String certificateNo
	/**
	 * Blog
	 */
	String blog
	/**
	 * 出生日期
	 */
	@BindingFormat('yyyy-MM-dd')
	Date birthday
	/**
	 * 农历生日
	 */
	String lunarBirthDay
	/**
	 * 备注
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

	/**
	* follows 客户跟进记录 对应 CustomerFollow
	*/
	static hasMany = [follows:CustomerFollow,files:Doc,files1:Doc]
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	
	static V = [
		list:[
			viewId:'ContactList',title:'联系人列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['contact_add','contact_update','contact_view','contact_delete',
            'contact_import','contact_export']//视图对应的操作
		],
		add:[viewId:'ContactAdd',title:'添加联系人',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'ContactEdit',title:'修改联系人',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'ContactView',title:'查看联系人',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'ContactMadd',title:'添加联系人',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ContactMedit',title:'修改联系人',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ContactMview',title:'查看联系人',clientType:'mobile',viewType:'form']
	   ]
	static list = [
			'customer.name','name','kind','phone1','mobile1','email1','birthday','dateCreated'
		  ]
	
	static add = [
			    'customer.name','name','kind','gender','appellAtion','fax','phone1','mobile1',
			  'email1','email2','birthday','lunarBirthDay','qq','webchat','blog',
			  'country','province','city','address','zipCode','certificateKind','certificateNo','remark',
			  'files'
		  ]
	static madd  = add
	static edit = [
			    'customer.name','id','name','kind','gender','appellAtion','fax','phone1','mobile1',
			  'email1','email2','birthday','lunarBirthDay','qq','webchat','blog','country','province','city',
			  'address','zipCode','certificateKind','certificateNo','remark','files', 'dateCreated','lastUpdated'
		  ]
	static medit  = edit
	static view = [
			  'customer.name','customer.id','id','name','kind','gender','appellAtion','fax','phone1','mobile1',
			  'email1','email2','birthday','lunarBirthDay','qq','webchat','blog','country','province','city',
			  'address','zipCode','certificateKind','certificateNo','remark','files','dateCreated','lastUpdated'
		  ]
	static mview  = view
	
	static pub = ['user','employee','customer','files','files1','employee.name']
	static allfields = list+add+edit+view+pub
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"customer.name":[
				add:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
				edit:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
				madd:[xtype:'selectListField',target:'customerSelectList'],
				medit:[xtype:'selectListField',target:'customerSelectList']
		],
		"country":[
				add:[xtype:'baseCascadeCombo',storeKind:1],
				edit:[xtype:'baseCascadeCombo',storeKind:1]
		],
		"province":[
				add:[xtype:'baseCascadeCombo',storeKind:2],
				edit:[xtype:'baseCascadeCombo',storeKind:2]
		],
		'city':[
				add:[xtype:'baseCascadeCombo'],
				edit:[xtype:'baseCascadeCombo']
		]
	]
	
	static constraints = {
		user nullable: false
		employee nullable: true
		customer nullable: false
		id attributes:[text:'ID',must:true]
		name attributes:[text:'联系人名称',must:true]
		kind attributes:[text:'联系人类型',dict:6,must:true]
		gender attributes:[text:'性别',dict:7,must:true]
		appellAtion attributes:[text:'称谓']
		fax attributes:[text:'传真']
		phone1 attributes:[text:'固定电话',must:true]
		phone2 attributes:[text:'固定电话2']
		mobile1 attributes:[text:'联系人手机',must:true]
		mobile2 attributes:[text:'联系人手机2']
		email1 attributes:[text:'Email',must:true]
		email2 attributes:[text:'Email2']
		qq attributes:[text:'qq']
		webchat attributes:[text:'微信号']
		country attributes:[text:'国家',must:true]
		province attributes:[text:'省',must:true]
		city attributes:[text:'城市',must:true]
		address attributes:[text:'地址']
		zipCode attributes:[text:'邮编']
		certificateKind attributes:[text:'证件类型',dict:8]
		certificateNo attributes:[text:'证件编号']
		blog attributes:[text:'Blog']
		birthday attributes:[text:'出生日期',must:true]
		lunarBirthDay attributes:[text:'农历生日']
		remark attributes:[text:'备注',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
	}

	static mapping={
		  table(name:'t_contact')
	}
	
}
