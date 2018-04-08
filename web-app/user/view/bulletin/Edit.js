Ext.define("user.view.bulletin.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.bulletinEdit',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            viewId:'BulletinEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'bulletinList'
            }]
        }
    ]
})