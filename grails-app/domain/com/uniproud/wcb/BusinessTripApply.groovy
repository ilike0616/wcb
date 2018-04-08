package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 出差申请
 * @author shqv
 */
class BusinessTripApply extends ExtField{
	static belongsTo = [User,Employee]
	/**
	 * 拍照photos
	 * audits 审核记录
	 */
	static hasMany = [photos:Doc]
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
	 * 所有者
	 */
	Employee owner
	/**
	 * 和审核任务表的关联
	 * audit.auditOpinion 可查看审核意见列表
	 * audit.nowAuditOpinion 当前待审卡点
	 * audit.auditState 审核进度  1.待审核  2.审核中3.审核通过  4.审核未通过
	 */
	Audit audit
	/**
	 * 主题
	 */
	String subject
	/**
	 * 客户
	 */
	Customer customer
	/**
	 * 联系人
	 */
	Contact contact
	/**
	 * 目的地
	 */
	String aimAddress
	/**
	 * 出差费用
	 */
	Double fare
	/**
	 * 出差事由
	 */
	String content
	/**
	 * 开始时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date startDate
	/**
	 * 结束时间
	 */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
	Date endDate
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated
	
	/**
	 * 当前审核人
	 */
	Employee auditor
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	static V = [
		list:[viewId:'BusinessTripApplyList',title:'出差申请列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['business_trip_apply_add','business_trip_apply_update','business_trip_apply_view','business_trip_apply_delete'
                 ,'business_trip_apply_opinion','business_trip_apply_export']//视图对应的操作
			],
		add:[viewId:'BusinessTripApplyAdd',title:'添加出差申请',clientType:'pc',viewType:'form'],
		edit:[viewId:'BusinessTripApplyEdit',title:'修改出差申请',clientType:'pc',viewType:'form'],
		view:[viewId:'BusinessTripApplyView',title:'查看出差申请',clientType:'pc',viewType:'form'],
		madd:[viewId:'BusinessTripApplyMadd',title:'添加出差申请',clientType:'mobile',viewType:'form'],
		medit:[viewId:'BusinessTripApplyMedit',title:'修改出差申请',clientType:'mobile',viewType:'form'],
//		mview:[viewId:'BusinessTripApplyMview',title:'查看出差申请',clientType:'mobile',viewType:'form']
		]
	static list = ['audit.id','audit.auditState','auditor.name','subject','aimAddress','fare','content','startDate','endDate','dateCreated',
					'customer.id','customer.name','contact.id','contact.name']
	static add = ['subject','aimAddress','fare','content','startDate','endDate','auditor.name',
					'customer.name','contact.name']
	static edit = ['id','subject','aimAddress','fare','content','startDate','endDate','auditor.id','auditor.name',
				   'customer.id','customer.name','contact.id','contact.name']
	static view = ['subject','aimAddress','fare','content','startDate','endDate','dateCreated','lastUpdated',
					'customer.id','customer.name','contact.id','contact.name']
	static madd = ['subject','aimAddress','fare','content','startDate','endDate','auditor.name',
					'customer.name','contact.name','photos']
	static medit = ['id','subject','aimAddress','fare','content','startDate','endDate','auditor.name',
					'customer.name','contact.name','photos']
//	static mview = ['subject','aimAddress','fare','content','startDate','endDate','auditor.name',
//					'customer.name','contact.name','photos','dateCreated','lastUpdated']
	static pub = ['user','employee','audit','photos']

	static allfields = list+add+edit+view+pub+madd+medit
	//默认插入 userFiledExtend中的参数
	static FE = [
			bitian:['subject','aimAddress','content','startDate','endDate','auditor.name']
	]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		"content":[
			add:[xtype:'textareafield',grow:true],
			edit:[xtype:'textareafield',grow:true]
		],
		"auditor.name":[
			list:[text:'审核人'],
			add:[fieldLabel:'审核人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'auditor'],
			edit:[fieldLabel:'审核人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'auditor'],
			madd:[xtype:'selectListField',target:'employeeSelectList',label:'审核人'],
			medit:[xtype:'selectListField',target:'employeeSelectList',label:'审核人']
		],
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
		'photos':[
			list:[label:'照片'],
			add:[label:'拍照'],
			edit:[label:'拍照'],
			view:[label:'照片'],
			madd:[label:'拍照'],
			medit:[label:'拍照']
		]
	]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		audit nullable:false
		subject attributes:[text:'主题',must:true]
		aimAddress attributes:[text:'目的地',must:true]
		fare attributes:[text:'出差费用',must:true,scale:2]
		content attributes:[text:'出差事由',must:true]
		startDate attributes:[text:'开始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_business_trip_apply')
	}
}
