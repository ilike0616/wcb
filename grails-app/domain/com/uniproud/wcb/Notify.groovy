package com.uniproud.wcb
/**
 * 消息中心
 */
class Notify extends ExtField {
    static belongsTo = [User,Employee]
    /**
     * 所属用户
     */
    User user
    /**
     * 消息接收者
     */
    Employee employee
    /**
     * 部门
     */
    Dept dept
    /**
     *  消息类型
     *  1.业务触发消息
     *  2.自动消息
     */
    Integer type
    /**
     * 关联的数据模块
     */
    Module module
    /**
     * 对应原是数据ID
     */
    Long objectId
    /**
     * 消息主题
     */
    String subject
    /**
     * 消息内容
     */
    String content
    /**
     * 是否已已阅读
     */
    Boolean isRead = false
    /**
     * 是否已处理
     */
    Boolean state = false
    /**
     * 此消息来自于哪个，消息通知模型
     */
    NotifyModel notifyModel
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
                    viewId:'NotifyList',title:'提醒消息',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['notify_view','notify_read','notify_delete']//视图对应的操作
            ],
            latestNotifyList:[
                    viewId:'LatestNotifyList',title:'最新消息',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:[]//视图对应的操作
            ],
            view:[viewId:'NotifyView',title:'查看提醒消息',clientType:'pc',viewType:'form',viewExtend:[]],
    ]

    static list = ['id','type','module.moduleName','subject','content','isRead','state']
    static latestNotifyList = ['subject','module.moduleName','content','dateCreated']

    static view = ['id','type','module.moduleName','subject','content','isRead','state']

    static pub = ['user','employee','module','notifyModel']

    static allfields = list+view+pub

    static FE = [:]

    static VE = [:]

    static constraints = {
        id attributes:[text:'ID',must:true]
        user nullable:false
        employee nullable:false
        type attributes:[text:'消息类型',must:true,dict:40]
        module attributes:[text:'模块',must:true]
        subject attributes:[text:'主题',must:true]
        content attributes:[text:'内容',must:true]
        isRead attributes:[text:'是否已读',must:true]
        state attributes:[text:'是否已处理',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping = {
        table('t_notify')
    }
}
