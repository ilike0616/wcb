package com.uniproud.wcb
/**
 * 工作台页面中，所展示的portal
 */
class Portal {
    /**
     * 所属模块
     */
    Module module
    /**
     * 部门
     */
    Dept dept
    /**
     * 类型
     * 1.动态
     * 2.图表
     */
    Integer type
    /**
     * 标题
     */
    String title
    /**
     * 显示高度
     */
    Integer height
    /**
     * 关联组件的xtype
     */
    String xtype
    /**
     * 穿透查询需要的视图
     */
    String viewId
    /**
     * 穿透查询需要的store
     */
    String viewStore
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    static constraints = {
        module nullable:false
        title blank: false,nullable: false
    }

    static mapping = {
        table('t_portal')
    }
}
