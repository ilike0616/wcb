Ext.define('user.view.taskAssigned.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.taskAssignedMain',
    layout: 'border',
    items: [{
        region:'center',
        xtype: 'tabpanel',
        tabPosition: 'top',
        items:[{
                title: '全部',
                taskType:'1',
                xtype:'taskAssignedList',
                viewId:'TaskAssignedList',
                store:Ext.create('user.store.TaskAssignedStore')
            },{
                title: '待办',
                taskType:'2',
                xtype:'taskAssignedList',
                viewId:'TaskAssignedList',
                store:Ext.create('user.store.TaskAssignedStore')
            },{
                title: '已办',
                taskType:'3',
                xtype:'taskAssignedList',
                viewId:'TaskAssignedList',
                store:Ext.create('user.store.TaskAssignedStore')
            }]
        }]
});