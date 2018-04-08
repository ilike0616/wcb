Ext.define('user.store.MarketActivityStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('market_activity'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'marketActivity/list',
            remove: 'marketActivity/delete',
            update: "marketActivity/update",
            insert: "marketActivity/insert",
            save: 'marketActivity/save'
        }
    },
    autoLoad:false
});