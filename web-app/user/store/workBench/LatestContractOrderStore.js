Ext.define('user.store.workBench.LatestContractOrderStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('contract_order'),
    pageSize:10,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'contractOrder/list',
            remove: 'contractOrder/delete',
            update: "contractOrder/update",
            insert: "contractOrder/insert",
            save: 'contractOrder/save'
        }
    },
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
})