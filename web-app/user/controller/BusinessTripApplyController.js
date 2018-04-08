Ext.define('user.controller.BusinessTripApplyController',{
    extend : 'Ext.app.Controller',
    views:['businessTripApply.List'] ,
    stores:['BusinessTripApplyStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
    	this.application.getController("AuditController");
        this.control({
        	
            'businessTripApplyList button[operationId=business_trip_apply_opinion]':{
            	click:function(btn){
            		var v = Ext.widget('auditOpinionWinList',{listDom:btn.up('baseList'),formType:1});
            		v.show();
            	}
            },
            'businessTripApplyList button[operationId=business_trip_apply_audit_again]':{
                click:function(btn){
                    var grid = btn.up('baseList');
                    var store = grid.getStore();
                    var record = grid.getSelectionModel().getSelection()[0];
                    Ext.Ajax.request({
                        url: store.getProxy().api['reAudit'],
                        params: {id: record.get('id')},
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            if (d.success) {
                                Ext.example.msg('提示', '操作成功，已重新送审！');
                                store.load();
                            } else {
                                console.info(d.errors);
                                Ext.example.msg('提示', '操作失败！');
                            }
                        }, failure: function (response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                        }
                    });
                }
            }
        	/**
            'businessTripApplyList button#updateButton': {
                click: function (btn) {
                    var record = btn.up('businessTripApplyList').getSelectionModel().getSelection()[0];
                    if(record.data.audit.auditState == 1){
                    	var view = Ext.widget('businessTripApplyEdit').show();
                    	view.down('form').loadRecord(record);
                    }else{
                    	Ext.example.msg('提示', '审核中或审核完成，不能修改！');
                    }
                }
            }
            */
        });
    }
})