Ext.define('user.store.OvertimeApplyStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('overtime_apply'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'overtimeApply/list',
            remove: 'overtimeApply/delete',
            update: "overtimeApply/update",
            insert: "overtimeApply/insert",
            save: 'overtimeApply/save',
            reAudit:'overtimeApply/reAudit'
        }
    },
    autoLoad:false
});