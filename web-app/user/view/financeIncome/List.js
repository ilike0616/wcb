/**
 * Created by like on 2015/7/9.
 */
Ext.define('user.view.financeIncome.List', {
    extend: 'public.BaseList',
    alias: 'widget.financeIncomeList',
    autoScroll: true,
    store:Ext.create('user.store.FinanceIncomeStore'),
    title: '财务入账',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'FinanceIncomeList'
});