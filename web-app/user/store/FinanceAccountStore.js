/**
 * Created by like on 2015/7/9.
 */
Ext.define('user.store.FinanceAccountStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('finance_account'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'financeAccount/list',
            remove: 'financeAccount/delete',
            update: "financeAccount/update",
            insert: "financeAccount/insert",
            save: 'financeAccount/save'
        }
    },
    autoLoad:false
});