/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.workBench.LatestCustomerStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("customer"),
    pageSize:10,  //每页显示的记录行数
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
})