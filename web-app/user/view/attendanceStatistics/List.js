/**
 * Created by like on 2015-01-13.
 */
Ext.define('user.view.attendanceStatistics.List', {
    extend: 'public.BaseList',
    alias: 'widget.attendanceStatisticsList',
    autoScroll: true,
    store:Ext.create('user.store.AttendanceStatisticsStore'),
    title: '考勤统计',
    split:true,
    forceFit:true,
    renderLoad:true,
    enableStatisticBtn:false,
    viewId:'AttendanceStatisticsList'
});