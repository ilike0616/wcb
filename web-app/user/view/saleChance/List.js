/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.saleChance.List', {
    extend: 'public.BaseList',
    alias: 'widget.saleChanceList',
    autoScroll: true,
    store:Ext.create('user.store.SaleChanceStore'),
    split:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'SaleChanceList'
});