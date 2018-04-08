/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.AimPerformanceDetailStore',{
    extend : 'Ext.data.Store',
    model:'user.model.AimPerformanceDetailModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'aimPerformance/detailList'
        }
    },
    autoLoad:true
})