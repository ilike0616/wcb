Ext.define('admin.view.role.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.roleMain',
    autoScroll : true,
    bodyStyle: 'padding:3px',
    itemId : 'roleMain',
    baseCls:'x-plain main-panel',
    layout : {
        type: 'table',
        pack: 'start',
        columns: 2
    },
    defaults: {
        frame:true
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'deptUserList',
                    title: '用户列表',
                    width:200,
                    height:600,
                    tdAttrs : {
                        style : 'vertical-align:top'
                    },
                    rowspan : 2
                },
                {
                    xtype: 'roleList',
                    title : '角色列表',
                    width: 920,
                    height:400
                },
                {
                    xtype: 'privilegeList',
                    title: '权限列表',
                    store:'PrivilegeStore',
                    width:920,
                    height:400
                }
            ]
        });
        me.callParent(arguments);
    }
});