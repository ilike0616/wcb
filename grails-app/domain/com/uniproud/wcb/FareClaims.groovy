package com.uniproud.wcb

import com.sun.org.apache.xpath.internal.operations.Bool
import org.grails.databinding.BindingFormat

/**
 * 费用报销
 * @author like
 */
class FareClaims extends ExtField {
    static hasMany = [files: Doc, photos: Doc, detail: FareClaimsDetail, financeExpense:FinanceIncomeExpense]
    /**
     * 所属用户
     */
    User user
    /**
     * 创建者
     */
    Employee employee
    /**
     * 所有者
     */
    Employee owner
    /**
     * 部门
     */
    Dept dept
    /**
     * 费用主题
     */
    String subject
    /**
     * 报销单号
     */
    String fareNo
    /**
     * 金额
     */
    Double money
    /**
     * 费用类型
     */
    Integer fareKind
    /**
     * 报销类型
     */
    Integer claimsKind
    /**
     * 是否预付
     */
    Boolean isPrepay = false
    /**
     * 已支取现金
     */
    Boolean isDraw = false
    /**
     * 关联的客户
     */
    Customer customer
    /**
     * 关联的联系人
     */
    Contact contact
    /**
     * 关联的销售机会
     */
    SaleChance saleChance
    /**
     * 关联的销售订单
     */
    ContractOrder contractOrder
    /**
     * 关联的市场活动
     */
    MarketActivity marketActivity
    /**
     * 关联的外出申请
     */
    GoOutApply goOutApply
    /**
     * 关联的出差申请
     */
    BusinessTripApply businessTripApply
    /**
     *  关联的审核任务
     */
    Audit audit
    /**
     * 当前审核人
     */
    Employee auditor
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

    static V = [
            list : [
                    viewId: 'FareClaimsList', title: '费用报销', clientType: 'pc', viewType: 'list', store: 'FareClaimsStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : ['fare_claims_add', 'fare_claims_update', 'fare_claims_view', 'fare_claims_delete'
                             , 'fare_claims_export']//视图对应的操作
            ],
            view : [viewId: 'FareClaimsView', title: '查看费用报销', clientType: 'pc', viewType: 'form', viewExtend: []],
            add  : [viewId: 'FareClaimsAdd', title: '添加费用报销', clientType: 'pc', viewType: 'form'],
            edit : [viewId: 'FareClaimsEdit', title: '修改费用报销', clientType: 'pc', viewType: 'form'],
            madd : [viewId: 'FareClaimsMadd', title: '添加费用报销', clientType: 'mobile', viewType: 'form'],
            medit: [viewId: 'FareClaimsMedit', title: '修改费用报销', clientType: 'mobile', viewType: 'form'],
            mview: [viewId: 'FareClaimsMview', title: '查看费用报销', clientType: 'mobile', viewType: 'form']
    ]
    static list = ['subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'audit.auditState', 'auditor.name', 'owner.name', 'dateCreated']
    static view = ['id','subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'audit.auditState', 'auditor.id', 'auditor.name', 'owner.name', 'remark', 'detail']
    static add = ['subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'auditor.id', 'auditor.name', 'owner.name', 'remark', 'detail']
    static edit = ['id', 'subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'auditor.id', 'auditor.name', 'owner.id', 'owner.name', 'remark', 'detail']
    static madd = ['subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'auditor.id', 'auditor.name', 'owner.name', 'remark']
    static medit = ['id', 'subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'auditor.id', 'auditor.name', 'owner.name', 'remark']
    static mview = ['subject', 'money', 'fareKind', 'claimsKind', 'isPrepay', 'isDraw', 'audit.auditState', 'auditor.id', 'auditor.name', 'owner.name', 'remark']

    static pub = ['user', 'employee', 'customer', 'contact', 'saleChance', 'contractOrder', 'marketActivity','audit','auditor','files','photos','financeExpense']
    static allfields = list + add + edit + view + madd + medit + mview + pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [
            "auditor.name"         : [
                    list : [text: '审核人'],
                    add  : [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor'],
                    edit : [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor'],
                    madd : [xtype: 'selectListField', target: 'employeeSelectList', label: '审核人'],
                    medit: [xtype: 'selectListField', target: 'employeeSelectList', label: '审核人']
            ],
            "owner.name"           : [
                    list : [text: '所有者'],
                    add  : [fieldLabel: '所有者', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'owner'],
                    edit : [fieldLabel: '所有者', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'owner'],
                    madd : [xtype: 'selectListField', target: 'employeeSelectList', label: '所有者'],
                    medit: [xtype: 'selectListField', target: 'employeeSelectList', label: '所有者']
            ],
            "customer.name"        : [
                    add  : [xtype: 'baseSpecialTextfield', store: 'CustomerStore', viewId: 'CustomerList', hiddenName: 'customer'],
                    edit : [xtype: 'baseSpecialTextfield', store: 'CustomerStore', viewId: 'CustomerList', hiddenName: 'customer'],
                    madd : [xtype: 'selectListField', target: 'customerSelectList', nextSelectListField: 'contact.name'],
                    medit: [xtype: 'selectListField', target: 'customerSelectList', nextSelectListField: 'contact.name']
            ],
            "contact.name"         : [
                    add  : [xtype: 'baseSpecialTextfield', store: 'ContactStore', viewId: 'ContactList', hiddenName: 'contact', paramName: 'customer'],
                    edit : [xtype: 'baseSpecialTextfield', store: 'ContactStore', viewId: 'ContactList', hiddenName: 'contact', paramName: 'customer'],
                    madd : [xtype: 'selectListField', target: 'contactSelectList'],
                    medit: [xtype: 'selectListField', target: 'contactSelectList']
            ],
            "saleChance.subject"   : [
                    add  : [xtype: 'baseSpecialTextfield', store: 'SaleChanceStore', viewId: 'SaleChanceList', hiddenName: 'saleChance'],
                    edit : [xtype: 'baseSpecialTextfield', store: 'SaleChanceStore', viewId: 'SaleChanceList', hiddenName: 'saleChance'],
                    madd : [xtype: 'selectListField', target: 'saleChanceSelectList'],
                    medit: [xtype: 'selectListField', target: 'saleChanceSelectList']
            ],
            "contractOrder.subject": [
                    add  : [xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder'],
                    edit : [xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder'],
                    madd : [xtype: 'selectListField', target: 'contractOrderSelectList'],
                    medit: [xtype: 'selectListField', target: 'contractOrderSelectList']
            ],
            "marketActivity.name"  : [
                    add  : [xtype: 'baseSpecialTextfield', store: 'MarketActivityStore', viewId: 'MarketActivityList', hiddenName: 'marketActivity'],
                    edit : [xtype: 'baseSpecialTextfield', store: 'MarketActivityStore', viewId: 'MarketActivityList', hiddenName: 'marketActivity'],
                    madd : [xtype: 'selectListField', target: 'marketActivitySelectList'],
                    medit: [xtype: 'selectListField', target: 'marketActivitySelectList']
            ],
            detail                 : [
                    add : [title: '费用明细', listView: 'FareClaimsDetailEditList'],
                    edit: [title: '费用明细', listView: 'FareClaimsDetailEditList'],
                    view: [title: '费用明细', listView: 'FareClaimsDetailEditList']
            ]
    ]
    static constraints = {
        id attributes: [text: 'ID', must: true]
        user nullable: false
        employee nullable: false
        owner attributes: [text: '所有者', must: true]
        subject attributes: [text: '费用主题', must: true]
        fareNo attributes: [text: '报销单号', must: true]
        money attributes: [text: '金额', must: true]
        fareKind attributes: [text: '费用类型', must: true, dict: 57]
        claimsKind attributes: [text: '报销类型', must: true, dict: 58]
        isPrepay attributes: [text: '是否预付', must: true]
        isDraw attributes: [text: '已支付', must: true]
        customer attributes: [text: '客户', must: true]
        contact attributes: [text: '联系人', must: true]
        saleChance attributes: [text: '销售商机', must: true]
        contractOrder attributes: [text: '订单', must: true]
        marketActivity attributes: [text: '市场活动', must: true]
        auditor attributes: [text: '审核人', must: true]
        remark attributes: [text: '备注', must: true]
        deleteFlag attributes: [text: '删除标志', must: true]
        dateCreated attributes: [text: '创建时间', must: true]
        lastUpdated attributes: [text: '修改时间', must: true]
    }

    static mapping = {
        table('t_fare_claims')
    }
}
