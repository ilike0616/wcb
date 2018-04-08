/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ServiceTaskStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('service_task'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'serviceTask/list',
            remove: 'serviceTask/delete',
            update: "serviceTask/update",
            insert: "serviceTask/insert",
            save: 'serviceTask/save'
        }
    },
    autoLoad:false
});