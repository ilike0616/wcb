/**
 * Created by like on 2015-04-24.
 */
Ext.define('admin.store.AppVersionStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.AppVersionModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'appVersion/list',
            remove: 'appVersion/delete',
            update: "appVersion/update",
            insert: "appVersion/insert",
            save: 'appVersion/save'
        }
    },
    autoLoad:true
});