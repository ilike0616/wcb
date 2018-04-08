Ext.define('admin.view.privilege.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.privilegeMain',
    layout: 'border',
    items: [{
            region:'west',
            width: 300,
            collapsible: true,
            layout: 'fit',
            title:'用户列表',
            xtype: 'deptUserList'
        },{
            region:'center',
            split: true,
            layout: 'fit',
            title: '权限列表',
            xtype: 'privilegeList'
        }
    ]
});