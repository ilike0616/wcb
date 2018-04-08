package com.uniproud.wcb
/**
 * 系统消息模型表
 * 包含自动消息和触发消息两种情况
 * 触发消息
 *  1.比关联NotifyModelFilter ，否则此模型不生效
 * 自动消息
 *
 */
class NotifyModel {
    static belongsTo = [User,Employee,Module]
    /**
     * 所属用户
     */
    User user
    /**
     * 创建者
     * 如果是系统管理员创建，此字段可以为空
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     * 模型名称
     */
    String name
    /**
     * 是否是自动按规则产生消息
     * true标示为，后台线程轮训，不是业务直接触发。如，生日提醒，计划执行提醒
     * false表示为，业务触发消息。当字段值发送变化，就产生此消息
     */
    Boolean isAuto = false
    /**
     * 如果是自动按规则，处理的任务，必须指定预期时间
     */
    Date expectDate
    /**
     * 关联的具体模块
     */
    Module module
    /**
     * 关联的条件
     */
    NotifyModelFilter notifyModelFilter
    /**
     * 新增的时候是否提醒
     */
    Boolean insertNotify = true
    /**
     * 修改的时候是否提醒
     */
    Boolean updateNotify = true
    /**
     * 删除的时候是否提醒
     */
    Boolean deleteNotify = false
    /**
     * 消息接收者，所对应的字段
     * 此字段筛选，应是此数据模型关联的Employee
     */
    String notifyField
    /**
     * 消息接收者是否有可能是多人
     */
    Boolean isNotifyMany
    /**
     * 接收消息者，默认情况下，是否接收消息
     * 有可能有的消息，是需要员工开启才对改员工产生消息
     */
    Boolean isDefaultRecv = true
    /**
     * 是否允许消息接收者，禁止此消息
     */
    Boolean isAllowForbid = true
    /**
     * 系统默认的提醒消息主题模版
     */
    String subjectTemplate
    /**
     * 系统默认的提醒消息内容模版
     */
    String contentTemplate
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false
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
                    viewId:'NotifyModelList',title:'消息模型列表',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['notify_model_add','notify_model_update','notify_model_view','notify_model_delete'
                         ,'notify_model_filter']//视图对应的操作
            ],
            add:[viewId:'NotifyModelAdd',title:'添加消息模型',clientType:'pc',viewType:'form',viewExtend:[]],
            edit:[viewId:'NotifyModelEdit',title:'修改消息模型',clientType:'pc',viewType:'form',viewExtend:[]],
            view:[viewId:'NotifyModelView',title:'查看消息模型',clientType:'pc',viewType:'form',viewExtend:[]],
    ]

    static list = ['id','name','module.moduleName','insertNotify','updateNotify','notifyField',
                   'isNotifyMany','isDefaultRecv','isAllowForbid','dateCreated']

    static add = ['name','module.moduleId','insertNotify','updateNotify','notifyField',
                  'isDefaultRecv','isAllowForbid','subjectTemplate','contentTemplate']

    static edit = ['id','name','module.moduleId','insertNotify','updateNotify','notifyField',
                   'isDefaultRecv','isAllowForbid','subjectTemplate','contentTemplate']

    static view = ['name','module.moduleName','insertNotify','updateNotify','notifyField',
                   'isDefaultRecv','isAllowForbid','subjectTemplate','contentTemplate','dateCreated','lastUpdated']

    static pub = ['user','employee','module','notifyModelFilter']

    static allfields = list+add+edit+view+pub

    static FE = [:]

    static VE = [
            'module.moduleId':[
                    add:[xtype:'hidden'],
                    edit:[xtype:'hidden']
            ],
            'notifyField':[
                add:[xtype:'combo',valueField:'fieldName',displayField:'fieldText',store:[] ],
                edit:[xtype:'combo',valueField:'fieldName',displayField:'fieldText',store:[] ],
                view:[xtype:'combo',valueField:'fieldName',displayField:'fieldText',store:[] ]
            ],
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
        employee nullable:true
        name attributes:[text:'名称',must:true]
        isAuto attributes:[text:'自动规则',must:true]
        expectDate  attributes:[text:'提醒时间',must:true]
        module  attributes:[text:'模块',must:true]
        insertNotify attributes:[text:'新增提醒',must:true]
        updateNotify attributes:[text:'修改提醒',must:true]
        deleteNotify attributes:[text:'删除提醒',must:true]
        notifyField attributes:[text:'接受者',must:true]
        isNotifyMany attributes:[text:'多接受者',must:true]
        isDefaultRecv attributes:[text:'默认接收',must:true]
        isAllowForbid attributes:[text:'允许禁止',must:true]
        subjectTemplate attributes:[text:'主题模板',must:true]
        contentTemplate attributes:[text:'内容模板',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_notify_model')
    }
}
