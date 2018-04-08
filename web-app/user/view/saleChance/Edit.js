Ext.define("user.view.saleChance.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.saleChanceEdit',
    requires: [
        'public.BaseForm'
    ],
    title: '修改销售商机',
    items: [
        {
            xtype: 'baseForm',
            viewId:'SaleChanceEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'saleChanceList'
            }]
        }
    ]
});
