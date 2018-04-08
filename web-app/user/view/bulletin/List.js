/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.bulletin.List', {
    extend: 'public.BaseList',
    alias: 'widget.bulletinList',
    autoScroll: true,
    store:Ext.create('user.store.BulletinStore'),
    title: '内部公告管理',
    split:true,
    alertName:'subject',
    renderLoad:true,
    viewId:'BulletinList'
});