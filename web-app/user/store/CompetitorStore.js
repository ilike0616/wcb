Ext.define('user.store.CompetitorStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('competitor'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'competitor/list',
            remove: 'competitor/delete',
            update: "competitor/update",
            insert: "competitor/insert",
            save: 'competitor/save'
        }
    },
    autoLoad:false
})