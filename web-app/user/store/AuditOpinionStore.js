Ext.define('user.store.AuditOpinionStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('audit_opinion'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'audit/listOpinion'
//            remove: 'audit/delete',
//            update: "audit/update",
//            insert: "audit/insert",
//            save: 'audit/save'
        }
    },
    autoLoad:false
})