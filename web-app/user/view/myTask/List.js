/**
 * Created by like on 2015/8/18.
 */
Ext.define('user.view.myTask.List', {
    extend: 'public.BaseList',
    alias: 'widget.myTaskList',
    autoScroll: true,
    store:Ext.create('user.store.MyTaskStore'),
    title: '我的任务',
    split:true,
    forceFit:true,
    viewId:'MyTaskList'
});