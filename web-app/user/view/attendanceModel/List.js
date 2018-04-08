/**
 * Created by like on 2015-01-13.
 */
Ext.define('user.view.attendanceModel.List', {
    extend: 'public.BaseList',
    alias: 'widget.attendanceModelList',
    autoScroll: true,
    store:Ext.create('user.store.AttendanceModelStore'),
    title: '考勤模板管理',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'AttendanceModelList'
});