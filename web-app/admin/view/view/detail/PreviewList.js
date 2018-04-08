Ext.define("admin.view.view.detail.PreviewList",{
    extend: 'public.BaseWin',
    alias: 'widget.previewList',
    height:300,
    width:900,
    autoScroll:true,
    layout:'fit',
    requires: [
        'public.BaseList'
    ],
    initComponent: function() {
        var me = this;
        title = me.title;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'baseList',
                    viewId:me.viewId,
                    store:Ext.create('user.store.'+me.storeName),
                    url:'view/previewList?viewId='+me.viewId+'&userId='+me.userId,
                    enableComplexQuery:false,
                    enableSearchField:false
                }
            ],
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        })
        me.callParent(arguments);
    }
})