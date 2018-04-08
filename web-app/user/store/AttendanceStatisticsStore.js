/**
 * Created by guozhen on 2015/04/16.
 */
Ext.define('user.store.AttendanceStatisticsStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("attendance_statistics"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'attendanceStatistics/list'/*,
            remove: 'attendanceStatistics/delete',
            update: "attendanceStatistics/update",
            insert: "attendanceStatistics/insert",
            save: 'attendanceStatistics/save'*/
        }
    },
    autoSync:true,
    autoLoad:false
})