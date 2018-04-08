/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("user.store.EmployeeTreeStore",{
    extend : 'Ext.data.TreeStore',
    model : modelFactory.getModelByModuleId('employee'),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy : {
        type : 'proxyTemplate',
        api:{
            read: 'employee/treeList'
        },
        reader : {
            type : 'json',
            root: 'data',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    autoLoad: false,
    folderSort : true,
    root: {
        expanded: true,
        name: "员工管理"
    }
})
