Ext.define('user.view.attendanceCalendar.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.attendanceCalendarMain',
    layout: 'border',
    items: [
        {
            xtype: 'attendanceCalendarList',
            store:Ext.create('user.store.AttendanceCalendarStore'),
            enableComplexQuery:false,
            region: 'west',
            width: 500
        },
        {
            xtype: 'panel',
            layout:'border',
            region: 'center',
            items:[{
                region:'center',
                xtype:'attendanceCalendar'
            },{
                region:'south',
                xtype:'workDayInfo'
            }]
        }
    ]
});