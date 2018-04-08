Ext.define('user.controller.CompetitorDynamicController',{
    extend : 'Ext.app.Controller',
    views:['competitorDynamic.List'] ,
    stores:['CompetitorDynamicStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
        	
        });
    }
})