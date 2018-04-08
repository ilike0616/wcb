/**
 * Created by like on 2015-01-22.
 */
Ext.define('user.view.taskAssigned.List', {
    extend: 'public.BaseList',
    alias: 'widget.taskAssignedList',
    autoScroll: true,
    store:Ext.create('user.store.TaskAssignedStore'),
    title: '任务交办',
    split:true,
    forceFit:true,
    viewId:'TaskAssignedList'
});