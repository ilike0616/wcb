/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.store.EmployeeNotifyModelForbidStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("employee_notify_model"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employeeNotifyModel/forbidList'
        }
    },
    autoLoad:false
});