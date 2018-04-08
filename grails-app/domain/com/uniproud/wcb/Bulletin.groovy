package com.uniproud.wcb

/**
 * 内部公告表
 * @author shqv
 */
class Bulletin extends ExtField{
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
     * 标题
     */
    String subject
    /**
     * 分类
     * 1：重要通知；2：公司新闻；业界动态；4：其他
     */
    Integer kind
    /**
     * 通告内容
     */
    String content
    /**
     * 是否全公司
     */
    Boolean allCompany
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated
    /**
     * 删除标志 true 标识数据删除
     */
    Boolean deleteFlag = false

    List acceptors
    List acceptDepts
    static hasMany = [acceptors:Employee,acceptDepts:Dept,files:Doc,files1:Doc]

    static V = [
            list:[
                    viewId:'BulletinList',title:'内部公告列表',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['bulletin_add','bulletin_update','bulletin_view','bulletin_delete',
                         'bulletin_export']//视图对应的操作
            ],
            latestBulletinList:[
                    viewId:'LatestBulletinList',title:'最新公告',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:[]//视图对应的操作
            ],
            add:[viewId:'BulletinAdd',title:'添加内部公告',clientType:'pc',viewType:'form',viewExtend:[]],
            edit:[viewId:'BulletinEdit',title:'修改内部公告',clientType:'pc',viewType:'form',viewExtend:[]],
            view:[viewId:'BulletinView',title:'查看内部公告',clientType:'pc',viewType:'form',viewExtend:[]]
    ]
    static list = [
        'subject','kind','content','allCompany','employee.name','dateCreated'
    ]

    static latestBulletinList = [
            'subject','kind','content','allCompany','employee.name','dateCreated'
    ]

    static add = [
        'subject','kind','content','allCompany','acceptors.name','acceptDepts.name','files','files1'
    ]
    static edit = [
        'id','subject','kind','content','allCompany','acceptors.name','acceptDepts.name','files','files1','dateCreated','lastUpdated'
    ]
    static view = [
        'id','subject','kind','content','allCompany','acceptors.name','acceptDepts.name','files','files1','dateCreated','lastUpdated'
    ]

    static pub = ['user','employee','acceptors','acceptDepts','files1','files']
    static allfields = list+add+edit+view+pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [
            "employee.name":[
                list:[fieldLabel:'所有者']
            ],
            "acceptors.name":[
                list:[fieldLabel: '接受者']
            ],
            "acceptDepts.name":[
                list:[fieldLabel: '接受部门']
            ],
            "content":[
                list:[width:400]
            ],
            "acceptors.name":[
                    add:[xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'acceptors',colspan:2],
                    edit:[xtype:'baseMultiSelectTextareaField',store:'EmployeePagingStore',viewId:'EmployeeList',hiddenName:'acceptors',colspan:2],
                    view:[xtype:'textarea',colspan:2,width: 500]
            ],
            "acceptDepts.name":[
                    add:[xtype:'baseMultiSelectTextareaField',store:'DeptNotTreeStore',viewId:'DeptList',hiddenName:'acceptDepts',colspan:2],
                    edit:[xtype:'baseMultiSelectTextareaField',store:'DeptNotTreeStore',viewId:'DeptList',hiddenName:'acceptDepts',colspan:2],
                    view:[xtype:'textarea',colspan:2,width: 500]
            ]
    ]

    static constraints = {
        user nullable: false
        employee nullable: false
        acceptors nullable: true
        acceptDepts nullable: true
        files nullable: true
        files1 nullable: true
        id attributes:[text:'ID',must:true]
        subject attributes:[text:'主题',must:true]
        kind attributes:[text:'种类',dict:41,must:true]
        content attributes:[text:'内容',must:true]
        allCompany attributes:[text:'是否全公司',dict:46]
        dateCreated attributes:[text:'创建时间',must:true]
        lastUpdated attributes:[text:'修改时间',must:true]
    }

    static mapping={
        table(name:'t_bulletin')
    }
}
