package com.uniproud.wcb

/**
 * 回复，评论
 * @author shqv
 */
class Comment {
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 所属员工
	 */
	Employee employee
    /**
     * 关联的随笔
     */
    Note note
	/**
	 * 回复内容
	 */
	String content
	/**
	 * photos 照片
	 */
	static hasMany = [photos:Doc]
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
    static constraints = {
		employee nullable:true
		content nullable:false,attributes:[text:'ID',must:true]
    }
	
	static mapping = {
		table('t_comment')
	}
}
