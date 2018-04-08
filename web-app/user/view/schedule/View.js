/**
 * Created by guozhen on 2015/8/26.
 */
Ext.define("user.view.schedule.View",{
    extend: 'public.BaseWin',
    alias: 'widget.scheduleView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
                readOnly:true
            },
            viewId:'ScheduleView',
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});