package com.uniproud.wcb

import java.text.DateFormat

/**
 * 考勤数据
 * 签到和签退按对存储
 * 签到和签退的时间，会参照数据模型，进行计算
 * 本数据模型中，也存储了，计算之后的结果信息
 */
class AttendanceData {

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
     * 签到方式
     * 1.手机
     * 2.pc
     */
    Integer signWay
    /**
     * 签到时间
     */
    Date signDate
    /**
     * 签退时间
     */
    Date signOutDate
    /**
     * 是否为迟到
     */
    Boolean isLate
    /**
     * 迟到时长（分钟）
     */
    Integer lateMinutes
    /**
     * 是否早退
     */
    Boolean isEarlyWork
    /**
     * 迟到时长（分钟）
     */
    Integer earlyWorkMinutes
    /**
     * 工作时间 （小时）
     * 通过时间模型换算得出得结果
     */
    Double workTime
    /**
     * 实际工作时间（小时）
     * 通过签到和签退时间换算获得
     */
    Double realWorkTime
    /**
     * 签到位置 通过坐标获取的位置
     */
    String signLocation
    /**
     * 签到经度
     */
    String signLongtitude
    /**
     * 签到维度
     */
    String signLatitude

    /**
     * 签退 通过坐标获取的位置
     */
    String signOutLocation
    /**
     * 签退经度
     */
    String signOutLongtitude
    /**
     * 签退对象维度
     */
    String signOutLatitude
    /**
     * 签到照片
     */
    Doc signPhoto
    /**
     * 签退照片
     */
    Doc signOutPhoto
    /**
     * 签到偏移距离（米）
     */
    Integer signOffsetDist
    /**
     * 签退偏移距离
     */
    Integer signOutOffsetDist
    /**
     * 签到设备
     */
    String signEqu
    /**
     * 签退设备
     */
    String signOutEqu
    /**
     * 是否出差
     */
    Boolean isBusinessTrip
    /**
     * 是否为工作日
     */
    Boolean isWorkDay
    /**
     * 创建时间
     */
    Date dateCreated
    /**
     * 修改时间
     */
    Date lastUpdated

    static V = [
            list:[
                    viewId:'AttendanceDataList',title:'考勤数据',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['attendance_data_view','attendance_data_location',
                         'attendance_data_import','attendance_data_export']//视图对应的操作
            ],
            view:[viewId:'AttendanceDataView',title:'查看考勤数据',clientType:'pc',viewType:'form',viewExtend:[]],
            mview:[viewId:'AttendanceDataMview',title:'查看考勤数据',clientType:'mobile',viewType:'form']
    ]
    static list = [
            'employee.name','signDate','signOutDate','isLate','isEarlyWork','workTime','realWorkTime','signLocation',
            'signOutLocation'
    ]
    static view = [
            'id','employee.name','signWay','signDate','signOutDate','isLate','lateMinutes','isEarlyWork','earlyWorkMinutes',
            'workTime','realWorkTime','signLocation','signLongtitude','signLatitude','signOutLocation','signOutLongtitude',
            'signOutLatitude','signPhoto','signOutPhoto','signOffsetDist','signOutOffsetDist','signEqu','signOutEqu',
            'isBusinessTrip','isWorkDay','dateCreated','lastUpdated'
    ]
    static mview  = view

    static pub = ['user','employee','signPhoto','signOutPhoto']
    static allfields = list+view+pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [:]

    static constraints = {
        user nullable:false
        employee nullable:false
        signWay attributes:[text:'签到方式',dict:38]
        signDate attributes:[text:'签到时间',must:true]
        signOutDate attributes:[text:'签退时间',must:true]
        isLate attributes:[text:'是否迟到',must:true]
        lateMinutes attributes:[text:'迟到时长（分）']
        isEarlyWork attributes:[text:'是否早退',must:true]
        earlyWorkMinutes attributes:[text:'早退时长（分）']
        workTime attributes:[text:'应工作时长（时）',must:true]
        realWorkTime attributes:[text:'实际工作时长（时）',must:true]
        signLocation attributes:[text:'签到位置',must:true]
        signLongtitude attributes:[text:'签到经度']
        signLatitude attributes:[text:'签到纬度']
        signOutLocation attributes:[text:'签退位置']
        signOutLongtitude attributes:[text:'签退经度']
        signOutLatitude attributes:[text:'签退纬度']
        signPhoto attributes:[text:'签到照片']
        signOutPhoto attributes:[text:'签退照片']
        signOffsetDist attributes:[text:'签到偏移距离（米）']
        signOutOffsetDist attributes:[text:'签退偏移距离（米）']
        signEqu attributes:[text:'签到设备']
        signOutEqu attributes:[text:'签退设备']
        isBusinessTrip attributes:[text:'是否出差']
        isWorkDay attributes:[text:'是否工作日']
        dateCreated attributes:[text:'创建时间']
        lastUpdated attributes:[text:'修改时间']
    }

    static mapping = {
        table('t_attendance_data')
    }
}
