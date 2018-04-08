Ext.define('user.store.UserTreeStore', {
    extend: 'Ext.data.TreeStore',
    fields:['text','name','ctrl','view','viewId','moduleId'],
    defaultRootId : 'root',
    autoLoad:false,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        url:'menu/userTree',
        reader:'json'
    }
});
