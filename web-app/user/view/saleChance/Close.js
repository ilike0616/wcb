Ext.define("user.view.saleChance.Close",{
    extend: 'public.BaseWin',
    alias: 'widget.saleChanceClose',
    requires: [
        'public.BaseForm'
    ],
    title: '关闭销售商机',
    items: [
        {
            xtype: 'baseForm',
            viewId:'SaleChanceClose',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save'
            }]
        }
    ]
});
