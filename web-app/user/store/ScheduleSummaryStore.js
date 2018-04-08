/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ScheduleSummaryStore',{
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
            read: 'schedule/summaryList'
        }
    },
    remoteSort: true,
    autoLoad:true
})