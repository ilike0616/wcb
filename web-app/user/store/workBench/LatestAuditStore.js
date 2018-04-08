Ext.define('user.store.workBench.LatestAuditStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('work_audit'),
    pageSize:10,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'audit/list?auditType=1',
            remove: 'audit/delete',
            update: "audit/audit",
            insert: "audit/insert",
            save: 'audit/save'
        }
    },
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
})