package com.uniproud.wcb

class OutsiteRecord {
	static belongsTo = [User,Employee,CustomerFollow,ObjectFollow,ServiceTask,InstallOrder]
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
     *所属客户跟进 记录
     */
    CustomerFollow customerFollow
    /**
     *所属客户跟进 记录
     */
    ObjectFollow objectFollow
    /**
     * 服务派单模块
     */
    ServiceTask serviceTask
	/**
	 * 安装订单
	 */
	InstallOrder installOrder
	/**
     *动作类型：1-签到；2-签退；3-拍照
    */
	Integer actionKind
	/**
     * 商机类型：1-客户；2-商机 3-服务派单 4-安装订单
     */
	Integer objectType
    /**
     * 现成照片
     */
	static hasMany = [photos:Doc]
    /**
    *备注信息
    **/    
	String remark
    /**
     *商机经度
     */
    String longtitude
    /**
     * 商机维度
     */
    String latitude
    /**
     * 位置 通过坐标获取的位置
     */
    String location
    /**
     *创建时间
     */
    Date dateCreated    
    /**
     *修改时间
     */
    Date lastUpdated
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	static V = [
			list:[
				viewId:'OutsiteRecordList',title:'现场记录',clientType:'pc',viewType:'list',
				ext:[],//扩展参数值最终进入ViewExtend
				opt:['outsite_record_view','outsite_record_location']//视图对应的操作
			],
            view:[viewId:'OutsiteRecordView',title:'查看现场记录',clientType:'pc',viewType:'form',viewExtend:[]],
            customerFollowSignOn:[viewId:'CustomerFollowSignOn',title:'客户跟进签到',clientType:'mobile',viewType:'form',viewExtend:[]],
			customerFollowSignOut:[viewId:'CustomerFollowSignOut',title:'客户跟进签退',clientType:'mobile',viewType:'form',viewExtend:[]],
			customerFollowPhoto:[viewId:'CustomerFollowPhoto',title:'客户跟进拍照',clientType:'mobile',viewType:'form',viewExtend:[]],
			objectFollowSignOn:[viewId:'ObjectFollowSignOn',title:'商机跟进签到',clientType:'mobile',viewType:'form',viewExtend:[]],
			objectFollowSignOut:[viewId:'ObjectFollowSignOut',title:'商机跟进签退',clientType:'mobile',viewType:'form',viewExtend:[]],
			objectFollowPhoto:[viewId:'ObjectFollowPhoto',title:'商机跟进拍照',clientType:'mobile',viewType:'form',viewExtend:[]],
			serviceTaskSignOn:[viewId:'ServiceTaskSignOn',title:'服务派单签到',clientType:'mobile',viewType:'form',viewExtend:[]],
			serviceTaskSignOut:[viewId:'ServiceTaskSignOut',title:'服务派单签退',clientType:'mobile',viewType:'form',viewExtend:[]],
			serviceTaskPhoto:[viewId:'ServiceTaskPhoto',title:'服务派单拍照',clientType:'mobile',viewType:'form',viewExtend:[]]
		  ]
	static list = [
			'customerFollow.subject','objectFollow.subject','serviceTask.subject',
			'actionKind','objectType','longtitude','latitude','location','remark','dateCreated','lastUpdated'
		  ]
	
	static customerFollowPhoto = ['customerFollow.id','customerFollow.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']
	
	static customerFollowSignOut = ['customerFollow.id','customerFollow.subject',
			'actionKind','objectType',
			'longtitude','latitude','location','remark','photos']
	
	static customerFollowSignOn = ['customerFollow.id','customerFollow.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']
	
	static objectFollowPhoto = ['objectFollow.id','objectFollow.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']
	
	static objectFollowSignOut = ['objectFollow.id','objectFollow.subject',
			'actionKind','objectType',
			'longtitude','latitude','location','remark','photos']
	
	static objectFollowSignOn = ['objectFollow.id','objectFollow.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']

	static serviceTaskPhoto = ['serviceTask.id','serviceTask.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']
	
	static serviceTaskSignOut = ['serviceTask.id','serviceTask.subject',
			'actionKind','objectType',
			'longtitude','latitude','location','remark','photos']
	
	static serviceTaskSignOn = ['serviceTask.id','serviceTask.subject',
		'actionKind','objectType',
		'longtitude','latitude','location','remark','photos']

    static view = [
            'id','customerFollow.subject','objectFollow.subject','serviceTask.subject',
            'actionKind','objectType','longtitude','latitude','location','remark','dateCreated','lastUpdated'
    ]
	
	static allfields = list+customerFollowPhoto+customerFollowSignOut+customerFollowSignOn
	
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
			actionKind:[
				customerFollowSignOn:[xtype:'hidden',value:1],
				customerFollowSignOut:[xtype:'hidden',value:2],
				customerFollowPhoto:[xtype:'hidden',value:3],
				objectFollowSignOn:[xtype:'hidden',value:1],
				objectFollowSignOut:[xtype:'hidden',value:2],
				objectFollowPhoto:[xtype:'hidden',value:3],
				serviceTaskSignOn:[xtype:'hidden',value:1],
				serviceTaskSignOut:[xtype:'hidden',value:2],
				serviceTaskPhoto:[xtype:'hidden',value:3]
			],
			objectType:[
				customerFollowSignOn:[xtype:'hidden',value:1],
				customerFollowSignOut:[xtype:'hidden',value:1],
				customerFollowPhoto:[xtype:'hidden',value:1],
				objectFollowSignOn:[xtype:'hidden',value:2],
				objectFollowSignOut:[xtype:'hidden',value:2],
				objectFollowPhoto:[xtype:'hidden',value:2],
				serviceTaskSignOn:[xtype:'hidden',value:3],
				serviceTaskSignOut:[xtype:'hidden',value:3],
				serviceTaskPhoto:[xtype:'hidden',value:3]
			],
			photos:[
				customerFollowSignOn:[xtype:'photoPanel',target:'customerFollowSignOn'],
				customerFollowSignOut:[xtype:'photoPanel',target:'customerFollowSignOut'],
				customerFollowPhoto:[xtype:'photoPanel',target:'customerFollowPhoto'],
				objectFollowSignOn:[xtype:'photoPanel',target:'objectFollowSignOn'],
				objectFollowSignOut:[xtype:'photoPanel',target:'objectFollowSignOut'],
				objectFollowPhoto:[xtype:'photoPanel',target:'objectFollowPhoto'],
				serviceTaskSignOn:[xtype:'photoPanel',target:'serviceTaskSignOn'],
				serviceTaskSignOut:[xtype:'photoPanel',target:'serviceTaskSignOut'],
				serviceTaskPhoto:[xtype:'photoPanel',target:'serviceTaskPhoto']
			]
	]
    static constraints = {
    	user nullable: false
        employee nullable: false
        customerFollow nullable: true
        id attributes:[text:'ID',must:true]
        actionKind attributes:[text:'动作类型',must:true,dict:11]
        objectType attributes:[text:'商机类型',must:true,dict:12]
        remark attributes:[text:'备注信息',must:true]
        longtitude attributes:[text:'经度',must:true]
        latitude attributes:[text:'维度',must:true]
        location attributes:[text:'位置',must:true]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }
    static mapping = {
    	table(name:'t_outsite_record')
    }
}
