package com.uniproud.wcb
/**
 * 权限用户Portal关系表
 */
class ModuleStore {
    /**
     * 模块
     */
    Module module
    /**
     * store名称，不带路径
     */
    String store
	/**
	 * 名称的描述
	 */
	String name
	/**
	 *创建时间
	 */
	Date dateCreated
	/**
	 *修改时间
	 */
	Date lastUpdated
	static constraints = {
        module nullable: false
        store nullable: false,unique: true
	}

	static mapping = {
		table(name:'t_module_store')
	}
}
