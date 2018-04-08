/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.store.WorkDayStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("work_day"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            /*read: 'workDay/list',
            remove: 'workDay/delete',
            update: "workDay/update",
            insert: "workDay/insert",
            save: 'workDay/save'*/
        }
    },
    autoSync:true,
    autoLoad:false
});