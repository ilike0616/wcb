Ext.define("user.view.saleChance.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.saleChanceAdd',
    requires: [
               'public.BaseForm'
    ],
    title: '添加销售商机',
    items: [
       {
           xtype: 'baseForm',
           viewId:'SaleChanceAdd',
           buttons: [{
               text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'saleChanceList'
               }]
           }
       ]
});
