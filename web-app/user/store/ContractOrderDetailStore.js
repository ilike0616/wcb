Ext.define('user.store.ContractOrderDetailStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('contract_order_detail'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'contractOrder/detailList'
        }
    },
    autoLoad:false
});