Ext.define('user.controller.CompetitorController',{
    extend : 'Ext.app.Controller',
    views:['competitor.List'] ,
    stores:['CompetitorStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
        	
        });
    }
})