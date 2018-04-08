/**
 * Created by like on 2015/7/9.
 */
Ext.define('user.view.financeExpense.List', {
    extend: 'public.BaseList',
    alias: 'widget.financeExpenseList',
    autoScroll: true,
    store:Ext.create('user.store.FinanceExpenseStore'),
    title: '财务出账',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'FinanceExpenseList'
});