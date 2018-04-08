/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.saleChanceFollow.List', {
    extend: 'public.BaseList',
    alias: 'widget.saleChanceFollowList',
    autoScroll: true,
    alertName: 'subject',
    store:Ext.create('user.store.SaleChanceFollowStore'),
    title: '商机跟进',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'SaleChanceFollowList'
});