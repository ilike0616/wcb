package com.uniproud.wcb

class Product extends ExtField{
    static belongsTo = [User,Employee,ProductKind]
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
     * 部门
     */
    Dept dept
    /**
     * 产品分类
     */
    ProductKind productKind
    /**
     *  规格型号
     */
    String productModel
    /**
     * 产品编号
     */
    String productNo
    /**
     * 产品编号
     */
    String name
    /**
     * 条形码
     */
    String barCode
    /**
     * 长
     */
    Double length
    /**
     *  宽
     */
    Double width
    /**
     * 高
     */
    Double height
    /**
     * 颜色
     */
    String color
    /**
     * 材质
     */
    String materiat
    /**
     * 单位
     */
    Integer unit
    /**
     * 销售价
     */
    Double salePrice
    /**
     * 成本价
     */
    Double costPrice
    /**
     * 产品图片
     */
    Doc photo
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
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
			viewId:'ProductList',title:'产品列表',clientType:'pc',viewType:'list',store:'ContactStore',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['product_add','product_update','product_view','product_delete','product_import','product_export']//视图对应的操作
		],
		add:[viewId:'ProductAdd',title:'添加产品',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'ProductEdit',title:'修改产品',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'ProductView',title:'查看产品',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'ProductMadd',title:'添加产品',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ProductMedit',title:'修改产品',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ProductMview',title:'查看产品',clientType:'mobile',viewType:'form']
		]
	static list = ['name','productNo','length','width','height','unit','salePrice','costPrice']
	
	static add = ['name','productNo','productKind.name','photo','productModel','barCode','unit'
                  ,'salePrice','costPrice','materiat','color','length','width','height','files']
	static madd = add
	static edit = ['id','name','productNo','photo','productKind.name','productModel','barCode','unit'
                   ,'salePrice','costPrice','materiat','color','length','width','height','files']
	static medit = edit
	static view = ['name','productNo','productKind.name','photo','productModel','barCode','unit'
                   ,'salePrice','costPrice','materiat','color','length','width','height'
                   ,'dateCreated','lastUpdated','files','files1']
	static mview = view
    static pub = ['user','employee','productKind']
	
	static allfields = list+add+edit+view+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
            "productKind.name":[
                    add: [xtype: 'baseComboBoxTree',name:'productKind',fieldLabel: '分类',displayField: 'text',rootVisible: false,minPickerHeight: 200,store : 'ProductKindStoreForEdit'],
                    edit: [xtype: 'baseComboBoxTree',name:'productKind',fieldLabel: '分类',displayField: 'text',rootVisible: false,minPickerHeight: 200,store : 'ProductKindStoreForEdit'],
                    madd:[xtype:'selectListField',target:'productKindSelectList'],
                    medit:[xtype:'selectListField',target:'productKindSelectList']
            ]
	]
    static constraints = {
        id attributes:[text:'ID',must:true]
        user nullable:false
        employee nullable:false
        productKind nullable:false
        files nullable:true
        files1 nullable:true
        photo nullable: true
        name maxSize: 255,attributes:[text:'产品名称',must:true]
        productNo attributes:[text:'产品编号',must:true]
        productModel attributes:[text:'产品型号',must:true]
        barCode attributes:[text:'条形码']
        length attributes:[text:'长',scale:2]
        width attributes:[text:'宽',scale:2]
        height attributes:[text:'高',scale:2]
        color attributes:[text:'颜色',scale:2]
        materiat attributes:[text:'材质']
        unit attributes:[text:'单位',dict:17]
        salePrice attributes:[text:'销售价',scale:2]
        costPrice attributes:[text:'成本价',scale:2]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }
    static mapping = {
        table('t_product')
    }
}
