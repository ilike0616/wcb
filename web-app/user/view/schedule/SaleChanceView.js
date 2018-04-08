/**
 * Created by guozhen on 2015/8/26.
 */
Ext.define("user.view.schedule.SaleChanceView",{
    extend: 'public.BaseWin',
    alias: 'widget.scheduleSaleChanceView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
                readOnly:true
            },
            viewId:'ScheduleSaleChanceView',
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});