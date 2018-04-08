package com.uniproud.wcb

/**
 * 审核意见表
 * @author shqv
 */
class AuditOpinion {
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
	 * 审核人
	 */
	Employee auditor
	/**
	 * 指定下次审核人
	 */
	Employee nextAuditor
	/**
	 * 隶属任何任务
	 */
	Audit audit
	/**
	 * 审核状态
	 * 1。未审核
	 * 2。通过
	 * 3。通过，继续流转
	 * 4。未通过
	 */
	Integer state
	/**
	 * 审核意见
	 */ 
	String content
	/**
	 * photos 照片
	 */
	static hasMany = [photos:Doc]
	
	Doc photo
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
		list:[viewId:'AuditOpinionList',title:'审核意见',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:[]//视图对应的操作
		],
		edit:[viewId:'AuditOpinionEdit',title:'审核',clientType:'pc',viewType:'form']
		]
	static list = ['auditor.name','state','content','dateCreated']
//	static add = ['state','content']
	static edit = ['id','state','content','nextAuditor.name']
//	static view = ['state','content','dateCreated','lastUpdated']
	static pub = ['id','user','employee','auditor','photos']

	static allfields = list+edit+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		'auditor.name':[
			list:[text:'审核人']
		],
		"content":[
			edit:[xtype:'textareafield',grow:true]
		],
		"nextAuditor.name":[
			edit:[fieldLabel:'审核人',disabled:'true',xtype:'baseSpecialTextfield',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'nextAuditor']
		],
	]
	
    static constraints = {
		user nullable:false
		employee nullable:false
		auditor nullable:false
		audit nullable:false
		state attributes:[text:'审核状态',must:true,dict:22]
		content attributes:[text:'审核意见',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_audit_opinion')
	}
}
