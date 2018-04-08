/**
 * Created by like on 2015/8/27.
 */
Ext.define('user.view.sfa.List', {
    extend: 'public.BaseList',
    alias: 'widget.sfaList',
    autoScroll: true,
    store:Ext.create('user.store.SfaStore'),
    title: 'SFA方案',
    split:true,
    forceFit:true,
    viewId:'SfaList'
});