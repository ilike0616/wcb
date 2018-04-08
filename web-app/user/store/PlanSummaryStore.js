/**
 * Created by guozhen on 2014/11/13.
 */
Ext.define('user.store.PlanSummaryStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("plan_summary"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'planSummary/list',
            remove: 'planSummary/delete',
            update: "planSummary/update",
            insert: "planSummary/insert",
            save: 'planSummary/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
});