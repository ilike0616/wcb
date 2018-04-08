/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.AimPerformanceStore',{
    extend : 'Ext.data.TreeStore',
    model:'user.model.AimPerformanceModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'aimPerformance/list'
        },
        reader : {
            type : 'json',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    autoLoad:true,
    folderSort : true,
    root: {
        expanded: true,
        name: "目标业绩"
    }
})