/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.AgentStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.AgentModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'agent/list',
            remove: 'agent/delete',
            update: "agent/update",
            insert: "agent/insert",
            save: 'agent/save'
        }
    },
    autoLoad:true
});