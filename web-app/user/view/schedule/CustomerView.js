/**
 * Created by guozhen on 2015/8/26.
 */
Ext.define("user.view.schedule.CustomerView",{
    extend: 'public.BaseWin',
    alias: 'widget.scheduleCustomerView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
                readOnly:true
            },
            viewId:'ScheduleCustomerView',
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});