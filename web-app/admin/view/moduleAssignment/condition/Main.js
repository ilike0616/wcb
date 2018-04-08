Ext.define('admin.view.moduleAssignment.condition.Main', {
    extend: 'Ext.window.Window',
    alias: 'widget.conditionMain',
    layout: 'border',
    height:500,
    width:1000,
    modal:true,
    title:'条件设置',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                region:'west',
                width: 450,
                layout: 'fit',
                xtype: 'conditionList',
                userOperation : me.userOperation,
                user : me.user
            },{
                region:'center',
                layout: 'fit',
                xtype: 'conditionDetailList',
                user : me.user,
                module: me.module
            }
            ]
        })
        me.callParent(arguments);
    }
});