Ext.define('user.store.AuditStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('work_audit'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'audit/list',
            remove: 'audit/delete',
            update: "audit/audit",
            insert: "audit/insert",
            save: 'audit/save'
        }
    },
    autoLoad:false
});