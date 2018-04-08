/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.controller.WorkDayController',{
    extend : 'Ext.app.Controller',
    views:['workDay.List','workDay.Calendar','workDay.Month'] ,
    stores:['WorkDayStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
        });
    }
})
