/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.attendanceCalendar.List', {
    extend: 'public.BaseList',
    alias: 'widget.attendanceCalendarList',
    autoScroll: true,
    store:Ext.create('user.store.AttendanceCalendarStore'),
    split:true,
    renderLoad:true,
    viewId:'AttendanceCalendarList'
});