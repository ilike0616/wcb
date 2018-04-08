Ext.define('admin.store.ModuleStoreForEditCheck', {
    extend: 'Ext.data.TreeStore',
    fields : ['text','id','leaf','moduleId'],
    root: {
        expanded: true,
        text: "模块管理"
    },
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'module/modulesForEditCheck'
        },
        reader:'json'
    }
});
