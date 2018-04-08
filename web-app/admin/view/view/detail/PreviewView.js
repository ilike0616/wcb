Ext.define("admin.view.view.detail.PreviewView",{
    extend: 'public.BaseWin',
    alias: 'widget.previewView',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
        var me = this;
        if(me.paramColumns == 1){
            me.width = 400;
        }
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'baseForm',
                    defaults:{
                        readOnly:true
                    },
                    viewId:me.viewId,
                    url:'view/previewView?viewId='+me.viewId+'&userId='+me.userId,
                    unLocalStorage:true,
                    paramColumns:me.paramColumns,
                    buttons:[{
                        text:'关闭',iconCls:'cancel',
                        handler:function(btn){
                            btn.up('window').close();
                        }
                    }]
                }
            ]
        })
        me.callParent(arguments);
    }
})