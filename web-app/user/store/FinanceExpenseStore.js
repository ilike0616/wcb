/**
 * Created by like on 2015/7/10.
 */
Ext.define('user.store.FinanceExpenseStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('finance_expense'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'financeExpense/list',
            remove: 'financeExpense/delete',
            update: "financeExpense/update",
            insert: "financeExpense/insert",
            charge: 'financeExpense/charge',
            forbid: 'financeExpense/forbid',
            wrong: 'financeExpense/wrong',
            reAudit:'financeExpense/reAudit'
        }
    },
    autoLoad:false
});