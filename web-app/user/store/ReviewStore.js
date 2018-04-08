/**
 * Created by like on 2015/8/12.
 */
Ext.define('user.store.ReviewStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("review"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'review/list',
            remove: 'review/delete',
            update: "review/update",
            insert: "review/insert",
            save: 'review/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
});