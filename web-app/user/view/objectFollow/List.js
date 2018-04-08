/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.objectFollow.List', {
    extend: 'public.BaseList',
    alias: 'widget.objectFollowList',
    autoScroll: true,
    store:Ext.create('user.store.ObjectFollowStore'),
    title: '对象跟进',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'ObjectFollowList'
});