Ext.define('user.store.DeptStoreForEdit', {
    extend: 'Ext.data.TreeStore',
    fields : ['text','id','leaf','user.id'],
    root: {
        expanded: true,
        text: "部门管理"
    },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'dept/departmentsForEdit'
        },
        reader:'json'
    }
});
