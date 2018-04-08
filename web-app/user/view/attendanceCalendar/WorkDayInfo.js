Ext.define("user.view.attendanceCalendar.WorkDayInfo",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.workDayInfo',
    title:'相关信息',
    layout:{
        align: 'center',
        pack: 'center',
        type: 'vbox'
    },
    items:[{
        xtype: 'panel',
        margin:'10 0 10 0',
        items:[{
            xtype:'form',
            bodyStyle: 'padding:5px 5px 5px',
            border:0,
            layout: {
                type: 'vbox'
            },
            fieldDefaults: {
                labelWidth: 80
            },
            items:[{
                xtype: 'radiogroup',
                fieldLabel: '状态',
                columns: 3,
                width:300,
                items: [
                    { boxLabel: '休', name: 'kind', inputValue: '1'},
                    { boxLabel: '班', name: 'kind', inputValue: '2'}
                ]
            },{
                xtype : 'textareafield',
                grow : true,
                name : 'remark',
                width: 300,
                fieldLabel: '备注'
            },{
                xtype:'hiddenfield',
                name:'attendanceCalendar'
            }],
            buttons: [{
                text: '确定', itemId: 'attendance_calendar_confirm', iconCls: 'table_save'
            },{
                text: '删除', itemId: 'attendance_calendar_delete', iconCls: 'cancel',hidden:true
            }]
        }]
    }]
});
