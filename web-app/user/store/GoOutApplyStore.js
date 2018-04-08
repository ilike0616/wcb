Ext.define('user.store.GoOutApplyStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('goout_apply'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'goOutApply/list',
            remove: 'goOutApply/delete',
            update: "goOutApply/update",
            insert: "goOutApply/insert",
            save: 'goOutApply/save',
            reAudit:'goOutApply/reAudit'
        }
    },
    autoLoad:false
});