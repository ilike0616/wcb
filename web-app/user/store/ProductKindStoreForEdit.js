Ext.define('user.store.ProductKindStoreForEdit', {
    extend: 'Ext.data.TreeStore',
    fields : ['text','id','leaf','user.id'],
    root: {
        expanded: true,
        text: "产品分类"
    },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'productKind/productKindsForEdit'
        },
        reader:'json'
    }
});
