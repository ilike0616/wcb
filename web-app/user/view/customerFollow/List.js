/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.customerFollow.List', {
    extend: 'public.BaseList',
    alias: 'widget.customerFollowList',
    autoScroll: true,
    alertName: 'subject',
    store:Ext.create('user.store.CustomerFollowStore'),
    title: '客户跟进',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'CustomerFollowList'
});