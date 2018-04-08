package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 计划总结
 * @author shqv
 *
 */
class PlanSummary {
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
	 * 主题
	 */
	String subject
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
	 * 计划
	 */
	String plans
	/**
	 * 总结
	 */
	String summarys
    /**
     * 新的建议
     */
    String advice
	/**
	 * 类型 1.日报 2，周报。 3.月报 ， 4.其他
	 */
	Integer type

	/**
	 * 状态  1，暂存    2，已交
	 */
	Integer state
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
		listDay:[viewId:'PlanSummaryDayList',title:'日报列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['plan_summary_day_add','plan_summary_day_update','plan_summary_day_view','plan_summary_day_delete']//视图对应的操作
			],
		addDay:[viewId:'PlanSummaryDayAdd',title:'添加日报',clientType:'pc',viewType:'form'],
		editDay:[viewId:'PlanSummaryDayEdit',title:'修改日报',clientType:'pc',viewType:'form'],
		viewDay:[viewId:'PlanSummaryDayView',title:'查看日报',clientType:'pc',iewType:'form'],
		
		listWeek:[viewId:'PlanSummaryWeekList',title:'周报列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['plan_summary_week_add','plan_summary_week_update','plan_summary_week_view','plan_summary_week_delete']//视图对应的操作
			],
		addWeek:[viewId:'PlanSummaryWeekAdd',title:'添加周报',clientType:'pc',viewType:'form'],
		editWeek:[viewId:'PlanSummaryWeekEdit',title:'修改周报',clientType:'pc',viewType:'form'],
		viewWeek:[viewId:'PlanSummaryWeekView',title:'查看周报',clientType:'pc',iewType:'form'],
		
		listMonth:[viewId:'PlanSummaryMonthList',title:'月报列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['plan_summary_month_add','plan_summary_month_update','plan_summary_month_view','plan_summary_month_delete']//视图对应的操作
			],
		addMonth:[viewId:'PlanSummaryMonthAdd',title:'添加月报',clientType:'pc',viewType:'form'],
		editMonth:[viewId:'PlanSummaryMonthEdit',title:'修改月报',clientType:'pc',viewType:'form'],
		viewMonth:[viewId:'PlanSummaryMonthView',title:'查看月报',clientType:'pc',iewType:'form'],
		
		maddDay:[viewId:'PlanSummaryMaddDay',title:'添加日报',clientType:'mobile',viewType:'form'],
		meditDay:[viewId:'PlanSummaryMeditDay',title:'修改日报',clientType:'mobile',viewType:'form'],
		mviewDay:[viewId:'PlanSummaryMviewDay',title:'查看日报',clientType:'mobile',viewType:'form'],
		maddWeek:[viewId:'PlanSummaryMaddWeek',title:'添加周报',clientType:'mobile',viewType:'form'],
		meditWeek:[viewId:'PlanSummaryMeditWeek',title:'修改周报',clientType:'mobile',viewType:'form'],
		mviewWeek:[viewId:'PlanSummaryMviewWeek',title:'查看周报',clientType:'mobile',viewType:'form'],
		maddMonth:[viewId:'PlanSummaryMaddMonth',title:'添加月报',clientType:'mobile',viewType:'form'],
		meditMonth:[viewId:'PlanSummaryMeditMonth',title:'修改月报',clientType:'mobile',viewType:'form'],
		mviewMonth:[viewId:'PlanSummaryMviewMonth',title:'查看月报',clientType:'mobile',viewType:'form']
		]
	
    static listDay = ['subject','startDate','endDate','summarys','plans','state','employee.name']
    static addDay = ['subject','startDate','endDate','summarys','plans','type']
    static editDay = ['id','subject','startDate','endDate','summarys','plans','dateCreated','lastUpdated']
    static viewDay = [
            'subject','startDate','endDate','summarys','plans','state','type','dateCreated','lastUpdated'
    ]
	static listWeek = ['subject','startDate','endDate','summarys','plans','state','employee.name']
	static addWeek = ['subject','startDate','endDate','summarys','plans','type']
	static editWeek = ['id','subject','startDate','endDate','summarys','plans','dateCreated','lastUpdated']
	static viewWeek = [
			'subject','startDate','endDate','summarys','plans','state','type','dateCreated','lastUpdated'
	]
	static listMonth = ['subject','startDate','endDate','summarys','plans','state','employee.name']
	static addMonth = ['subject','startDate','endDate','summarys','plans','type']
	static editMonth = ['id','subject','startDate','endDate','summarys','plans','dateCreated','lastUpdated']
	static viewMonth = [
			'subject','startDate','endDate','summarys','plans','state','type','dateCreated','lastUpdated'
	]
	static maddDay = ['summarys','plans','type']
	static meditDay = ['summarys','plans']
	static mviewDay = ['subject','summarys','plans','dateCreated']
	static maddWeek = ['summarys','plans','type']
	static meditWeek = ['summarys','plans']
	static mviewWeek = ['subject','summarys','plans','dateCreated']
	static maddMonth = ['summarys','plans','type']
	static meditMonth = ['summarys','plans']
	static mviewMonth = ['subject','summarys','plans','dateCreated']
    static pub = ['user','employee']

    static allfields = listDay+addDay+editDay+viewDay+listWeek+addWeek+editWeek+viewWeek+listMonth+addMonth+editMonth+viewMonth+pub+maddDay+meditDay+mviewDay+maddWeek+meditWeek+mviewWeek+maddMonth+meditMonth+mviewMonth
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
		'type':[
			addDay:[hidden:true],
			addWeek:[hidden:true],
			addMonth:[hidden:true],
			maddDay:[hidden:true],
			maddWeek:[hidden:true],
			maddMonth:[hidden:true],
		],
		'summarys':[
			listDay:[text:'今日总结'],
			listWeek:[text:'本周总结'],
			listMonth:[text:'本月总结'],
			addDay:[text:'今日总结'],
			addWeek:[text:'本周总结'],
			addMonth:[text:'本月总结'],
			editDay:[text:'今日总结'],
			editWeek:[text:'本周总结'],
			editMonth:[text:'本月总结'],
			viewDay:[text:'今日总结'],
			viewWeek:[text:'本周总结'],
			viewMonth:[text:'本月总结'],
			maddDay:[label:'今日总结'],
			maddWeek:[label:'本周总结'],
			maddMonth:[label:'本月总结'],
			meditDay:[label:'今日总结'],
			meditWeek:[label:'本周总结'],
			meditMonth:[label:'本月总结'],
			mviewDay:[label:'今日总结'],
			mviewWeek:[label:'本周总结'],
			mviewMonth:[label:'本月总结']
		],
		'plans':[
			listDay:[text:'明日计划'],
			listWeek:[text:'下周计划'],
			listMonth:[text:'下月计划'],
			addDay:[text:'明日计划'],
			addWeek:[text:'下周计划'],
			addMonth:[text:'下月计划'],
			editDay:[text:'明日计划'],
			editWeek:[text:'下周计划'],
			editMonth:[text:'下月计划'],
			viewDay:[text:'明日计划'],
			viewWeek:[text:'下周计划'],
			viewMonth:[text:'下月计划'],
			maddDay:[label:'明日计划'],
			maddWeek:[label:'下周计划'],
			maddMonth:[label:'下月计划'],
			meditDay:[label:'明日计划'],
			meditWeek:[label:'下周计划'],
			meditMonth:[label:'下月计划'],
			mviewDay:[label:'明日计划'],
			mviewWeek:[label:'下周计划'],
			mviewMonth:[label:'下月计划']
		]
		]
    static constraints = {
        id attributes:[text:'ID',must:true]
        user nullable:false
        employee nullable:true
        subject maxSize: 255,attributes:[text:'主题',must:true]
        startDate attributes:[text:'开始时间',must: true]
        endDate attributes:[text:'结束时间',must: true]
        plans attributes:[text:'计划',must: true]
        summarys attributes:[text:'总结',must: true]
        type attributes:[text:'类型',must: true,dict:18]
        state attributes:[text:'状态',dict:19]
        dateCreated attributes:[text:'创建时间',must: true]
        lastUpdated attributes:[text:'修改时间',must: true]
        deleteFlag attributes:[text:'是否删除']
    }
	
	static mapping = {
		table("t_plan_summary")
		plans type: "text"
		summarys type: "text"
	}
}
