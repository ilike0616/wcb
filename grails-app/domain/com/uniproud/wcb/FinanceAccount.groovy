package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 财务账号
 * @author like
 */
class FinanceAccount extends ExtField {

    /**
     * 所属用户
     */
    User user
    /**
     * 创建者
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 账户名称
     */
    String name
    /**
     * 账户状态：1-待启用；2-已启用；3-已禁用
     */
    Integer state = 1
    /**
     * 账户类型
     */
    Integer kind
    /**
     * 开户银行
     */
    Integer bank
    /**
     * 开户账号
     */
    String bankAccount
    /**
     * 开户名称
     */
    String bankUserName
    /**
     * 开户日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date openDate
    /**
     * 期初金额
     */
    Double initBalance
    /**
     * 当前余额
     */
    Double balance
    /**
     * 备注
     */
    String remark
    /**
     * 创建时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date dateCreated
    /**
     * 修改时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date lastUpdated
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false

    static hasMany = [files: Doc, photos: Doc]

    static V = [
            list: [
                    viewId: 'FinanceAccountList', title: '财务账户', clientType: 'pc', viewType: 'list', store: 'FinanceAccountStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : ['finance_account_add', 'finance_account_update', 'finance_account_view', 'finance_account_delete'
                             , 'finance_account_export']//视图对应的操作
            ],
            view: [viewId: 'FinanceAccountView', title: '查看财务账户', clientType: 'pc', viewType: 'form', viewExtend: []],
            add : [viewId: 'FinanceAccountAdd', title: '添加财务账户', clientType: 'pc', viewType: 'form'],
            edit: [viewId: 'FinanceAccountEdit', title: '修改财务账户', clientType: 'pc', viewType: 'form'],
    ]
    static list = ['name','kind','bank','bankAccount','bankUserName','openDate','initBalance','balance','dateCreated']
    static view = ['name','kind','bank','bankAccount','bankUserName','openDate','initBalance','balance','dateCreated','remark']
    static add = ['name','kind','bank','bankAccount','bankUserName','openDate','initBalance','remark']
    static edit = ['id','name','kind','bank','bankAccount','bankUserName','openDate','initBalance','remark']

    static pub = ['user', 'employee','files','photos']
    static allfields = list + add + edit + view + pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [:]

    static constraints = {
        id attributes: [text: 'ID', must: true]
        user nullable: false
        employee nullable: false
        name attributes: [text: '账户名称', must: true]
        kind attributes: [text: '账户类型', must: true, dict: 60]
        bank attributes: [text: '开户银行', must: true, dict: 61]
        bankAccount attributes: [text: '开户账号', must: true]
        bankUserName attributes: [text: '开户名称', must: true]
        openDate attributes: [text: '开户日期', must: true]
        initBalance attributes: [text: '期初金额', must: true]
        balance attributes: [text: '当前余额', must: true]
        remark attributes: [text: '备注', must: true]
        deleteFlag attributes: [text: '删除标志', must: true]
        dateCreated attributes: [text: '创建时间', must: true]
        lastUpdated attributes: [text: '修改时间', must: true]
    }

    static mapping = {
        table('t_finance_account')
    }
}
