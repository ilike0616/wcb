Ext.define('user.store.ContractOrderStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('contract_order'),
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
    autoLoad:false
});