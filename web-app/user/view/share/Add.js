Ext.define("user.view.share.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.shareAdd',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            viewId:'ShareAdd',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'shareList'
            }]
        }
    ]
})