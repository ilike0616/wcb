/**
 * Created by like on 2015-04-13.
 */
Ext.define('user.view.notifyModel.List', {
    extend: 'public.BaseList',
    alias: 'widget.notifyModelList',
    autoScroll: true,
    store:Ext.create('user.store.NotifyModelStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'NotifyModelList',
    notifyModule:''
});