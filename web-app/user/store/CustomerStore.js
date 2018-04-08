/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.CustomerStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("customer"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'customer/list',
            remove: 'customer/delete',
            update: "customer/update",
            insert: "customer/insert",
            save: 'customer/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
});