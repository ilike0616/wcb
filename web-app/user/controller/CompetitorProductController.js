Ext.define('user.controller.CompetitorProductController',{
    extend : 'Ext.app.Controller',
    views:['competitorProduct.List'] ,
    stores:['CompetitorProductStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
        	
        });
    }
})