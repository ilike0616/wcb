/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.OnsiteObjectController',{
    extend : 'Ext.app.Controller',
    views:['onsiteObject.List'] ,
    stores:['OnsiteObjectStore','ContactStore'],
    models:['OnsiteObjectModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'onsiteObjectList',
            selector : 'onsiteObjectList'
        },
        {
            ref: 'onsiteObjectDeleteBtn',
            selector: 'onsiteObjectList button#deleteButton'
        }
    ],
    init:function() {
        this.application.getController("CustomerController");
    	this.application.getController("ObjectFollowController");
        this.control({
        	'onsiteObjectList':{
	    		select:function(selectModel, record, index, eOpts){
	    			eOpts.up('baseList').initValues = [{id:'onsiteObject.id',value:record.get('id')},{id:'onsiteObject.name',value:record.get('name')}];
	    		}
        	}
        });
    }
})
