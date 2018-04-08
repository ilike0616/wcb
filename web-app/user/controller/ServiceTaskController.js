/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.ServiceTaskController',{
    extend : 'Ext.app.Controller',
    views:['serviceTask.List'] ,
    stores:['ServiceTaskStore','CustomerStore','ContactStore'],
    models:['ServiceTaskModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
       
    ],
    init:function() {
        this.control({
            
        });
    }
})
