package com.uniproud.wcb
/**
 * 企业反馈
 */
class UserFeedback {
    /**
     * 企业账户
     */
    User user
    /**
     * 反馈者
     */
    Employee employee
	/**
	 * 部门
	 */
	Dept dept
    /**
     * 反馈种类
     */
    Integer kind
    /**
     * 反馈内容
     */
    String content
    /**
	 * 创建时间
	 */
	Date dateCreated
	/**
	 * 修改时间
	 */
	Date lastUpdated

	static constraints = {
        user nullable: false
        employee nullable: false
        content nullable: false
	}

	static mapping = {
		table(name: 't_user_feedback')
	}
}
