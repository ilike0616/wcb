/**
 * Created by guozhen on 2015/04/16.
 */
Ext.define('user.controller.AttendanceCalendarController',{
    extend : 'Ext.app.Controller',
    views:['attendanceCalendar.Main','attendanceCalendar.List','attendanceCalendar.AttendanceCalendar','attendanceCalendar.WorkDayInfo'] ,
    stores:['AttendanceCalendarStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
            'attendanceCalendarMain attendanceCalendarList':{
                select:function(selectModel, record, index, eOpts) {
                    var main = eOpts.up('attendanceCalendarMain');
                    var calendar = main.down('attendanceCalendar');
                    var workDayInfo = main.down('workDayInfo');
                    workDayInfo.down('form').getForm().reset();
                    calendar.attendanceCalendar = record.get('id');
                    workDayInfo.down('hiddenfield[name=attendanceCalendar]').setValue(record.get('id'));
                    calendar.fullUpdate();
                }
            },
            'attendanceCalendarMain workDayInfo button#attendance_calendar_confirm':{
                click:function(btn) {
                    var main = btn.up('attendanceCalendarMain');
                    var attendanceCalendar = main.down('attendanceCalendar');
                    var workDayInfo = main.down('workDayInfo');
                    var kind = workDayInfo.down('radiogroup').getValue();
                    var remark = workDayInfo.down('textarea').getValue();
                    var attendanceCalendarId = workDayInfo.down('hiddenfield').getValue();
                    if(attendanceCalendarId == "" || Ext.typeOf(kind.kind) == "undefined") return;
                    Ext.Ajax.request({
                        url: 'attendanceCalendar/insertOrUpdWorkDay',
                        params:{
                            kind : kind,
                            remark : remark,
                            workDate : attendanceCalendar.getValue(),
                            attendanceCalendar:attendanceCalendarId
                        },
                        method: 'POST',
                        timeout: 4000,
                        async: false,
                        success: function (response, opts) {
                            attendanceCalendar.fullUpdate(attendanceCalendar.getValue());
                        }
                    });
                }
            },
            'attendanceCalendarMain workDayInfo button#attendance_calendar_delete': {
                click: function (btn) {
                    var main = btn.up('attendanceCalendarMain');
                    var attendanceCalendar = main.down('attendanceCalendar');
                    Ext.Ajax.request({
                        url: 'attendanceCalendar/deleteWorkDay',
                        params:{
                            workDate : attendanceCalendar.getValue(),
                            attendanceCalendar:attendanceCalendar.attendanceCalendar
                        },
                        method: 'POST',
                        timeout: 4000,
                        async: false,
                        success: function (response, opts) {
                            attendanceCalendar.fullUpdate(attendanceCalendar.getValue());
                            main.down('workDayInfo').down('form').getForm().reset();
                        }
                    });
                }
            }
        });
    }
})
