Ext.define('user.view.product.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.productMain',
    layout : 'border',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'productProductKindList',
                    title : '产品分类',
                    region:'west',
                    layout : 'fit',
                    split: true,
                    collapsible: true,
                    width:300
                },{
                    xtype: 'productList',
                    title : '产品列表',
                    region:'center',
                    layout : 'fit',
                    split: true
                }
            ]
        });
        me.callParent(arguments);
    }
});