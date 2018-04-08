/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.workBench.LatestCustomerFollowStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("customer_follow"),
    pageSize: 10,
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
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
})