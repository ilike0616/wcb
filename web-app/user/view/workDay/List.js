/**
 * Created by guozhen on 2015/4/21.
 */
Ext.define('user.view.workDay.List', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workDayList',
    title: '考勤工作日',
    forceFit:true,
    layout:'border',
    requires: [
        'Ext.Viewport',
        'Ext.layout.container.Border',
        'Ext.picker.Date',
        'Ext.calendar.util.Date',
        'Ext.calendar.CalendarPanel',
        'Ext.calendar.data.MemoryCalendarStore',
        'Ext.calendar.data.MemoryEventStore',
        'Ext.calendar.data.Events',
        'Ext.calendar.data.Calendars',
        'Ext.calendar.form.EventWindow'
    ],
    initComponent: function(){
        var me = this;
        Ext.apply(me, {
            items: [{
                region: 'center',
                xtype: 'workDayCalendar',
                height:500,
                eventStore: Ext.create('Ext.calendar.data.MemoryEventStore', {
                    data: Ext.calendar.data.Events.getData()
                }),
                calendarStore: Ext.create('Ext.calendar.data.MemoryCalendarStore', {
                    data: Ext.calendar.data.Calendars.getData()
                }),
                border: false,
                id:'app-calendar',
                region: 'center',
                monthText : '月',
                showDayView : false,
                showWeekView : false,
                monthViewCfg: {
                    showHeader: true,
                    showWeekLinks: true,
                    showWeekNumbers: true
                },

                listeners: {
                    'eventclick': {
                        fn: function(vw, rec, el){
                        },
                        scope: me
                    },
                    'eventover': function(vw, rec, el){
                        //console.log('Entered evt rec='+rec.data.Title+', view='+ vw.id +', el='+el.id);
                    },
                    'eventout': function(vw, rec, el){
                        //console.log('Leaving evt rec='+rec.data.Title+', view='+ vw.id +', el='+el.id);
                    },
                    'eventadd': {
                        fn: function(cp, rec){
                            me.showMsg('Event '+ rec.data.Title +' was added');
                        },
                    scope: me
                    },
                    'eventupdate': {
                        fn: function(cp, rec){
                            me.showMsg('Event '+ rec.data.Title +' was updated');
                        },
                        scope: me
                    },
                    'eventcancel': {
                        fn: function(cp, rec){
                        // edit canceled
                        },
                        scope: me
                    },
                    'viewchange': {
                        fn: function(p, vw, dateInfo){
                            if(dateInfo){
                                me.down('workDayCalendar').monthText = Ext.Date.format(dateInfo.viewStart, 'Y-m');
                            }
                        },
                        scope: me
                    },
                    'dayclick': {
                        fn: function(vw, dt, ad, el){
                            alert(el);
                        },
                        scope: me
                    },
                    'rangeselect': {
                        fn: function(win, dates, onComplete){
                        },
                        scope: me
                    },
                    'eventmove': {
                        fn: function(vw, rec){
                        },
                        scope: me
                    },
                    'eventresize': {
                        fn: function(vw, rec){
                        },
                        scope: me
                    },
                    'eventdelete': {
                        fn: function(win, rec){
                        },
                        scope: me
                    },
                    'initdrag': {
                        fn: function(vw){
                        },
                        scope: me
                    }
                }
            }]
        });
        me.callParent(arguments);
    },
    showEditWindow : function(rec, animateTarget){
        var me = this;
        if(!me.editWin){
            me.editWin = Ext.create('Ext.calendar.form.EventWindow', {
                calendarStore: me.calendarStore,
                listeners: {
                    'eventadd': {
                        fn: function(win, rec){
                            win.hide();
                            rec.data.IsNew = false;
                            me.eventStore.add(rec);
                            me.eventStore.sync();
                            me.showMsg('Event '+ rec.data.Title +' was added');
                        },
                        scope: this
                    },
                    'eventupdate': {
                        fn: function(win, rec){
                            win.hide();
                            rec.commit();
                            me.eventStore.sync();
                            me.showMsg('Event '+ rec.data.Title +' was updated');
                        },
                        scope: this
                    },
                    'eventdelete': {
                        fn: function(win, rec){
                            me.eventStore.remove(rec);
                            me.eventStore.sync();
                            win.hide();
                            me.showMsg('Event '+ rec.data.Title +' was deleted');
                        },
                        scope: this
                    },
                    'editdetails': {
                        fn: function(win, rec){
                            win.hide();
                            Ext.getCmp('app-calendar').showEditForm(rec);
                        }
                    }
                }
            });
        }
        me.editWin.show(rec, animateTarget);
    }
},function() {
    /*
     * A few Ext overrides needed to work around issues in the calendar
     */
    Ext.form.Basic.override({
        reset: function() {
            var me = this;
            // This causes field events to be ignored. This is a problem for the
            // DateTimeField since it relies on handling the all-day checkbox state
            // changes to refresh its layout. In general, this batching is really not
            // needed -- it was an artifact of pre-4.0 performance issues and can be removed.
            //me.batchLayouts(function() {
            me.getFields().each(function(f) {
                f.reset();
            });
            //});
            return me;
        }
    });

    // Currently MemoryProxy really only functions for read-only data. Since we want
    // to simulate CRUD transactions we have to at the very least allow them to be
    // marked as completed and successful, otherwise they will never filter back to the
    // UI components correctly.
    Ext.data.MemoryProxy.override({
        updateOperation: function(operation, callback, scope) {
            operation.setCompleted();
            operation.setSuccessful();
            Ext.callback(callback, scope || this, [operation]);
        },
        create: function() {
            this.updateOperation.apply(this, arguments);
        },
        update: function() {
            this.updateOperation.apply(this, arguments);
        },
        destroy: function() {
            this.updateOperation.apply(this, arguments);
        }
    });
});