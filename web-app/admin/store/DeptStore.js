/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("admin.store.DeptStore",{
    extend : 'Ext.data.TreeStore',
    model : 'admin.model.DeptModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy : {
        type : 'proxyTemplate',
        api:{
            read: 'dept/listForAdmin',
            remove: 'dept/delete',
            update: "dept/update",
            insert: "dept/insert",
            save: 'dept/save'
        },
        reader : {
            type : 'json',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    folderSort : true,
    root: {
        expanded: true,
        name: "部门管理"
    }
})
