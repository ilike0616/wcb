/**
 * Created by like on 2015/7/7.
 */
Ext.define('user.store.FareClaimsStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('fare_claims'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'fareClaims/list',
            remove: 'fareClaims/delete',
            update: "fareClaims/update",
            insert: "fareClaims/insert",
            save: 'fareClaims/save',
            reAudit:'fareClaims/reAudit'
        }
    },
    autoLoad:false
});