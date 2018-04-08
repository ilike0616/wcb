/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.view.attendanceData.List', {
    extend: 'public.BaseList',
    alias: 'widget.attendanceDataList',
    autoScroll: true,
    store:Ext.create('user.store.AttendanceDataStore'),
    title: '考勤数据',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'AttendanceDataList'
});