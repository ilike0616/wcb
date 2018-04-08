Ext.define('admin.view.userMenu.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.userMenuMain',
    layout: 'border',
    items: [{
            region:'west',
            split: true,
            collapsible: true,
            xtype: 'deptUserList',
            store: 'UserNotUseSysTplStore',
            width : '25%',
            title: '用户列表'
        },{
            region:'center',
            split: true,
            layout: 'fit',
            xtype: 'userMenuList',
            title : '菜单列表'
        }]
});