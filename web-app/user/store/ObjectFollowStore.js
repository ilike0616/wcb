/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ObjectFollowStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("object_follow"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'objectFollow/list',
            remove: 'objectFollow/delete',
            update: "objectFollow/update",
            insert: "objectFollow/insert",
            save: 'objectFollow/save'
        }
    },
    autoLoad:false
});