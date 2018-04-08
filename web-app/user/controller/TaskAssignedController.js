/**
 * Created by like on 2015-01-22.
 */
Ext.define('user.controller.TaskAssignedController', {
	extend : 'Ext.app.Controller',
	views : [ 'taskAssigned.Main', 'taskAssigned.List', 'taskAssigned.Edit', 'taskAssigned.View', 'taskAssigned.Reply',
			'taskAssigned.Report', 'taskAssigned.Comment' ],
	stores : [ 'TaskAssignedStore' ],
	models : ['CommentModel'],
	GridDoActionUtil : Ext.create("admin.util.GridDoActionUtil"),
	refs : [],
	init : function() {
		this.control({
			'taskAssignedMain taskAssignedList' : {
				render : function(grid) {
					var store = grid.getStore();
					Ext.apply(store.proxy.extraParams, {
						taskType : grid.taskType
					});
					store.load();
				}
			},
			'taskAssignedMain button[operationId=task_assigned_reply]' : {
				click : function(btn) {
					var view = Ext.widget("taskAssignedReply", {
						listDom : btn.up('baseList')
					}).show();
					var record = btn.up('baseList').getSelectionModel().getSelection()[0];
					view.down('form').down("field[name=id]").setValue(record.get('id'));
				}
			},
			'taskAssignedReply button[itemId=reply]' : {
				click : function(btn) {
					this.GridDoActionUtil.doInsert(btn.up('window').listDom, btn.up('form'), btn.up('window'),null, 'comment');
				}
			},
			'taskAssignedMain button[operationId=task_assigned_report]' : {
				click : function(btn) {
					var view = Ext.widget("taskAssignedReport", {
						listDom : btn.up('baseList')
					}).show();
					var record = btn.up('baseList').getSelectionModel().getSelection()[0];
					view.down('form').down("field[name=id]").setValue(record.get('id'));
				}
			},
			'taskAssignedReport button[itemId=report]' : {
				click : function(btn) {
					this.GridDoActionUtil.doInsert(btn.up('window').listDom, btn.up('form'), btn.up('window'), null, 'comment');
				}
			},
			'taskAssignedMain button[operationId=task_assigned_view]' : {
				click : function(btn) {
					var vm = btn.vw, view, showWin = btn.showWin;
					if (showWin) {
						view = Ext.widget(vm, {
							listDom : btn.up('baseList'),
							viewId : vm,
							optType : btn.optType
						}).show();
					}
					var record = btn.up('baseList').getSelectionModel().getSelection()[0];
					if (view.down('form'))
						view.down('form').loadRecord(record);
					var executorName = [];
					var ccName = [];
					Ext.each(record.get('executor'),function(item){
						executorName.push(item.name);
					})
					if(executorName.length > 0){
						executorName.join(",");
					}
					Ext.each(record.get('cc'),function(item){
						ccName.push(item.name);
					})
					if(ccName.length > 0){
						ccName.join(",");
					}
					view.down('textareafield[name=executor.name]').setValue(executorName);
					view.down('textareafield[name=cc.name]').setValue(ccName);
					// 加载回复列表
					var store = view.down('grid').getStore();
        			var id = "0";
        			if(view.down('form').down("field[name=id]")){
        				id = view.down('form').down("field[name=id]").value;
        			}
        			Ext.apply(store.proxy.extraParams,{id:id});
        			store.load();
				}
			},
			'taskAssignedMain button[operationId=task_assigned_update]' : {
				click : function(btn) {
					var vm = btn.vw, view, showWin = btn.showWin;
					if (showWin) {
						view = Ext.widget(vm, {
							listDom : btn.up('baseList'),
							viewId : vm,
							optType : btn.optType
						}).show();
					}
					var record = btn.up('baseList').getSelectionModel().getSelection()[0];
					if (view.down('form'))
						view.down('form').loadRecord(record);
					var executorId=[],executorName = [];
					var ccId=[],ccName = [];
					Ext.each(record.get('executor'),function(item){
						executorId.push(item.id);
						executorName.push(item.name);
					})
					if(executorName.length > 0){
						executorId.join(",");
						executorName.join(",");
					}
					Ext.each(record.get('cc'),function(item){
						ccId.push(item.id);
						ccName.push(item.name);
					})
					if(ccName.length > 0){
						ccId.join(",");
						ccName.join(",");
					}
					view.down('hiddenfield[name=executor]').setValue(executorId);
					view.down('textareafield[name=executor.name]').setValue(executorName);
					view.down('hiddenfield[name=cc]').setValue(ccId);
					view.down('textareafield[name=cc.name]').setValue(ccName);
				}
			}
		});
	}
})
