/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.PortalStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.PortalModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'portal/list',
            remove: 'portal/delete',
            update: "portal/update",
            insert: "portal/insert"
        }
    },
    autoLoad:true
})