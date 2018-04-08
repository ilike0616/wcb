Ext.define('user.controller.MarketActivityController',{
    extend : 'Ext.app.Controller',
    views:['marketActivity.List'] ,
    stores:['MarketActivityStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
        	
        });
    }
})