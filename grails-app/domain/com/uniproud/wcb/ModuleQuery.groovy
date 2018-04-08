package com.uniproud.wcb

import org.grails.databinding.BindingFormat

class ModuleQuery {
    /**
     * 部门
     */
    Dept dept
    /**
     * 员工
     */
    Employee employee
    /**
     * 模块
     */
    Module module
    /**
     * 修改时间
     */
    Date lastQueryDate

    static constraints = {
        employee nullable:false
        module nullable: false
    }
	
    static mapping = {
        table('t_module_query')
    }
}
