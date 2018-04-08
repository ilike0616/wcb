package com.uniproud.wcb

/**
 * 审核任务表
 * @author shqv
 */
class Audit {
	/**
	 * 审核意见
	 */
	List auditOpinions
	/**
	 * 已经完成审核的审核人,后台程序维护，便于查询
	 */
	List auditors
	static hasMany = [auditOpinions:AuditOpinion,auditors:Employee]
	/**
	 * 所属用户
	 */
	User user
	/**
	 * 审核单据所属员工
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
	 * 关联联系人
	 */
	Contact contact
	/**
	 * 当前,审核人
	 */
	Employee auditor
	/**
	 * 主题
	 */
	String subject
	/**
	 * 审核类型
	 * 1.外出申请
	 * 2.出差申请
	 * 3.请假申请
	 * 4.加班申请
	 * 5.费用报销
	 * 6.财务入账
	 * 7.财务出账
	 * 8.开票
	 */
	Integer type
	/**
	 * 页面中不展示，此字段。
	 * 页面取值方便。
	 * 如：是对外出的审核该字段的值应为 goout
	 * 出差  trip
	 * 请假 leave
	 * 加班  overtime
	 * 费用报销 fareClaims
	 * 财务入账 income
	 * 财务出账 expense
	 * 开票 invoice
	 */
	String qz
	/**
	 * 外出
	 */
	GoOutApply goout
	/**
	 * 出差
	 */
	BusinessTripApply trip
	/**
	 * 请假
	 */
	LeaveApply leave
	/**
	 * 加班
	 */
	OvertimeApply overtime
	/**
	 * 费用报销
	 */
	FareClaims fareClaims
	/**
	 * 财务入账
	 */
	FinanceIncomeExpense income
	/**
	 * 财务出账
	 */
	FinanceIncomeExpense expense
	/**
	 * 开票
	 */
	Invoice invoice
	/**
	 * 审核状态
	 * 1.待审核
	 * 2.审核中
	 * 3.审核通过
	 * 4.审核未通过
	 */
	Integer auditState
	/**
	 * 当前待审
	 */
	AuditOpinion nowAuditOpinion
	/**
	 * 是否领导已评阅
	 */
	Boolean isReview = false
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	static V = [
			auditingList:[viewId:'AuditingList',title:'待审列表',clientType:'pc',viewType:'list',
						  ext:[],//扩展参数值最终进入ViewExtend
						  opt:['work_audit_audit','work_audit_view']//视图对应的操作
			],
			auditedList:[viewId:'AuditedList',title:'已审列表',clientType:'pc',viewType:'list',
						 ext:[],//扩展参数值最终进入ViewExtend
						 opt:['work_audit_view']//视图对应的操作
			],
			auditSendList:[viewId:'AuditSendList',title:'发出列表',clientType:'pc',viewType:'list',
						   ext:[],//扩展参数值最终进入ViewExtend
						   opt:['work_audit_view']//视图对应的操作
			],
			latestAuditList:[viewId:'LatestAuditList',title:'最新审核',clientType:'pc',viewType:'list',
							 ext:[],//扩展参数值最终进入ViewExtend
							 opt:[]//视图对应的操作
			]
	]
	static auditingList = ['subject','type','auditState','dateCreated','lastUpdated','employee.name']
	static latestAuditList = ['subject','type','employee.name','auditState','dateCreated']
	static auditedList = ['subject','type','auditState','dateCreated','lastUpdated','employee.name']
	static auditSendList = ['subject','type','auditState','dateCreated','lastUpdated','employee.name']
	static add = ['subject','type','qz']
	static edit = ['subject','type','qz']
	static view = ['subject','type','qz','auditState','dateCreated','lastUpdated']
	static pub = [
			'user','employee','auditor','customer','contact',
			'auditOpinions','auditors',
			'nowAuditOpinion',
			'goout',
			'trip',
			'leave',
			'overtime',
			'fareClaims',
			'income',
			'expense'
	]

	static allfields = auditingList+auditedList+auditSendList+add+edit+view+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [:]

	static constraints = {
		id attributes:[text:'ID',must:true]
		user nullable:false
		auditor nullable:false
		subject attributes:[text:'主题',must:true]
		type attributes:[text:'审核类型',must:true,dict:21]
		auditState attributes:[text:'审核状态',must:true,dict:23]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
	}

	static mapping = {
		table("t_audit")
	}
}