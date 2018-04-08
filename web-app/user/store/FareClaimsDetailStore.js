/**
 * Created by like on 2015/7/7.
 */
Ext.define('user.store.FareClaimsDetailStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('fare_claims_detail'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'fareClaims/detailList'
        }
    },
    autoLoad:false
});