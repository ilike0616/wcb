Ext.define('user.view.role.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.roleMain',
    bodyStyle: 'padding:3px',
    layout : {
        type: 'hbox',
        pack : 'start ',
        align: 'stretch'
    },
    defaults: {
        collapsible: true,
        collapseDirection:'left',
        margin : '0 2 0 0'
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'roleList',
                    title : '角色列表',
                    autoScroll : true,
                    split : true,
                    flex : 1
                },
                {
                    title: '权限列表',
                    html:'1111111',
                    flex : 1
                }
            ]
        });
        me.callParent(arguments);
    }
});