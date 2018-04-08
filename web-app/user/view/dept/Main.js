Ext.define('user.view.dept.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.deptMain',
    layout : 'border',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'deptList',
                    title : '部门列表',
                    region:'center',
                    layout : 'fit',
                    split: true
                }
            ]
        });
        me.callParent(arguments);
    }
});