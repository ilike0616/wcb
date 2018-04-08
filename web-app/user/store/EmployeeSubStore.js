/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.EmployeeSubStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('employee'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employee/listSub',
            remove: 'employee/delete',
            update: "employee/update",
            insert: "employee/insert",
            save: 'employee/save',
            initPassword: 'employee/initPassword'
        }
    },
    remoteSort: true,
    autoLoad:true
});