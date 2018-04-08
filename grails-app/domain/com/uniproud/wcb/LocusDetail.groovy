package com.uniproud.wcb

import java.util.Date;

class LocusDetail {
	
	static belongsTo = [Locus]
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
	 * 轨迹
	 */
	Locus locus
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
	 * 定位时间
	 */
	Date locusDate
	/**
	 * MAC地址
	 */
	String mac
	/**
	 * 手机型号
	 */
	String model
    /**
     * 提交时间（手机端时间）
     */
    Date submitDate
	/**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false

    static constraints = {
		user nullable:false
		employee nullable:false
		longtitude attributes:[text:'经度',must:true]
		latitude attributes:[text:'维度',must:true]
		location attributes:[text:'位置',must:true]
		locusDate attributes:[text:'定位时间',must:true]
		mac attributes:[text:'MAC地址',must:true]
		model attributes:[text:'手机',must:true]
		dateCreated attributes:[text:'创建时间',must:true]
    }
	
	static mapping = {
		table('t_locus_detail')
	}
}
