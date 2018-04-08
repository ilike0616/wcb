/**
 * Created by like on 2015-01-13.
 */
Ext.define('user.controller.LoginLogController',{
    extend : 'Ext.app.Controller',
    views:['loginLog.List'] ,
    stores:['LoginLogStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
        });
    }
})
