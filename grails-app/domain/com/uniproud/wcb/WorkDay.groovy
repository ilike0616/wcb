package com.uniproud.wcb
/**
 * 工作日表(调休表)
 * 一般情况下，周一 至 周五为工作日。周六周日为休息日。
 * 我们首先可以在AttendanceModel中设置，是否仍然按照此规则处理。
 * 提供了，把任意组合做为工作日或者休息日的设置
 * 此表中主要为调休，如正常情况下，周一到周五为工作日，周六周日为休息日：
 * 但是由于清明放假，所以下周一也要标记为休息日。
 * 出现这种情况，就可以在此表中进行标注。
 * 反之，如果要把休息日，更改为工作日，也一样可以在此表中进行标注。
 *
 */
class WorkDay {
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
     * 所属日历
     */
    AttendanceCalendar attendanceCalendar
    /**
     * 1.把工作日标注为休息日
     * 2.把休息日标注为工作日
     */
    Integer kind
    /**
     * 需要调整的日期
     */
    Date workDate
    /**
     * 备注信息
     */
    String remark

    static constraints = {
        user nullable:false
        employee nullable:false
    }

    static mapping = {
        table('t_work_day')
    }
}
