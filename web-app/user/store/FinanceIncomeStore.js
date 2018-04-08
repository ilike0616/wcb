/**
 * Created by like on 2015/7/10.
 */
Ext.define('user.store.FinanceIncomeStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('finance_income'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'financeIncome/list',
            remove: 'financeIncome/delete',
            update: "financeIncome/update",
            insert: "financeIncome/insert",
            charge: 'financeIncome/charge',
            forbid: 'financeIncome/forbid',
            wrong: 'financeIncome/wrong',
            reAudit:'financeIncome/reAudit'
        }
    },
    autoLoad:false
});