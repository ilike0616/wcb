/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.controller.UserFeedbackController',{
    extend : 'Ext.app.Controller',
    views:['userFeedback.List'] ,
    stores:['UserFeedbackStore'],
    models:['UserFeedbackModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'userFeedbackList combo[name=user]':{
                change:function(combo, newValue, oldValue, eOpts ){
                    var o = combo.up('userFeedbackList');
                    var store = o.getStore();
                    Ext.apply(store.proxy.extraParams, {userId:newValue});
                    store.load();
                }
            }
        });
    }
})
