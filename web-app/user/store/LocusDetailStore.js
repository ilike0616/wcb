Ext.define('user.store.LocusDetailStore',{
    extend : 'Ext.data.Store',
    model : 'user.model.LocusDetailModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'locus/detailList'
        }
    },
    autoLoad:false
});