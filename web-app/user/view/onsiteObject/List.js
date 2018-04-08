/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.onsiteObject.List', {
    extend: 'public.BaseList',
    alias: 'widget.onsiteObjectList',
    autoScroll: true,
    store: Ext.create('user.store.OnsiteObjectStore'),
    title: '现场对象',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'name',
    viewId:'OnsiteObjectList'
});