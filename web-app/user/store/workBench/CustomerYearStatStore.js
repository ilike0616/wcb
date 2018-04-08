Ext.define('user.store.workBench.CustomerYearStatStore',{
    extend : 'Ext.data.Store',
    model:'user.model.CustomerYearStatModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'statistics/customerYearStat'
        }
    },
    autoLoad:true,
    sorters:[{property : 'employeeName'}],
    groupField: 'deptName'
})