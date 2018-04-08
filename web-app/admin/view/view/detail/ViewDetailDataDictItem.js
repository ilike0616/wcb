Ext.define("admin.view.view.detail.ViewDetailDataDictItem",{
    extend: 'public.BaseWin',
    alias: 'widget.viewDetailDataDictItem',
    autoScroll:true,
    layout:'fit',
    width: 500,
    height: 600,
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                xtype: 'dictItemList'
            }],
            buttons:[{
                text:'关闭',iconCls:'cancel',handler:function(btn){btn.up('window').close();}
            }]
        })
        me.callParent(arguments);
    }
})