/**
 * Created by like on 2015-04-24.
 */
Ext.define('admin.controller.AppVersionController',{
    extend : 'Ext.app.Controller',
    views:['appVersion.List','appVersion.Add','appVersion.Edit'] ,
    stores:['AppVersionStore'],
    models:['AppVersionModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({

        });
    }
})