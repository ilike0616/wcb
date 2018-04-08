Ext.define('admin.store.TreeStore', {
    extend: 'Ext.data.TreeStore',
    fields:['text','name','ctrl','view'],
    defaultRootId : 'root',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        url:'menu/tree',
        reader:'json'
    }
});
