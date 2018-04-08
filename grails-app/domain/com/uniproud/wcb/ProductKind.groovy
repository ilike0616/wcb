package com.uniproud.wcb

class ProductKind extends ExtField{
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
	 * 部门
	 */
	Dept dept
    /**
     * 父分类种子，为产品分类树的生成做准备
     */
    ProductKind parentKind
    /**
     * 分类名称
     */
    String name
    /**
     * 分类类型，为以后扩展用
     */
    Integer kindType
    /**
     * children 产品分类的子元素
     */
    static hasMany = [children : ProductKind,files:Doc,files1:Doc]
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	static V = [
		list:[
			viewId:'ProductKindList',title:'产品分类列表',clientType:'pc',viewType:'list',store:'ContactStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['product_kind_add','product_kind_update','product_kind_view','product_kind_delete'
                ,'product_kind_import','product_kind_export']//视图对应的操作
		],
		add:[viewId:'ProductKindAdd',title:'添加产品分类',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'ProductKindEdit',title:'修改产品分类',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'ProductKindView',title:'查看产品分类',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'ProductKindMadd',title:'添加产品分类',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ProductKindMedit',title:'修改产品分类',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ProductKindMview',title:'查看产品分类',clientType:'mobile',viewType:'form']
	]
	static list = ['name','kindType','lastUpdated','dateCreated']
	
	static add = ['name','kindType','parentKind.name']
	static madd = add
	static edit = ['id','name','kindType','parentKind.name']
	static medit = edit
	static view = ['id','name','kindType','lastUpdated','dateCreated','parentKind.name','parentKind.id','employee.name','employee.id']
	static mview = view
    static pub = ['user','parentKind','children','files','files1']
	
	static allfields = list+add+edit+view+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
			name:[
				list:[xtype:'treecolumn']
			],
            "files":[
                    add:[xtype:'baseUploadField',fieldLabel:'附件',colspan:2,hiddenName: 'files'],
                    edit:[xtype:'baseUploadField',fieldLabel:'附件',colspan:2,hiddenName: 'files']
            ],
            "files1":[
                    add:[xtype:'baseUploadField',fieldLabel:'附件2',colspan:2,hiddenName: 'files1'],
                    edit:[xtype:'baseUploadField',fieldLabel:'附件2',colspan:2,hiddenName: 'files1']
            ],
            "parentKind.name":[
                    add: [xtype: 'baseComboBoxTree',fieldLabel: '上级分类',displayField: 'text',name:'parentKind',rootVisible: false,minPickerHeight: 200,store : 'ProductKindStoreForEdit'],
                    edit: [xtype: 'hiddenfield',name:'parentKind']
            ]
	]
    static constraints = {
        id attributes:[text:'ID',must:true]
        user nullable:false
        parentKind nullable:true
        children nullable:true
        files nullable:true
        files1 nullable:true
        name maxSize: 255,attributes:[text:'分类名称',must:true]
        kindType attributes:[text:'分类类别',must: true,dict : 16]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_product_kind')
    }
}
