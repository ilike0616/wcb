/**
 * Created by like on 2015/8/18.
 */
Ext.define("user.view.myTask.View",{
    extend: 'public.BaseWin',
    alias: 'widget.myTaskView',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'baseForm',
                    viewId:'MyTaskView',
                    defaults:{
                        readOnly:true
                    },
                    buttons:[{
                        text:'关闭',iconCls:'cancel',
                        handler:function(btn){
                            btn.up('window').close();
                        }
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
});