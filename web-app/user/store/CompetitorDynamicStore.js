Ext.define('user.store.CompetitorDynamicStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('competitor_dynamic'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'competitorDynamic/list',
            remove: 'competitorDynamic/delete',
            update: "competitorDynamic/update",
            insert: "competitorDynamic/insert",
            save: 'competitorDynamic/save'
        }
    },
    autoLoad:false
})