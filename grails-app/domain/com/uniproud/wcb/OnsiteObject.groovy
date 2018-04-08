package com.uniproud.wcb

/**
 * 	商机管理
 */
class OnsiteObject extends ExtField{
    static belongsTo = [User,Employee]
    /**
     * follows 跟进集合 <ObjectFollow>
     */
    static hasMany = [follows:ObjectFollow,files:Doc,files1:Doc]
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
     * 商机名称
     */
    String name
    /**
     * 关联客户
     */
    Customer customer
    /**
     * 客户负责联系人
     */
    Contact contact
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
     * 商机经度
     */
    String longtitude
    /**
     * 商机维度
     */
    String latitude
    /**
     * 位置 通过坐标获取的位置
     */
    String location
    /**
     * 地址
     */
    String address
    /**
     * 电话
     */
    String phone
    /**
     * 传真
     */
    String fax
    /**
     * 移动电话
     */
    String mobile
    /**
     * 描述
     */
    String description
    /**
     * 备注信息
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     * 图片，现场图片实景
     */
    Doc pictures
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated
	static V = [
		list:[
			viewId:'OnsiteObjectList',title:'客户列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['onsite_object_add','onsite_object_update','onsite_object_view','onsite_object_delete','onsite_object_follow'
            ,'onsite_object_import','onsite_object_export']//视图对应的操作
			],
		add:[viewId:'OnsiteObjectAdd',title:'添加商机',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'OnsiteObjectEdit',title:'修改商机',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'OnsiteObjectView',title:'查看商机',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'OnsiteObjectMadd',title:'添加商机',clientType:'mobile',viewType:'form'],
		medit:[viewId:'OnsiteObjectMedit',title:'修改商机',clientType:'mobile',viewType:'form'],
		mview:[viewId:'OnsiteObjectMview',title:'查看商机',clientType:'mobile',viewType:'form']
	]
	
	static list = ['name','customer.name','contact.name','address','longtitude','latitude','location','phone']
	
	static add = ['name','customer.name','contact.name','address','phone','mobile','pictures','files']
	static madd = add
	static edit = ['name','customer.name','contact.name','address','phone','mobile','id','pictures','files']
	static medit = edit
	static view = [
					'id','name','customer.id','customer.name','contact.id','contact.name',
					'country','province','city','longtitude','latitude','location','address','phone','mobile','fax',
					'description','remark','employee.name','dateCreated','lastUpdated'
                ]
	static mview = view
	static pub = ['customer','contact','pictures','files','files1','employee']
	
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
            "contact.name":[
                    add:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
                    edit:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
					madd:[xtype:'selectListField',target:'contactSelectList'],
					medit:[xtype:'selectListField',target:'contactSelectList']
            ]
	]
    static constraints = {
        id attributes:[text:'ID',must:true]
        user nullable: false
        employee nullable: false
        customer nulable:true
        contact nullable: true
        pictures nullable: true
        follows nullable: true
        files nullable: true
        files1 nullable: true
        name attributes:[text:'商机名称',must:true,def:true]
        country attributes:[text:'国家']
        province attributes:[text:'省份']
        city attributes:[text:'城市']
        longtitude attributes:[text:'经度',must:true]
        latitude attributes:[text:'维度',must:true]
        location attributes:[text:'位置',must:true]
        address attributes:[text:'地址',must:true]
        phone attributes:[text:'电话',must:true]
        fax attributes:[text:'传真',must:true]
        mobile attributes:[text:'移动电话',must:true]
        description attributes:[text:'描述',must:true]
        remark attributes:[text:'备注信息',must:true]
        deleteFlag attributes:[text:'删除标志',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }
    static mapping = {
        table(name: 't_onsite_object')
    }
}
