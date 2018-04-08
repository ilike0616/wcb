/**
 * Created by like on 2015/8/18.
 */
Ext.define('user.view.myTask.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.myTaskMain',
    layout: 'border',
    items: [{
        region:'center',
        xtype: 'tabpanel',
        tabPosition: 'top',
        items:[{
            title: '全部',
            taskType:'1',
            xtype:'myTaskList',
            viewId:'MyTaskList',
            store:Ext.create('user.store.MyTaskStore')
        },{
            title: '待办',
            taskType:'2',
            xtype:'myTaskList',
            viewId:'MyTaskList',
            store:Ext.create('user.store.MyTaskStore')
        },{
            title: '已办',
            taskType:'3',
            xtype:'myTaskList',
            viewId:'MyTaskList',
            store:Ext.create('user.store.MyTaskStore')
        }]
    }]
});