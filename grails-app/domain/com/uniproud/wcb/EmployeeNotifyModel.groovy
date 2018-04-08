package com.uniproud.wcb
/**
 * 员工与通知模型的关系表
 * 1.员工可以设置，是否或者禁用对应的通知模型
 * 2.员工可以设置修改，通知模型中的，消息模版
 * 3.一个员工对应的一个通知模型只能有一条纪录
 */
class EmployeeNotifyModel {
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
     * 关联的通知模型
     */
    NotifyModel notifyModel
    /**
     * 是否接收对应模型的提醒消息
     */
    Boolean isRecv
    /**
     * 系统默认的提醒消息主题模版
     */
    String subjectTemplate
    /**
     * 系统默认的提醒消息内容模版
     */
    String contentTemplate
    /**
     *创建时间
     */
    Date dateCreated
    /**
     *修改时间
     */
    Date lastUpdated

    static V = [
            list:[
                    viewId:'EmployeeNotifyModelList',title:'员工消息设置',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['employee_notify_model_view','employee_notify_model_update','employee_notify_model_default']//视图对应的操作
            ],
            forbidList:[
                    viewId:'EmployeeNotifyModelForbidList',title:'员工消息设置',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['employee_notify_model_view']//视图对应的操作
            ],
            view:[viewId:'EmployeeNotifyModelView',title:'查看员工消息设置',clientType:'pc',viewType:'form',viewExtend:[]],
            edit:[viewId:'EmployeeNotifyModelEdit',title:'员工消息设置',clientType:'pc',viewType:'form',viewExtend:[]]
    ]

    static list = ['id','notifyModel.id','notifyModel.name','isRecv','subjectTemplate','contentTemplate']

    static forbidList = ['id','notifyModel.id','notifyModel.name','isRecv','subjectTemplate','contentTemplate']

    static view = ['id','notifyModel.id','notifyModel.name','isRecv','subjectTemplate','contentTemplate']

    static edit = ['id','notifyModel.id','notifyModel.name','isRecv','subjectTemplate','contentTemplate']

    static pub = ['user','employee','notifyModel']

    static allfields = list+forbidList+view+edit+pub

    static FE = [:]

    static VE = [
            "subjectTemplate":[
                    add:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore'],
                    view:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore'],
                    edit:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore']
            ],
            "contentTemplate":[
                    add:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore'],
                    view:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore'],
                    edit:[xtype:'baseTemplateTextAreaField',storeId:'user.store.UserFieldStore']
            ]
    ]

    static constraints = {
        user nullable:false
        employee nullable:false
        notifyModel nullable:false
        isRecv attributes:[text:'是否接收',must:true]
        subjectTemplate attributes:[text:'主题模版',must:true]
        contentTemplate attributes:[text:'内容模版',must:true]
    }
    static mapping = {
        table('t_employee_notify_model')
    }
}
