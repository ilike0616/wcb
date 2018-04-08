/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('agent.store.LoginLogStore',{
    extend : 'Ext.data.Store',
    model : 'agent.model.LoginLogModel',
    proxy:{
        type:'ajax',
        api:{
            read: 'loginLog/listForAgent'
        },
        reader:{
            type:'json',
            root:'data',
            successProperty:'success',
            totalProperty:'total'
        },
        writer:{
            type:'json'
        },
        simpleSortMode: true
    },
    autoLoad:true
});