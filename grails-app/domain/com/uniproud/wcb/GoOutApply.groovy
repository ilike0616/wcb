package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 外出申请
 * @author shqv
 */
class GoOutApply extends ExtField{
	static belongsTo = [User,Employee]
	/**
	 * 拍照photos
	 * audits 审核记录
	 */
	static hasMany = [photos:Doc]
	
	Doc photo
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
	 * 事由
	 */
	String content
	/**
	 * 关联客户
	 */
	Customer customer
	/**
	 * 联系人
	 */
	Contact contact
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
		list:[viewId:'GoOutApplyList',title:'外出申请列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['goout_apply_add','goout_apply_update','goout_apply_view','goout_apply_delete','goout_apply_opinion'
                ,'goout_apply_export']//视图对应的操作
			],
		add:[viewId:'GoOutApplyAdd',title:'添加外出申请',clientType:'pc',viewType:'form'],
		edit:[viewId:'GoOutApplyEdit',title:'修改外出申请',clientType:'pc',viewType:'form'],
		view:[viewId:'GoOutApplyView',title:'查看外出申请',clientType:'pc',viewType:'form'],
		madd:[viewId:'GoOutApplyMadd',title:'添加外出申请',clientType:'mobile',viewType:'form'],
		medit:[viewId:'GoOutApplyMedit',title:'修改外出申请',clientType:'mobile',viewType:'form'],
//		mview:[viewId:'GoOutApplyMview',title:'查看外出申请',clientType:'mobile',viewType:'form']
		]
	static list = ['audit.id','audit.auditState','auditor.name','subject','content','startDate','endDate','dateCreated',
				'customer.name','contact.name']
	static add = ['subject','content','startDate','endDate','auditor.name',
				'customer.name','contact.name']
	static edit = ['id','subject','content','startDate','endDate','auditor.id','auditor.name',
				   'customer.id','customer.name','contact.id','contact.name','photos']
	static view = ['subject','content','startDate','endDate','dateCreated','lastUpdated',
				'customer.id','customer.name','contact.id','contact.name']
	static madd = ['subject','content','startDate','endDate','customer.name','contact.name','auditor.name','photos']
	static medit = ['id','subject','content','startDate','endDate','customer.name','contact.name','auditor.name','photos']
//	static mview = ['subject','content','startDate','endDate','customer.name','contact.name','auditor.name','audit.auditState','photos','dateCreated','lastUpdated']
	static pub = ['user','employee','audit','photos']

	static allfields = list+add+edit+view+pub+madd+medit
	//默认插入 userFiledExtend中的参数
	static FE = [
			bitian:['subject','content','startDate','endDate','auditor.name']
	]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
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
		content attributes:[text:'事由',must:true]
		startDate attributes:[text:'开始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_go_out_apply')
	}
}
