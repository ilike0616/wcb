/**
 * Created by like on 2015-04-13.
 */
Ext.define('user.store.NotifyModelStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("notify_model"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'notifyModel/list',
            remove: 'notifyModel/delete',
            update: "notifyModel/update",
            insert: "notifyModel/insert",
            save: 'notifyModel/save',
            notifyField: 'notifyModel/getNotifyFieldList'
        }
    },
    autoLoad:false
});