/**
 * Created by guozhen on 2014/11/13.
 */
Ext.define('user.controller.PlanSummaryController',{
    extend : 'Ext.app.Controller',
    views:['planSummary.Main','planSummary.List','planSummary.Add','planSummary.Edit','planSummary.View'] ,
    stores:['PlanSummaryStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'planSummaryList',
            selector : 'planSummaryList'
        },
        {
            ref:'planSummaryEditForm',
            selector:'planSummaryEdit form'
        },
        {
            ref:'planSummaryEditWin',
            selector:'planSummaryEdit'
        },
        {
            ref: 'planSummaryDeleteBtn',
            selector: 'planSummaryList button#deleteButton'
        },
        {
            ref: 'planSummaryUpdateBtn',
            selector: 'planSummaryList button[itemId=updateButton]'
        }
    ],
    init:function() {
        this.control({
            'planSummaryMain planSummaryList':{
                render:function(grid){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams, {planSummaryType:grid.planSummaryType});
                    store.load();
                }
            },
            'planSummaryList button': {
                click: function (btn) {
                	var optType = btn.optType;
                    if(optType=='add'){
                    	Ext.widget('planSummaryAdd',{listDom:btn.up('baseList'),viewId:btn.vw,optType:btn.optType}).show();
                    }else if(optType=='update'){
                    	var view = Ext.widget('planSummaryEdit',{listDom:btn.up('baseList'),viewId:btn.vw,optType:btn.optType}).show();
                    	var record = btn.up('baseList').getSelectionModel().getSelection();
                		if(view.down('form'))view.down('form').loadRecord(record[0]);
                	}else if(optType=='view'){
                    	var view = Ext.widget('planSummaryView',{listDom:btn.up('baseList'),viewId:btn.vw,optType:btn.optType}).show();
                    	var record = btn.up('baseList').getSelectionModel().getSelection();
                		if(view.down('form'))view.down('form').loadRecord(record[0]);
                    }
                }
            }
        });
    }
})
