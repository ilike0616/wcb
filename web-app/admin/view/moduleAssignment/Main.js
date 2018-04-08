Ext.define('admin.view.moduleAssignment.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.moduleAssignmentMain',
    layout: 'border',
    items: [{
            region:'west',
            width: 300,
            collapsible: true,
            layout: 'fit',
            title:'用户列表',
            store: 'UserNotUseSysTplStore',
            xtype: 'deptUserList'
        },{
            region:'center',
            layout: 'fit',
            title: '模块列表',
            xtype: 'moduleAssignmentList'
        },{
            region:'east',
            width:400,
            xtype:'tabpanel',
            layout: 'fit',
            items:[
                {
                    xtype: 'enableOperationList',
                    title: '操作列表'
                },{
                    xtype: 'enableUserPortalList',
                    title: '用户Portal'
                }
            ]
        }
    ]
});