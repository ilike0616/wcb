Ext.define('user.store.CompetitorProductStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('competitor_product'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'competitorProduct/list',
            remove: 'competitorProduct/delete',
            update: "competitorProduct/update",
            insert: "competitorProduct/insert",
            save: 'competitorProduct/save'
        }
    },
    autoLoad:false
})