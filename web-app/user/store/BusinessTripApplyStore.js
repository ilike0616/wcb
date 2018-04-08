Ext.define('user.store.BusinessTripApplyStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('business_trip_apply'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'businessTripApply/list',
            remove: 'businessTripApply/delete',
            update: "businessTripApply/update",
            insert: "businessTripApply/insert",
            save: 'businessTripApply/save',
            reAudit:'businessTripApply/reAudit'
        }
    },
    autoLoad:false
});