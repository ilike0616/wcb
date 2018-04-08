/**
 * Created by guozhen on 2015/8/26.
 */
Ext.define("user.view.schedule.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.scheduleEdit',
    requires: [
        'public.BaseForm'
    ],
    title: '修改日程安排',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ScheduleEdit',
            buttons: [{
                text:'保存',itemId:'scheduleCalendarUpdate',iconCls:'table_save',autoUpdate:false,target:'scheduleMain'
            }]
        }
    ]
});