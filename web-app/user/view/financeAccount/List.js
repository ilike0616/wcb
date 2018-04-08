/**
 * Created by like on 2015/7/9.
 */
Ext.define('user.view.financeAccount.List', {
    extend: 'public.BaseList',
    alias: 'widget.financeAccountList',
    autoScroll: true,
    store:Ext.create('user.store.FinanceAccountStore'),
    title: '财务账户',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'FinanceAccountList'
});