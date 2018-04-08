/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.store.AttendanceDataStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("attendance_data"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'attendanceData/list',
            remove: 'attendanceData/delete',
            update: "attendanceData/update",
            insert: "attendanceData/insert",
            save: 'attendanceData/save'
        }
    },
    autoSync:true,
    autoLoad:false
});