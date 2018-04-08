package com.uniproud.wcb
/**
 * 权限用户Portal关系表
 */
class PrivilegeUserPortal {
	User user
	/**
	 * 部门
	 */
	Dept dept
    /**
     * 权限
     */
    Privilege privilege

    /**
     * 用户portal
     */
    UserPortal userPortal
	/**
	 *创建时间
	 */
	Date dateCreated
	/**
	 *修改时间
	 */
	Date lastUpdated
	static constraints = {
        privilege nullable: false
        userPortal nullable: false
	}

	static mapping = {
		table(name:'t_privilege_user_portal')
	}
}
