/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.AttendanceCalendarStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("attendance_calendar"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'attendanceCalendar/list',
            remove: 'attendanceCalendar/delete',
            update: "attendanceCalendar/update",
            insert: "attendanceCalendar/insert",
            save: 'attendanceCalendar/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:true,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
});