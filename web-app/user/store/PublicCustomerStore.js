/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.PublicCustomerStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("public_customer"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'publicCustomer/list',
            remove: 'publicCustomer/delete',
            update: "publicCustomer/update",
            insert: "publicCustomer/insert",
            save: 'publicCustomer/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
});