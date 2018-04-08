/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.serviceTask.List', {
    extend: 'public.BaseList',
    alias: 'widget.serviceTaskList',
    autoScroll: true,
    store:Ext.create('user.store.ServiceTaskStore'),
    title: '服务派单',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'ServiceTaskList'
});