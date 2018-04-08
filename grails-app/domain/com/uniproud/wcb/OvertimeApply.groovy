package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 加班申请
 * @author shqv
 */
class OvertimeApply extends ExtField{
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
	 * 主题
	 */
	String subject
	/**
	 * 加班类型
	 * 1.普通加班
	 * 2.周末加班
	 * 3.节假日加班
	 */
	Integer type
	/**
	 * 加班地点
	 * 1.公司
	 * 2.非加班
	 */
	Integer place
	/**
	 * 加班理由
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
		list:[viewId:'OvertimeApplyList',title:'加班申请列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['overtime_apply_add','overtime_apply_update','overtime_apply_view','overtime_apply_delete'
                 ,'overtime_apply_opinion','overtime_apply_export']//视图对应的操作
			],
		add:[viewId:'OvertimeApplyAdd',title:'添加加班申请',clientType:'pc',viewType:'form'],
		edit:[viewId:'OvertimeApplyEdit',title:'修改加班申请',clientType:'pc',viewType:'form'],
		view:[viewId:'OvertimeApplyView',title:'查看加班申请',clientType:'pc',viewType:'form'],
		madd:[viewId:'OvertimeApplyMadd',title:'添加加班申请',clientType:'mobile',viewType:'form'],
		medit:[viewId:'OvertimeApplyMedit',title:'修改加班申请',clientType:'mobile',viewType:'form'],
//		mview:[viewId:'OvertimeApplyMview',title:'查看加班申请',clientType:'mobile',viewType:'form']
		]
	static list = ['audit.id','audit.auditState','auditor.name','subject','type','place','content','startDate','endDate','dateCreated']
	static add = ['subject','type','place','content','startDate','endDate','auditor.name']
	static edit = ['id','subject','type','place','content','startDate','endDate','auditor.id','auditor.name']
	static view = ['subject','type','place','content','startDate','endDate','dateCreated','lastUpdated']
	static madd = ['subject','type','place','content','startDate','endDate','auditor.name','photos']
	static medit = ['id','subject','type','place','content','startDate','endDate','auditor.name','photos']
//	static mview = ['subject','type','place','content','startDate','endDate','auditor.name','photos','dateCreated','lastUpdated']
	static pub = ['user','employee','audit']

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
		type attributes:[text:'加班类型',must:true,dict:26]
		place attributes:[text:'加班地点',must:true,dict:27]
		content attributes:[text:'加班事由',must:true]
		startDate attributes:[text:'开始时间',must:true]
		endDate attributes:[text:'结束时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_overtime_apply')
	}
}
