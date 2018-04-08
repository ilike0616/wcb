/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ScheduleEventStore',{
    extend : 'Ext.data.Store',
    model : 'user.model.ScheduleEventModel',
    proxy:{
        type:'ajax',
        noCache: true,
        reader:{
            type:'json',
            root:'data'
        },
        writer:{
            type:'json'
        },
        api:{
            read: 'schedule/list',
            remove: 'schedule/delete',
            update: "schedule/update",
            insert: "schedule/insert",
            save: 'schedule/save',
            insertFollow: 'schedule/insertFollow'
        }
    },
    remoteSort: true,
    autoLoad:true
})