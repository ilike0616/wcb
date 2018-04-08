Ext.define('user.store.RoleStoreForEdit', {
    extend: 'Ext.data.TreeStore',
    fields : ['text','id','leaf'],
    root: {
        expanded: true,
        text: "角色"
    },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'role/roleForEdit'
        },
        reader:'json'
    }
});
