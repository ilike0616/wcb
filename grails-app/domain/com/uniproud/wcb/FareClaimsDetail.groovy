package com.uniproud.wcb

import org.grails.databinding.BindingFormat

/**
 * 费用报销
 * @author like
 */
class FareClaimsDetail extends ExtField {

    static belongsTo = [FareClaims]
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 所属费用报销
     */
    FareClaims fareClaims
    /**
     * 二级科目
     */
    Integer detailKind
    /**
     * 科目金额
     */
    Double detailMoney
    /**
     * 事由
     */
    String detailReason
    /**
     * 票据张数
     */
    Integer ticketNum
    /**
     * 发生时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date detailStartDate
    /**
     * 截止时间
     */
    @BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date detailEndDate
    /**
     * 备注信息
     */
    String remark
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
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

    static V = [
            editList: [
                    viewId: 'FareClaimsDetailEditList', title: '费用明细', clientType: 'pc', viewType: 'list', store: 'FareClaimsDetailStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : []//视图对应的操作
            ],
            viewList: [
                    viewId: 'FareClaimsDetailViewList', title: '费用明细', clientType: 'pc', viewType: 'list', store: 'FareClaimsDetailStore',
                    ext   : [],//扩展参数值最终进入ViewExtend
                    opt   : []//视图对应的操作
            ],
            madd    : [viewId: 'FareClaimsDetailMadd', title: '添加费用明细', clientType: 'mobile', viewType: 'form'],
            medit   : [viewId: 'FareClaimsDetailMedit', title: '修改费用明细', clientType: 'mobile', viewType: 'form'],
            mview   : [viewId: 'FareClaimsDetailMview', title: '查看费用明细', clientType: 'mobile', viewType: 'form']
    ]

    static editList = ['fareClaims.id', 'detailKind', 'detailMoney', 'detailReason', 'ticketNum', 'detailStartDate', 'detailEndDate', 'remark']
    static viewList = ['fareClaims.id', 'detailKind', 'detailMoney', 'detailReason', 'ticketNum', 'detailStartDate', 'detailEndDate', 'remark']
    static madd = ['detailKind', 'detailMoney', 'detailReason', 'ticketNum', 'detailStartDate', 'detailEndDate', 'remark']
    static medit = ['id', 'detailKind', 'detailMoney', 'detailReason', 'ticketNum', 'detailStartDate', 'detailEndDate', 'remark']
    static mview = ['detailKind', 'detailMoney', 'detailReason', 'ticketNum', 'detailStartDate', 'detailEndDate', 'remark']

    static pub = ['user', 'employee', 'fareClaims']
    static allfields = editList + viewList + madd + medit + mview + pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [:]

    static constraints = {
        id attributes: [text: 'ID', must: true]
        user nullable: false
        employee nullable: false
        fareClaims nullable: false
        detailKind attributes: [text: '二级科目', must: true,dict:59]
        detailMoney attributes: [text: '科目金额', must: true]
        detailReason attributes: [text: '事由', must: true]
        ticketNum attributes: [text: '票据张数', must: true]
        detailStartDate attributes: [text: '发生时间', must: true]
        detailEndDate attributes: [text: '截止时间', must: true]
        remark attributes: [text: '备注', must: true]
        deleteFlag attributes: [text: '删除标志', must: true]
        dateCreated attributes: [text: '创建时间', must: true]
        lastUpdated attributes: [text: '修改时间', must: true]
    }

    static mapping = {
        table('t_fare_claims_detail')
    }
}
