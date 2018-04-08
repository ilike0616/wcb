Ext.define('user.controller.GoOutApplyController',{
    extend : 'Ext.app.Controller',
    views:['goOutApply.List'] ,
    stores:['GoOutApplyStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
    	this.application.getController("AuditController");
        this.control({
            'goOutApplyList button[operationId=goout_apply_opinion]':{
            	click:function(btn){
            		Ext.widget('auditOpinionWinList',{listDom:btn.up('baseList'),formType:1}).show();
            	}
            },
            'goOutApplyList button[operationId=goout_apply_audit_again]':{
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
            'goOutApplyList button#updateButton': {
                click: function (btn) {
                    var record = btn.up('goOutApplyList').getSelectionModel().getSelection()[0];
                    if(record.data.audit.auditState == 1){
	                    var view = Ext.widget('goOutApplyEdit').show();
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