Ext.define("user.view.product.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.productEdit',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    title: '修改产品',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ProductEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'productList'
            }]
        }
    ]
});
