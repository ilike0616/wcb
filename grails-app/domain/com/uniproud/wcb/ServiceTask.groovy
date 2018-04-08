package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class ServiceTask extends ExtField{
    static belongsTo = [User,Employee]
    /**
     * photos 现场拍照<OutsiteRecord>
     */
    static hasMany = [photos:OutsiteRecord,files:Doc,files1:Doc]
	/**
	 * 现成签到
	 */
	OutsiteRecord signon
	/**
	 * 现成签到
	 */
	OutsiteRecord signout
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
     * 关联客户
     */
    Customer customer
    /**
     * 客户负责联系人
     */
    Contact contact
    /**
     * 产品
     */
    Product product
    /**
     * 主题
     */
    String subject
    /**
     * 任务编号
     */
    String taskNo
    /**
     * 条形码，表示任务的唯一码，用于快递单等方便调用
     */
    String barcode
    /**
     * 可用数据字典表示
     */
    Integer taskKind
    /**
     * 期望开始时间
     */
    Date expectStartDate
    /**
     * 期望结束时间
     */
    Date expectEndDate
    /**
     * 实际开始时间
     */
    Date realStartDate
    /**
     * 实际结束时间
     */
    Date realEndDate
    /**
     * 任务地址
     */
    String taskAddress
    /**
     * 任务描述
     */
    String taskDesc
    /**
     * 任务内容
     */
    String taskContent
    /**
     * 任务结果
     */
    Integer taskResult
    /**
     * 任务状态
     * 0-待处理；1-处理中；2-已处理
     */
    Integer taskState
    /**
     * 备注信息
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
    /**
     *签到时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date signonDate
    /**
     *签退时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date signoffDate
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
			viewId:'ServiceTaskList',title:'服务派单',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['service_task_add','service_task_update','service_task_view','service_task_delete'
                 ,'service_task_import','service_task_export']//视图对应的操作
		],
		latestServiceTaskList:[
			viewId:'LatestServiceTaskList',title:'最新服务派单',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:[]//视图对应的操作
		],
		add:[viewId:'ServiceTaskAdd',title:'添加服务派单',clientType:'pc',viewType:'form',viewExtend:[]],
		edit:[viewId:'ServiceTaskEdit',title:'修改服务派单',clientType:'pc',viewType:'form',viewExtend:[]],
		view:[viewId:'ServiceTaskView',title:'查看服务派单',clientType:'pc',viewType:'form',viewExtend:[]],
		madd:[viewId:'ServiceTaskMadd',title:'添加服务派单',clientType:'mobile',viewType:'form'],
		medit:[viewId:'ServiceTaskMedit',title:'修改服务派单',clientType:'mobile',viewType:'form'],
		mview:[viewId:'ServiceTaskMview',title:'查看服务派单',clientType:'mobile',viewType:'form']
	]
	static list = ['customer.name','taskNo','subject','expectStartDate','expectEndDate','taskKind',
				'taskResult','taskState','signonDate','signoffDate','dateCreated']

	static latestServiceTaskList = [
            'customer.name','taskNo','subject','expectStartDate','expectEndDate','taskDesc','dateCreated'
    ]

	static add = [
            'subject','taskNo','customer.name','contact.name','expectStartDate','expectEndDate',
			'taskContent','taskDesc','taskAddress','taskKind','taskState','remark','files'
    ]
	static madd = add
	static edit = [
            'id','subject','taskNo','customer.name','contact.name','expectStartDate','expectEndDate',
		 	'realStartDate','realEndDate','taskContent','taskDesc','taskAddress','taskKind',
			'taskResult','taskState','remark','dateCreated','lastUpdated','files']
	static medit = edit
	static view = [
            'id','taskNo','subject','customer.id','customer.name','contact.id','contact.name','product.id','product.name',
            'expectStartDate','expectEndDate','realStartDate','realEndDate','taskContent','taskDesc','taskAddress','taskKind',
			'taskResult','taskState','remark','signonDate','signoffDate','dateCreated','lastUpdated','files'
    ]
	static mview = view
    static pub = ['user','employee','photos','files','files1','customer','contact','product']
	static allfields = list+add+edit+view+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
            "customer.name":[
                    add:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer'],
                    edit:[xtype:'baseSpecialTextfield',store:'CustomerStore',viewId:'CustomerList',hiddenName:'customer']
            ],
            "contact.name":[
                    add:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer'],
                    edit:[xtype:'baseSpecialTextfield',store:'ContactStore',viewId:'ContactList',hiddenName:'contact',paramName : 'customer']
            ]
	]
    static constraints = {
        user nullable: false
        employee nullable: false
        customer nulable:true
        contact nullable: true
		id attributes:[text:'ID',must:true]
		subject attributes:[text:'服务主题',must:true]
		taskNo attributes:[text:'任务编号',must:true]
		barcode attributes:[text:'条形码',must:true]
		taskKind attributes:[text:'任务类型',must:true,dict:13]
		expectStartDate attributes:[text:'期望开始时间',must:true]
		expectEndDate attributes:[text:'期望结束时间',must:true]
		realStartDate attributes:[text:'实际开始时间',must:true]
		realEndDate attributes:[text:'实际结束时间',must:true]
		taskAddress attributes:[text:'任务地址',must:true]
		taskDesc attributes:[text:'任务描述',must:true]
		taskContent attributes:[text:'任务内容',must:true]
		taskResult attributes:[text:'任务结果',must:true,dict:14]
		taskState attributes:[text:'任务状态',must:true,dict:15]
		remark attributes:[text:'备注',must:true]
		deleteFlag attributes:[text:'删除标志']
		signonDate attributes:[text:'签到时间',must:true]
		signoffDate attributes:[text:'签退时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_service_task')
    }
}
