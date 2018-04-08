package com.uniproud.wcb

class ShareDetail {
    static belongsTo = [share:Share]
    /**
     * 所属公司
     */
    User user
    /**
     * 分享者
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 分享到哪
     */
    String shareTo
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static constraints = {
        user nullable:false
        employee nullable:false
    }
	
    static mapping = {
        table('t_share_detail')
    }
}
