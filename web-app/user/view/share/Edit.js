Ext.define("user.view.share.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.shareEdit',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            viewId:'ShareEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'shareList'
            }]
        }
    ]
})