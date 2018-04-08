package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 请假申请
 * @author shqv
 */
class LeaveApply extends ExtField{
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
	 * 请假类型
	 * 1.事假
	 * 2.婚嫁
	 * 3.产假
	 * 4.病假
	 */
	Integer type
	/**
	 * 请假方式
	 * 1.天
	 * 2.小时
	 */
	Integer timeType
	/**
	 * 主题
	 */
	String subject
	/**
	 * 请假事由
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
	 * 和审核任务表的关联
	 * audit.auditOpinion 可查看审核意见列表
	 * audit.nowAuditOpinion 当前待审卡点
	 * audit.auditState 审核进度  1.待审核  2.审核中3.审核通过  4.审核未通过
	 */
	Audit audit
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
		list:[viewId:'LeaveApplyList',title:'请假申请列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['leave_apply_add','leave_apply_update','leave_apply_view','leave_apply_delete','leave_apply_opinion'
                ,'leave_apply_export']//视图对应的操作
			],
		add:[viewId:'LeaveApplyAdd',title:'添加请假申请',clientType:'pc',viewType:'form'],
		edit:[viewId:'LeaveApplyEdit',title:'修改请假申请',clientType:'pc',viewType:'form'],
		view:[viewId:'LeaveApplyView',title:'查看请假申请',clientType:'pc',viewType:'form'],
		madd:[viewId:'LeaveApplyMadd',title:'添加请假申请',clientType:'mobile',viewType:'form'],
		medit:[viewId:'LeaveApplyMedit',title:'修改请假申请',clientType:'mobile',viewType:'form']
//		mview:[viewId:'LeaveApplyMview',title:'查看请假申请',clientType:'mobile',viewType:'form']
		]
	static list = ['audit.auditState','auditor.name','type','timeType','subject','content','startDate','endDate','dateCreated']
	static add = ['type','timeType','subject','content','startDate','endDate','auditor.id','auditor.name']
	static edit = ['id','type','timeType','subject','content','startDate','endDate','auditor.id','auditor.id','auditor.name']
	static view = ['type','timeType','subject','content','startDate','endDate','auditor.name','dateCreated','lastUpdated']
	static madd = ['subject','type','content','startDate','endDate','auditor.id','auditor.name','photos']
	static medit = ['id','subject','type','content','startDate','endDate','auditor.id','auditor.name','photos']
//	static mview = ['subject','type','content','startDate','endDate','auditor.id','auditor.name','photos','dateCreated','lastUpdated']
	static pub = ['user','employee','audit']

	static allfields = list+add+edit+view+pub+madd+medit
	//默认插入 userFiledExtend中的参数
	static FE = [
			bitian:['type','timeType','subject','content','startDate','endDate','auditor.name']
	]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
			"auditor.name":[
				add:[fieldLabel:'审核人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'auditor'],
				edit:[fieldLabel:'审核人',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'auditor'],
				madd:[xtype:'selectListField',target:'employeeSelectList',label:'审核人'],
				medit:[xtype:'selectListField',target:'employeeSelectList',label:'审核人']
			]
		]
	
    static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		employee nullable:false
		audit nullable:false
		subject attributes:[text:'主题',must:true]
		type attributes:[text:'请假方式',must:true,dict:25]
		timeType attributes:[text:'请假类型',must:true,dict:24]
		content attributes:[text:'请假事由',must:true]
		startDate attributes:[text:'开始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	static mapping = {
		table('t_leave_apply')
	}
}
