/**
 * Created by guozhen on 2015/8/26.
 */
Ext.define("user.view.schedule.SaleChanceAdd",{
    extend: 'public.BaseWin',
    alias: 'widget.scheduleSaleChanceAdd',
    requires: [
        'public.BaseForm'
    ],
    title: '添加日程安排',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ScheduleSaleChanceAdd',
            buttons: [{
                text:'保存',itemId:'scheduleCalendarInsert',iconCls:'table_save',autoInsert:false,target:'scheduleMain'
            }]
        }
    ]
});