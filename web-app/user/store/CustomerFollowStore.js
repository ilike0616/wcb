/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.CustomerFollowStore',{
    extend : 'Ext.data.Store',
//    model:'user.model.CustomerFollowModel',
    model : modelFactory.getModelByModuleId("customer_follow"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'customerFollow/list',
            remove: 'customerFollow/delete',
            update: "customerFollow/update",
            insert: "customerFollow/insert",
            save: 'customerFollow/save'
        }
    },
    autoSync:true,
    autoLoad:false
})