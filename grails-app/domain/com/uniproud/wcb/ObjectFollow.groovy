package com.uniproud.wcb
import org.grails.databinding.BindingFormat
/**
 * 商机拜访记录
 */
class ObjectFollow extends ExtField{
    static belongsTo = [User,Employee,OnsiteObject]

    /**
     * photos 现场拍照<OutsiteRecord>
     */
    static hasMany = [photos:OutsiteRecord,files:Doc,files1:Doc]
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 现成签到
	 */
	OutsiteRecord signon
	/**
	 * 现成签到
	 */
	OutsiteRecord signout
    /**
     * 所属用户
     */
    User user
    /**
     * 所属员工
     */
    Employee employee
    /**
     * 关联商机
     */
    OnsiteObject onsiteObject
    /**
     * 跟进主题
     */
    String subject
    /**
     * 跟进开始时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date startDate
    /**
     * 跟进结束时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date endDate
    /**
     * 跟进种类 1,电话拜访  2,上门拜访
     */
    Integer followKind
    /**
     * 跟进内容
     */
    String followContent
    /**
     * 跟进结果
     */
    Integer followResult
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 签到时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date signonDate
    /**
     *签退时间
     */
	@BindingFormat('yyyy-MM-dd HH:mm:ss.S')
    Date signoffDate
	/**
	 * 删除标志 true 标识数据删除
	 */
	Boolean deleteFlag = false
	static V = [
			list:[
				viewId:'ObjectFollowList',title:'商机跟进列表',clientType:'pc',viewType:'list',
				ext:[],//扩展参数值最终进入ViewExtend
				opt:['object_follow_add','object_follow_update','object_follow_view','object_follow_delete'
                    ,'object_follow_import','object_follow_export']//视图对应的操作
			],
			add:[viewId:'ObjectFollowAdd',title:'添加商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
			edit:[viewId:'ObjectFollowEdit',title:'修改商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
			view:[viewId:'ObjectFollowView',title:'查看商机跟进',clientType:'pc',viewType:'form',viewExtend:[]],
			madd:[viewId:'ObjectFollowMadd',title:'添加商机跟进',clientType:'mobile',viewType:'form'],
			medit:[viewId:'ObjectFollowMedit',title:'修改商机跟进',clientType:'mobile',viewType:'form'],
			mview:[viewId:'ObjectFollowMview',title:'查看商机跟进',clientType:'mobile',viewType:'form']
	]
	static list = ['onsiteObject.name','subject','startDate','endDate','followKind','followResult']
	
	static add = ['onsiteObject.id','onsiteObject.name','subject','followKind',
					'startDate','endDate','followContent','followResult','files'
    ]
	static madd = add
	static edit = ['id','onsiteObject.id','onsiteObject.name','subject','followKind','startDate','endDate',
					'followContent','followResult','files','dateCreated','lastUpdated'
    ]
	static medit = edit
	static view = ['onsiteObject.name','subject','startDate','endDate','followKind','followContent',
		'followResult','signonDate','signoffDate','files','dateCreated','lastUpdated'
    ]
	static mview = view
    static pub = ['user','employee','onsiteObject','files','files1']
    static allfields = list+add+edit+view+pub
	//默认插入 userFiledExtend中的参数
	static FE = [:]
	//默认插入 ViewDetailExtend中的参数
	static VE = [
            "onsiteObject.name":[
                add:[xtype:'baseSpecialTextfield',store:'OnsiteObjectStore',viewId:'OnsiteObjectList',hiddenName:'onsiteObject'],
                edit:[xtype:'baseSpecialTextfield',store:'OnsiteObjectStore',viewId:'OnsiteObjectList',hiddenName:'onsiteObject'],
				madd:[xtype:'selectListField',target:'onsiteObjectSelectList'],
				medit:[xtype:'selectListField',target:'onsiteObjectSelectList'],
            ]
	]
    static constraints = {
        user nullable: false
        employee nullable: false
        id attributes:[text:'ID',must:true]
        subject attributes:[text:'跟进主题',must:true]
        startDate attributes:[text:'跟进开始时间',must:true]
        endDate attributes:[text:'跟进结束时间',must:true]
        followKind attributes:[text:'跟进种类',dict:9,must:true]
        followContent attributes:[text:'跟进内容',must:true]
        followResult attributes:[text:'跟进结果',must:true,dict:10]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
        signonDate attributes:[text:'签到时间',must:true]
        signoffDate attributes:[text:'签退时间',must:true]
    }
    static mapping = {
        table(name: 't_object_follow')
    }
}
