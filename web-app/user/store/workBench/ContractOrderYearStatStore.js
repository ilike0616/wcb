Ext.define('user.store.workBench.ContractOrderYearStatStore',{
    extend : 'Ext.data.Store',
    model:'user.model.CustomerYearStatModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'statistics/contractOrderYearStat'
        }
    },
    autoLoad:true,
    sorters:[{property : 'employeeName'}],
    groupField: 'deptName'
})