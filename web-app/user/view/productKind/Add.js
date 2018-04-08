Ext.define("user.view.productKind.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.productKindAdd',
    requires: [
        'public.BaseForm'
    ],
    title: '添加产品分类',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ProductKindAdd',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'productKindList'
            }]
        }
    ]
});