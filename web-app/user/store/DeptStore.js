/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("user.store.DeptStore",{
    extend : 'Ext.data.TreeStore',
    model : modelFactory.getModelByModuleId("dept"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy : {
        type : 'proxyTemplate',
        api:{
            read: 'dept/list',
            remove: 'dept/delete',
            update: "dept/update",
            insert: "dept/insert",
            save: 'dept/save'
        },
        reader : {
            type : 'json',
            root: 'data',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    autoLoad: true,
    folderSort : true,
    root: {
        expanded: true,
        name: "部门管理"
    }
})
