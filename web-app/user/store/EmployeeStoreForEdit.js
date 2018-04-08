Ext.define('user.store.EmployeeStoreForEdit', {
    extend: 'Ext.data.TreeStore',
    fields : ['name','id','leaf'],
    root: {
        expanded: true,
        text: "员工管理"
    },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employee/employeeForEdit'
        },
        reader:'json'
    }
});
