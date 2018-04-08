/**
 * Created by guozhen on 2015/04/16.
 */
Ext.define('user.controller.AttendanceStatisticsController',{
    extend : 'Ext.app.Controller',
    views:['attendanceStatistics.List'] ,
    stores:['AttendanceStatisticsStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
        });
    }
})
