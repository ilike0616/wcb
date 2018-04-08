/**
 * Created by guozhen on 2015/04/10.
 */
Ext.define('user.store.AttendanceModelStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("attendance_model"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'attendanceModel/list',
            remove: 'attendanceModel/delete',
            update: "attendanceModel/update",
            insert: "attendanceModel/insert",
            save: 'attendanceModel/save'
        }
    },
    autoSync:true,
    autoLoad:false
});