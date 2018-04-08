/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.ObjectFollowController',{
    extend : 'Ext.app.Controller',
    views:['objectFollow.List'] ,
    stores:['ObjectFollowStore','OnsiteObjectStore'],
    models:['ObjectFollowModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'objectFollowList',
            selector : 'objectFollowList'
        },
        {
            ref: 'objectFollowDeleteBtn',
            selector: 'objectFollowList button#deleteButton'
        }
    ],
    init:function() {
    	this.application.getController("OnsiteObjectController");
        this.control({
        	
        });
    }
})
