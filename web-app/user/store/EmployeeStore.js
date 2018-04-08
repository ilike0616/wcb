/**
 * Created by guozhen on 2014/6/19.
 * 查询所有的员工并且不分页，如：员工组织架构
 */
Ext.define('user.store.EmployeeStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('employee'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employee/list',
            remove: 'employee/delete',
            update: "employee/update",
            insert: "employee/insert",
            save: 'employee/save'
        }
    },
    autoLoad:true
});