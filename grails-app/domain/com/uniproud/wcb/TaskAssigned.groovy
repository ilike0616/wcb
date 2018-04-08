package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 任务交办
 * @author shqv
 */
class TaskAssigned extends ExtField{
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
	 * 任务内容
	 */
	String taskContent
	/**
	 * 关联客户
	 */
	Customer customer
	/**
	 * executor 执行者，一个任务可以选择多个执行者
	 * cc 抄送 可以抄送多人
	 * photos:拍照照片
	 */
	List comments
	List executor
	List cc
	static hasMany = [executor:Employee,cc:Employee,photos:Doc,comments:Comment]

    Comment lastComment
	/**
	 * 完成时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date finishedDate
	
	/**
	 * 任务状态  
	 * 1，进行中 ，2，完成，3，关闭
	 */
	Integer state = 1
	/**
	 * 是否领导已评阅
	 */
	Boolean isReview = false
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
			list:[viewId:'TaskAssignedList',title:'任务交办列表',clientType:'pc',viewType:'list',
				ext:[],//扩展参数值最终进入ViewExtend
				opt:['task_assigned_add','task_assigned_update','task_assigned_view','task_assigned_delete','task_assigned_reply','task_assigned_report']//视图对应的操作
				],
			add:[viewId:'TaskAssignedAdd',title:'添加任务交办',clientType:'pc',viewType:'form'],
			edit:[viewId:'TaskAssignedEdit',title:'修改任务交办',clientType:'pc',viewType:'form'],
			view:[viewId:'TaskAssignedView',title:'查看任务交办',clientType:'pc',viewType:'form'],
			madd:[viewId:'TaskAssignedMadd',title:'添加任务交办',clientType:'mobile',viewType:'form'],
			medit:[viewId:'TaskAssignedMedit',title:'修改任务交办',clientType:'mobile',viewType:'form']
			//mview:[viewId:'TaskAssignedMview',title:'查看任务交办',clientType:'mobile',viewType:'form']
		]
	static list = ['customer.name','taskContent','finishedDate','state','dateCreated','executor.name','cc.name','employee.photo']
	static add = ['customer.name','taskContent','executor.name','cc.name','finishedDate','photos']
    static edit = ['id','customer.name','taskContent','executor.name','cc.name','finishedDate','photos']
    static view = ['id','customer.id','customer.name','taskContent','executor.name','cc.name',
				'finishedDate','state','dateCreated','lastUpdated','comments']
	static madd = ['customer.name','taskContent','executor.name','cc.name','finishedDate','photos']
	static medit = ['customer.name','taskContent','executor.name','cc.name','finishedDate','photos']
	//static mview = ['customer.name','taskContent','state','executor.name','cc.name','finishedDate','photos']
    static pub = ['user','creator','executor','cc','photos','comments','customer','employee']
    static allfields = list+add+edit+view+pub+madd+medit
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
		'executor.name':[
			add:[fieldLabel:'执行人',xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'executor',colspan:2],
			edit:[fieldLabel:'执行人',xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'executor',colspan:2],
			view:[fieldLabel:'执行人',xtype:'textarea',colspan:2,width: 500],
			madd:[xtype:'selectListField',target:'employeeSelectList',label:'执行者',selectMode:'MULTI'],
			medit:[xtype:'selectListField',target:'employeeSelectList',label:'执行者',selectMode:'MULTI']
		],
		'cc.name':[
			add:[fieldLabel:'抄送',xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'cc',colspan:2],
			edit:[fieldLabel:'抄送',xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'cc',colspan:2],
			view:[fieldLabel:'抄送',xtype:'textarea',colspan:2,width: 500],
			madd:[xtype:'selectListField',target:'employeeSelectList',label:'抄送',selectMode:'MULTI'],
			medit:[xtype:'selectListField',target:'employeeSelectList',label:'抄送',selectMode:'MULTI']
		],
		'taskContent':[
			add:[xtype:'textareafield',grow:true,colspan:2,width:500],
			edit:[xtype:'textareafield',grow:true,colspan:2,width:500]
		],
		'comments':[
			view:[fieldLabel:'回复',xtype:'taskAssignedComment']
		],
		'photos':[
			add:[fieldLabel:'附件'],
			edit:[fieldLabel:'附件'],
			madd:[label:'拍照'],
			medit:[label:'拍照']
			]
		]
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable: false
		employee nullable: false
		customer nullable:true
		taskContent attributes:[text:'任务内容',must:true]
		finishedDate attributes:[text:'完成时间',must:true]
		state attributes:[text:'任务状态',must:true,dict:20]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table("t_task_assignde")
	}
}
