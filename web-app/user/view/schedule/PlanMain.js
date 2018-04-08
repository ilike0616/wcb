/**
 * Created by guozhen on 2015/8/27.
 */
Ext.define('user.view.schedule.PlanMain', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.schedulePlanMain',
    layout : 'border',
    requires: [
        'Ext.calendar.CalendarPanel'
    ],
    initComponent: function() {
        var me = this;
        Ext.apply(me, {
            items: [
                {
                    id:'schedulePlanMain-app-center',
                    title: '...', // will be updated to the current view's date range
                    region: 'center',
                    layout: 'border',
                    forceFit:true,
                    items: [{
                        xtype: 'container',
                        itemId:'dpContainer',
                        region: 'west',
                        width:216,
                        defaults:{
                            margin:'0 3 10 0'
                        },
                        items: [{
                            xtype: 'datepicker',
                            cls: 'ext-cal-nav-picker',
                            listeners: {
                                'select': {
                                    fn: function(dp, dt){
                                        me.down('calendarpanel').setStartDate(dt);
                                    },
                                    scope: this
                                }
                            }
                        },{
                            xtype:'panel',
                            layout:'vbox',
                            defaults:{
                              margin:'3 5 0 20'
                            },
                            items:[{
                                xtype: 'displayfield',
                                fieldLabel: '客户跟进',
                                labelWidth:70,
                                value:'<span style="background-color: #306da6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'
                            },{
                                xtype: 'displayfield',
                                fieldLabel: '商机跟进',
                                labelWidth:70,
                                value:'<span style="background-color: #86a723">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'
                            },{
                                xtype: 'displayfield',
                                fieldLabel: '待办任务',
                                labelWidth:70,
                                value:'<span style="background-color: #b6a980">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'
                            }]
                        }]
                    }, {
                        region: 'center',
                        border: false,
                        xtype: 'calendarpanel',
                        eventStore: Ext.create('user.store.ScheduleEventStore'),
                        calendarStore: this.calendarStore,
                        dayText:'天',
                        weekText:'周',
                        monthText:'月',
                        activeItem: 3, // month view
                        dayViewCfg: {
                            todayText:'今天'
                        },
                        weekViewCfg: {
                            todayText:'今天'
                        },
                        monthViewCfg: {
                            showHeader: true,
                            showWeekLinks: true,
                            showWeekNumbers: true,
                            todayText:'今天'
                        }
                    }
                    ]
                }
            ]
        })
        me.callParent(arguments);
    }
});