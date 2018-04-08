package com.uniproud.wcb

import grails.transaction.Transactional
import org.grails.databinding.BindingFormat

class FinanceIncomeExpense extends ExtField {

    static belongsTo = [User,Employee]

    static hasMany = [files: Doc, photos: Doc]
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
     * 出账（入账）主题
     */
    String subject
    /**
     * 流水号
     */
    String flowNo
    /**
     * 凭证号
     */
    String certificateNo
    /**
     * 1-入账；2-出账
     */
    Integer financeType
    /**
     * 入账类型
     */
    Integer incomeKind
    /**
     * 出账类型
     */
    Integer expenseKind
    /**
     * 付款方式
     */
    Integer payMethod
    /**
     * 关联财务账号：入账的入方，出账的出方
     */
    FinanceAccount financeAccount
    /**
     * 对方账户：入账的出方，出账的入方
     */
    String oppositeAccount
    /**
     * 对方开户行
     */
    Integer counterpartBank
    /**
     * 对方开户名
     */
    String counterpartBankUsername
    /**
     * 对方开户账号
     */
    String counterpartBankAccount
    /**
     * 出入账金额
     */
    Double money
    /**
     * 账户余额
     */
    Double financeAccountBalance
    /**
     * 流水日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date flowDate
    /**
     * 是否记账：1-带记账，2-已记账；3-已禁止
     */
    Integer chargeState = 1
    /**
     * 记账人
     */
    Employee bookkeeper
    /**
     * 记账日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date financeDate
    /**
     * 是否结账
     */
    Boolean isReckoning = false
    /**
     * 是否开票
     */
    Boolean isInvoice = false
    /**
     * 发票号
     */
    String invoiceNo
    /**
     * 开票日期
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date invoiceDate
    /**
     * 关联审核单
     */
    Audit audit
    /**
     * 审核人
     */
    Employee auditor
    /**
     * 经手人
     */
    Employee handEmployee
    /**
     * 红字更正
     */
    Integer wrongKind = 1
    /**
     * 备注
     */
    String remark
    /**
     * 关联类型:1-订单；2-费用报销
     */
    Integer linkType
    /**
     * 关联订单
     */
    ContractOrder contractOrder
    /**
     * 关联费用报销
     */
    FareClaims fareClaims
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

    def financeIncomeService

    @Transactional
    def afterInsert(){
        financeIncomeService.updateContractOrder(this)
    }

    def afterUpdate(){
        financeIncomeService.updateContractOrder(this)
    }

    def afterDelete(){
        financeIncomeService.updateContractOrder(this)
    }

    static V = [
            incomeList: [
                    viewId: 'FinanceIncomeList', title: '入账管理', clientType: 'pc', viewType: 'list', store: 'FinanceIncomeStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : ['finance_income_add', 'finance_income_update', 'finance_income_view', 'finance_income_delete'
                             , 'finance_income_export']//视图对应的操作
            ],
            incomeView: [viewId: 'FinanceIncomeView', title: '查看入账单', clientType: 'pc', viewType: 'form', viewExtend: []],
            incomeAdd : [viewId: 'FinanceIncomeAdd', title: '添加入账单', clientType: 'pc', viewType: 'form'],
            incomeEdit: [viewId: 'FinanceIncomeEdit', title: '修改入账单', clientType: 'pc', viewType: 'form'],
            incomeCharge: [viewId: 'FinanceIncomeCharge', title: '记账', clientType: 'pc', viewType: 'form']
    ]
    static V1 = [
            expenseList: [
                    viewId: 'FinanceExpenseList', title: '出账管理', clientType: 'pc', viewType: 'list', store: 'FinanceExpenseStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : ['finance_expense_add', 'finance_expense_update', 'finance_expense_view', 'finance_expense_delete'
                             , 'finance_expense_export']//视图对应的操作
            ],
            expenseView: [viewId: 'FinanceExpenseView', title: '查看出账单', clientType: 'pc', viewType: 'form', viewExtend: []],
            expenseAdd : [viewId: 'FinanceExpenseAdd', title: '添加出账单', clientType: 'pc', viewType: 'form'],
            expenseEdit: [viewId: 'FinanceExpenseEdit', title: '修改出账单', clientType: 'pc', viewType: 'form'],
            expenseCharge: [viewId: 'FinanceExpenseCharge', title: '记账', clientType: 'pc', viewType: 'form']
    ]

    static incomeList = ['subject', 'certificateNo', 'incomeKind', 'payMethod', 'financeAccount.name', 'oppositeAccount', 'money', 'financeAccountBalance','audit.auditState','auditor.name',
                         'flowDate', 'financeDate', 'linkType', 'dateCreated']
    static incomeView = ['subject', 'certificateNo', 'incomeKind', 'payMethod', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                         'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','audit.auditState','auditor.name',
                         'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate', 'linkType', 'dateCreated']
    static incomeAdd = ['subject', 'certificateNo', 'incomeKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                        'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                        'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static incomeEdit = ['id', 'subject', 'certificateNo', 'incomeKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                         'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                         'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static incomeCharge = ['id', 'subject', 'certificateNo', 'incomeKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                         'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                         'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static expenseList = ['subject', 'certificateNo', 'expenseKind', 'payMethod', 'financeAccount.name', 'oppositeAccount', 'money', 'financeAccountBalance','audit.auditState','auditor.name',
                          'flowDate', 'financeDate', 'linkType', 'dateCreated']
    static expenseView = ['subject', 'certificateNo', 'expenseKind', 'payMethod', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                          'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','audit.auditState','auditor.name',
                          'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate', 'linkType', 'dateCreated']
    static expenseAdd = ['subject', 'certificateNo', 'expenseKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                         'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                         'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static expenseEdit = ['id', 'subject', 'certificateNo', 'expenseKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                          'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                          'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static expenseCharge = ['id', 'subject', 'certificateNo', 'expenseKind', 'payMethod', 'financeAccount.id', 'financeAccount.name', 'oppositeAccount', 'counterpartBank',
                          'counterpartBankUsername', 'counterpartBankAccount', 'money', 'financeAccountBalance','auditor.name',
                          'flowDate', 'financeDate', 'isInvoice', 'invoiceNo', 'invoiceDate']
    static pub = ['user', 'employee', 'financeAccount', 'audit', 'auditor', 'handler', 'contractOrder', 'fareClaims', 'files', 'photos']
    static allfields = incomeList + incomeView + incomeEdit + incomeAdd + incomeCharge + expenseList + expenseView + expenseAdd + expenseEdit + expenseCharge + pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [
            "auditor.name"         : [
                    incomeList : [text: '审核人'],
                    incomeAdd  : [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor'],
                    incomeEdit : [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor'],
                    expenseList: [text: '审核人'],
                    expenseAdd : [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor'],
                    expenseEdit: [fieldLabel: '审核人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'auditor']
            ],
            "handEmployee.name"         : [
                    incomeList : [text: '经手人'],
                    incomeAdd  : [fieldLabel: '经手人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'handEmployee'],
                    incomeEdit : [fieldLabel: '经手人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'handEmployee'],
                    expenseList: [text: '经手人'],
                    expenseAdd : [fieldLabel: '经手人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'handEmployee'],
                    expenseEdit: [fieldLabel: '经手人', xtype: 'baseSpecialTextfield', store: 'EmployeePagingStore', viewId: 'EmployeeList', hiddenName: 'handEmployee']
            ],
            'financeAccount.name'  : [
                    incomeList : [text: '入方'],
                    incomeAdd  : [fieldLabel: '入方', xtype: 'baseSpecialTextfield', store: 'FinanceAccountStore', viewId: 'FinanceAccountList', hiddenName: 'financeAccount'],
                    incomeEdit : [fieldLabel: '入方', xtype: 'baseSpecialTextfield', store: 'FinanceAccountStore', viewId: 'FinanceAccountList', hiddenName: 'financeAccount'],
                    expenseList: [text: '出方'],
                    expenseAdd : [fieldLabel: '出方', xtype: 'baseSpecialTextfield', store: 'FinanceAccountStore', viewId: 'FinanceAccountList', hiddenName: 'financeAccount'],
                    expenseEdit: [fieldLabel: '出方', xtype: 'baseSpecialTextfield', store: 'FinanceAccountStore', viewId: 'FinanceAccountList', hiddenName: 'financeAccount']
            ],
            'contractOrder.subject': [
                    incomeList : [text: '订单'],
                    incomeAdd  : [fieldLabel: '订单', xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder'],
                    incomeEdit : [fieldLabel: '订单', xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder'],
                    expenseList: [text: '订单'],
                    expenseAdd : [fieldLabel: '订单', xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder'],
                    expenseEdit: [fieldLabel: '订单', xtype: 'baseSpecialTextfield', store: 'ContractOrderStore', viewId: 'ContractOrderList', hiddenName: 'contractOrder']
            ],
            'fareClaims.subject'   : [
                    incomeList : [text: '费用报销'],
                    incomeAdd  : [fieldLabel: '费用报销', xtype: 'baseSpecialTextfield', store: 'FareClaimsStore', viewId: 'FareClaimsList', hiddenName: 'fareClaims'],
                    incomeEdit : [fieldLabel: '费用报销', xtype: 'baseSpecialTextfield', store: 'FareClaimsStore', viewId: 'FareClaimsList', hiddenName: 'fareClaims'],
                    expenseList: [text: '费用报销'],
                    expenseAdd : [fieldLabel: '费用报销', xtype: 'baseSpecialTextfield', store: 'FareClaimsStore', viewId: 'FareClaimsList', hiddenName: 'fareClaims'],
                    expenseEdit: [fieldLabel: '费用报销', xtype: 'baseSpecialTextfield', store: 'FareClaimsStore', viewId: 'FareClaimsList', hiddenName: 'fareClaims']
            ],
            'financeAccountBalance':[
                    incomeAdd:[initName:'financeAccount.balance'],
                    incomeEdit:[initName:'financeAccount.balance'],
                    expenseAdd:[initName:'financeAccount.balance'],
                    expenseEdit:[initName:'financeAccount.balance']
            ]
    ]
    static constraints = {
        id attributes: [text: 'ID', must: true]
        user nullable: false
        employee nullable: false
        subject attributes: [text: '主题', must: true]
        flowNo attributes: [text: '流水号', must: true]
        certificateNo attributes: [text: '凭证号', must: true]
        financeType attributes: [text: '出入账', must: true, dict: 62]
        incomeKind attributes: [text: '入账类型', must: true, dict: 63]
        expenseKind attributes: [text: '出账类型', must: true, dict: 64]
        payMethod attributes: [text: '付款方式', must: true, dict: 42]
        oppositeAccount attributes: [text: '业务方', must: true]
        counterpartBank attributes: [text: '对方开户行', must: true, dict: 61]
        counterpartBankUsername attributes: [text: '对方开户名', must: true]
        counterpartBankAccount attributes: [text: '对方开户号', must: true]
        money attributes: [text: '金额', must: true]
        financeAccountBalance attributes: [text: '账户余额', must: true]
        flowDate attributes: [text: '流水日期', must: true]
        chargeState attributes: [text: '是否记账', must: true,dict: 66]
        financeDate attributes: [text: '记账日期', must: true]
        isReckoning attributes: [text: '是否结账', must: true, dict: 39]
        isInvoice attributes: [text: '是否开票', must: true, dict: 39]
        invoiceNo attributes: [text: '发票号', must: true]
        invoiceDate attributes: [text: '开票日期', must: true]
        wrongKind attributes: [text: '红字更正', must: true, dict: 65]
        remark attributes: [text: '备注', must: true]
        linkType attributes: [text: '关联类型', must: true, dict: 71]
        deleteFlag attributes: [text: '删除标志', must: true]
        dateCreated attributes: [text: '创建时间', must: true]
        lastUpdated attributes: [text: '修改时间', must: true]
        money validator:{val,obj,errors ->
            if(obj.financeType == 1 && obj.contractOrder){
                def money = 0
                FinanceIncomeExpense.createCriteria().list {
                    ne("id", obj.id)
                    eq("financeType",1)
                    eq("contractOrder", obj.contractOrder)
                    ne("chargeState",3) //排除禁止的
                    eq("deleteFlag",false)
                }?.each {
                    if(!it.audit || it.audit?.auditState != 4) {
                        money += it.money
                    }
                }
//log.info "$val - (${obj.contractOrder.discountMoney} - $money >= 1 :"+(val - (obj.contractOrder.discountMoney - money) >= 1)
                if(obj.contractOrder.discountMoney > 0) {
                    if (val - (obj.contractOrder.discountMoney - money) >= 1) {
                        errors.rejectValue('money', '金额大于订单的剩余可新增入账金额！')
                    }
                }else{
                    if (obj.contractOrder.discountMoney - money - val >= 1) {
                        errors.rejectValue('money', '金额大于订单的剩余可新增入账金额！')
                    }
                }
            }
        }
    }

    static mapping = {
        table('t_finance_income_expense')
    }
}
