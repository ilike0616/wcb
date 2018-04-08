Ext.define('user.store.LocusStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('locus'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'locus/list',
            remove: 'locus/delete',
            update: "locus/update",
            insert: "locus/insert",
            save: 'locus/save',
            detail : 'locus/detailList'
        }
    },
    autoLoad:false
});