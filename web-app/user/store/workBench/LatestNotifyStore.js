/**
 * Created by guozhen on 2015-04-22.
 */
Ext.define('user.store.workBench.LatestNotifyStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("notify"),
    pageSize:10,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'notify/list?fromWhere=workBench',
            remove: 'notify/delete',
            update: "notify/update",
            insert: "notify/insert",
            save: 'notify/save',
            toRead:'notify/toRead'
        }
    },
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
})