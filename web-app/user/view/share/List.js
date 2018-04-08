/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.share.List', {
    extend: 'public.BaseList',
    alias: 'widget.shareList',
    autoScroll: true,
    store:Ext.create('user.store.ShareStore'),
    title: '分享管理',
    split:true,
    alertName:'subject',
    renderLoad:true,
    viewId:'ShareList'
});