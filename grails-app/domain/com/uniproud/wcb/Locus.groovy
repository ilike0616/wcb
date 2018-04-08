package com.uniproud.wcb

import java.util.Date;

class Locus {
	
	static belongsTo = [User,Employee]
	/**
	 * 所属用户
	 */
	User user
	/**
	 * 员工
	 */
	Employee employee
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 开始时间
	 */
	Date startDate
	/**
	 * 最新时间
	 */
	Date latestDate
	/**
	 * 位置 通过坐标获取的位置
	 */
	String location
	/**
	 * 对象经度
	 */
	String longtitude
	/**
	 * 对象维度
	 */
	String latitude
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
	
	static hasMany = [detail:LocusDetail]
	
	static V = [
		list:[viewId:'LocusList',title:'员工轨迹列表',clientType:'pc',viewType:'list',
			ext:[],//扩展参数值最终进入ViewExtend
			opt:['locus_view','locus_location','locus_path']//视图对应的操作
			],
		view:[viewId:'LocusView',title:'查看',clientType:'pc',viewType:'form',viewExtend:[]],
		]
	
	static list = ['id','employee.id','employee.name','location','longtitude','latitude','startDate','latestDate','dateCreated']
	static view = ['employee.id','employee.name','location','longtitude','latitude','startDate','latestDate','dateCreated']
	static pub = ['user','employee']
	
	static allfields = list+pub
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [:]

    static constraints = {
		user nullable:false
		employee nullable:false
		longtitude attributes:[text:'经度',must:true]
		latitude attributes:[text:'维度',must:true]
		location attributes:[text:'位置',must:true]
		startDate attributes:[text:'开始时间',must:true]
		latestDate attributes:[text:'最新时间',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
		lastUpdated attributes:[text:'修改时间',must:true]
    }
	
	static mapping = {
		table('t_locus')
	}
}
