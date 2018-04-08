package com.uniproud.wcb
/**
 * 考勤数据，数据由系统，自动分析考勤数据，出差申请，外出申请，请假申请等数据模型后，换算得到此数据。
 * 用于每月进行考勤数据统计
 */
class AttendanceStatistics {
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
     * 哪一年月的统计
     */
    String yearMonth
    /**
     * 全勤（天）
     */
    Double allWorkDay
    /**
     * 出勤（天）
     */
    Double workDay
    /**
     * 缺勤（天）
     */
    Integer absenceDay
    /**
     * 迟到时长
     */
    Double lateHours
    /**
     * 迟到次数
     */
    Integer lateTimes
    /**
     * 早退时长
     */
    Double earlyWorkHours
    /**
     * 早退次数
     */
    Integer earlyWorkTimes
    /**
     * 加班（天）
     * 通过计算加班申请单获取结果
     */
    Double overtimeWorkDay
    /**
     * 加班（小时）
     * 通过计算加班申请单获取结果
     */
    Double overTimeHours
    /**
     * 请假（天）
     */
    Double leaveDay
    /**
     * 请假（小时）
     */
    Double leaveHours
    /**
     * 出差（天）
     */
    Double businessTripDay
    /**
     * 外出（小时）
     */
    Double goOutHours
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
                    viewId:'AttendanceStatisticsList',title:'考勤统计',clientType:'pc',viewType:'list',
                    ext:[],//扩展参数值最终进入ViewExtend
                    opt:['attendance_statistics_view','attendance_statistics_export']//视图对应的操作
            ],
            view:[viewId:'AttendanceStatisticsView',title:'查看考勤统计',clientType:'pc',viewType:'form',viewExtend:[]],
            mview:[viewId:'AttendanceStatisticsMview',title:'查看考勤统计',clientType:'mobile',viewType:'form']
    ]
    static list = [
            'employee.name','yearMonth','allWorkDay','workDay','absenceDay','lateHours','earlyWorkHours','overTimeHours',
            'leaveDay','businessTripDay','goOutHours'
    ]
    static view = [
            'id','employee.name','yearMonth','allWorkDay','workDay','absenceDay','lateHours','lateTimes','earlyWorkHours',
            'earlyWorkTimes','overtimeWorkDay','overTimeHours','leaveDay','leaveHours','businessTripDay','goOutHours',
            'dateCreated','lastUpdated'
    ]
    static mview  = view

    static pub = ['user','employee']
    static allfields = list+view+pub

    //默认插入 userFiledExtend中的参数
    static FE = [:]
    //默认插入 ViewDetailExtend中的参数
    static VE = [:]

    static constraints = {
        user nullable:false
        employee nullable:false
        yearMonth attributes:[text:'年月',must:true]
        allWorkDay attributes:[text:'全勤（天）',must:true]
        workDay attributes:[text:'出勤（天）',must:true]
        absenceDay attributes:[text:'缺勤（天）',must:true]
        lateHours attributes:[text:'迟到时长（时）',must: true]
        lateTimes attributes:[text:'迟到次数']
        earlyWorkHours attributes:[text:'早退时长（时）',must: true]
        earlyWorkTimes attributes:[text:'早退次数']
        overtimeWorkDay attributes:[text:'加班（天）']
        overTimeHours attributes:[text:'加班（时）',must:true]
        leaveDay attributes:[text:'请假（天）',must: true]
        leaveHours attributes:[text:'请假（时）']
        businessTripDay attributes:[text:'出差（天）',must: true]
        goOutHours attributes:[text:'外出（时）',must: true]
        dateCreated attributes:[text:'创建时间']
        lastUpdated attributes:[text:'修改时间']
    }

    static mapping = {
        table('t_attendance_statistics')
    }
}
