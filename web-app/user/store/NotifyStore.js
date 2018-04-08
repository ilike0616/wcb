/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.store.NotifyStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("notify"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'notify/list',
            remove: 'notify/delete',
            update: "notify/update",
            insert: "notify/insert",
            save: 'notify/save',
            toRead:'notify/toRead'
        }
    },
    autoLoad:false
});