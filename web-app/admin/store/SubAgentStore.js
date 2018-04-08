/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.SubAgentStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.SubAgentModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'subAgent/list',
            remove: 'subAgent/delete',
            update: "subAgent/update",
            insert: "subAgent/insert",
            save: 'subAgent/save'
        }
    },
    autoLoad:true
})