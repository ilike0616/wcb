/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.UserPortalStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.UserPortalModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userPortal/list',
            remove: 'userPortal/delete',
            update: "userPortal/update",
            insert: "userPortal/insert",
            save:"userPortal/save"
        }
    },
    autoLoad:true
})