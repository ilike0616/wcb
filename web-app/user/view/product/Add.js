Ext.define("user.view.product.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.productAdd',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    title: '添加产品',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ProductAdd',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'productList'
            }]
        }
    ]
});
