Ext.define('user.store.LeaveApplyStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('leave_apply'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'leaveApply/list',
            remove: 'leaveApply/delete',
            update: "leaveApply/update",
            insert: "leaveApply/insert",
            save: 'leaveApply/save',
            reAudit:'leaveApply/reAudit'
        }
    },
    autoLoad:false
})