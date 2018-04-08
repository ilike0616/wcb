package com.uniproud.wcb


class Customer extends ExtField{
    static belongsTo = [User,Employee]
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
     * 分配者
     */
    Employee allocatee
    /**
     * 主联系人
     */
    Contact contact
	/**
	 * logo
	 */
    Doc logo
    /**
     * 客户编号
     */
    String no
    /**
     * 客户名称
     */
    String name
    /**
     * 联系人名称
     */
    String contactName
    /**
     * 别名
     */
    String alias
    /**
     * 客户种类
     */    
    Integer kind
    /**
     * 客户阶段
     */ 
    Integer phase    
    /**
     * 客户等级
     */     
    Integer level
    /**
     * 信用等级
     */     
    Integer creditLevel
    /**
     * 客户来源
     */     
    Integer customerSource
    /**
     * 国家
     */     
    String country
    /**
     * 省份
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
     * 电话
     */     
    String phone
    /**
     * 传真
     */     
    String fax
    /**
     * Email地址
     */     
    String email
    /**
     * 移动电话
     */     
    String mobile
    /**
     * 公司QQ
     */     
    String qq
    /**
     * 微信号
     */    
    String webchat
    /**
     * 网址
     */      
    String webUrl
    /**
     * 开户银行
     */      
    String bankName
    /**
     * 银行账号
     */      
    String bankAccount
    /**
     * 企业税号
     */      
    String taxAccount
    /**
     * 开票名称
     */      
    String invoiceName
    /**
     * 开票地址
     */      
    String invoiceAddress
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
     * 客户分类
     * 1：公海客户；2：我的客户
     */
    Integer customerType
    /**
     * 客户状态
     * 1：未分配；2：申请中；3：已分配；4：回收空闲
     */
    Integer customerState = 1
    /**
     * 分配时间
     */
    Date allocatedDate
    /**
     * 最新跟进时间
     */
    Date latestFollowDate
    /**
     * 最新订购时间
     */
    Date latestOrderDate
    /**
     * 创建时间
     */      
    Date dateCreated    
    /**
     * 修改时间
     */      
    Date lastUpdated
    /**
     * contacts 联系人集合<Contact>
     * follows 跟进集合 <CustomerFollow>
     * files 文件集合
     */
    static hasMany = [contacts:Contact,follows:CustomerFollow,files:Doc,files1:Doc,notes:Note]
    /** 
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
	
	static V = [
		list:[
			viewId:'CustomerList',title:'客户列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['customer_add','customer_update','customer_delete','customer_view',
                 'customer_import','customer_export']//视图对应的操作
			],
        latestCustomerList:[
            viewId:'LatestCustomerList',title:'最新客户',clientType:'pc',viewType:'list',
            ext:[],// 扩展参数值最终进入ViewExtend
            opt:[]// 视图对应的操作
        ],
		add:[viewId:'CustomerAdd',title:'添加客户',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'CustomerEdit',title:'修改客户',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'CustomerView',title:'查看客户',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'CustomerMadd',title:'添加客户',clientType:'mobile',viewType:'form'],
		medit:[viewId:'CustomerMedit',title:'修改客户',clientType:'mobile',viewType:'form'],
		mview:[viewId:'CustomerMview',title:'查看客户',clientType:'mobile',viewType:'form']
	]
    static V1 = [
            listV1:[
                    viewId:'PublicCustomerList',title:'公海客户',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['public_customer_add','public_customer_update','public_customer_delete','public_customer_view',
                         'public_customer_import','public_customer_export','public_customer_allocation',
                         'public_customer_recover','public_customer_apply','public_customer_apply_audit']//视图对应的操作
            ],
            add:[viewId:'PublicCustomerAdd',title:'添加公海客户',clientType:'pc',viewType:'form',viewExtend:[]],
            edit:[viewId:'PublicCustomerEdit',title:'修改公海客户',clientType:'pc',viewType:'form',viewExtend:[]],
            viewV1:[viewId:'PublicCustomerView',title:'查看公海客户',clientType:'pc',viewType:'form',isSearchView:true,viewExtend:[]],
            madd:[viewId:'PublicCustomerMadd',title:'添加公海客户',clientType:'mobile',viewType:'form'],
            medit:[viewId:'PublicCustomerMedit',title:'修改公海客户',clientType:'mobile',viewType:'form'],
            mview:[viewId:'PublicCustomerMview',title:'查看公海客户',clientType:'mobile',viewType:'form']
    ]
    static list = ['name','kind','phase','level','creditLevel','customerSource','employee.name','dateCreated']
    static listV1 = ['name','kind','phase','creditLevel','customerSource','customerState','owner.name','allocatee.name','allocatedDate']
    static latestCustomerList = ['name','employee.name','dateCreated']
  	static add = [
            'name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
            'zipCode','phone','fax','email','mobile','webUrl','files'
            ]    
	static madd = [
            'name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
            'zipCode','phone','fax','email','mobile','webUrl'
            ]    
    static edit = [
            'id','name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
            'zipCode','phone','fax','email','mobile','webUrl','longtitude','latitude','location','files',
            'dateCreated','lastUpdated'
            ]
	static medit = [
            'id','name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
            'zipCode','phone','fax','email','mobile','webUrl','longtitude','latitude','location',
            'dateCreated','lastUpdated'
            ]
    static view = [
    	'id','name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
        'zipCode','phone','fax','email','mobile','webUrl','longtitude','latitude','location','employee.name',
        'files','dateCreated','lastUpdated'
    ]
    static viewV1 = [
            'id','name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
            'zipCode','phone','fax','email','mobile','webUrl','longtitude','latitude','location','employee.name','customerState',
            'owner.name','allocatee.name','allocatedDate','files','dateCreated','lastUpdated'
    ]
	static mview = [
    	'id','name','logo','alias','kind','phase','level','creditLevel','customerSource','country','province','city','address',
        'zipCode','phone','fax','email','mobile','webUrl','longtitude','latitude','location','employee.name',
        'dateCreated','lastUpdated'
    ]
    static pub = ['user','employee','contacts','files','files1']
    static allfields = list+listV1+add+edit+view+viewV1+pub

	//默认插入 userFiledExtend中的参数
    // bitian:定义默认为必填的字段
	static FE = [bitian:['name','kind']]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
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
        id attributes:[text:'ID',must:true]
        user nullable:false
        employee nullable:true,attributes:[text:'创建者']
        owner nullable:true,attributes:[text:'所有者']
        allocatee nullable: true,attributes:[text:'分配者']
        contacts nullable:true,attributes:[text:'联系人']
        name maxSize: 255,attributes:[text:'客户名称',must:true]
        alias attributes:[text:'客户别名']
        no attributes:[text:'客户编号']
        kind attributes:[text:'客户种类',dict:1,must:true]
        phase attributes:[text:'客户阶段',dict:2,must:true]
        level attributes:[text:'客户等级',dict:3]
        creditLevel attributes:[text:'信用等级',dict:4]
        customerSource attributes:[text:'客户来源',dict:5]
        country attributes:[text:'国家']
        province attributes:[text:'省份']
        city attributes:[text:'城市']
        address attributes:[text:'地址']
        zipCode attributes:[text:'邮编']
        phone attributes:[text:'电话']
        fax attributes:[text:'fax']
        email attributes:[text:'Email地址']
        mobile attributes:[text:'移动电话']
        qq attributes:[text:'公司QQ']
        webchat attributes:[text:'微信号']
        webUrl attributes:[text:'网址']
        bankName attributes:[text:'开户银行']
        bankAccount attributes:[text:'银行账号']
        taxAccount attributes:[text:'企业税号']
        invoiceName attributes:[text:'开票名称']
        invoiceAddress attributes:[text:'开票地址']
        remark attributes:[text:'备注']
        longtitude attributes:[text:'经度',must:true]
        latitude attributes:[text:'维度',must:true]
        location attributes:[text:'位置',must:true]
        customerState attributes:[text:'客户状态',dict:33]
        customerType attributes:[text:'客户分类',dict:34]
        allocatedDate attributes:[text:'分配时间']
        latestFollowDate attributes:[text:'最新跟进时间',must:true]
        latestOrderDate attributes:[text:'最新订购时间',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
        deleteFlag attributes:[text:'删除标记']
    }
    static mapping = {
        table(name:'t_customer')
    }

}
