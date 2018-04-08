/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.EmployeeStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.EmployeeModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    pageSize:15,  //每页显示的记录行数
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employee/listForAdmin',
            remove: 'employee/delete',
            update: "employee/update",
            insert: "employee/insert",
            save: 'employee/save'
        }
    },
    remoteSort: true,
    autoLoad:true
})