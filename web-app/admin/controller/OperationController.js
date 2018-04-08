/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define("admin.controller.OperationController",{
    extend:'Ext.app.Controller',
    views:['operation.List','operation.Add','operation.Edit','operation.View'] ,
    stores:['OperationStore'],
    models:['OperationModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'operationDeleteBtn',
            selector: 'operationList button#deleteButton'
        }
    ],
    init:function(){
        this.control({
            'operationList button[itemId=addButton]':{
                click:function(btn){
                    var record = btn.up("moduleMain").down("moduleList").getSelectionModel().getSelection()[0];
                    if(record == null){
                        alert("请选择模块！");
                        return;
                    }
                    var view = Ext.widget("operationAdd");
                    var clientType = btn.up('operationList').clientType;
                    view.down('button#save').target = 'moduleMain operationList[clientType='+clientType+']';
                    view.down("hidden[name=module]").setValue(record.get('id'))
                    view.down("combo[name=clientType]").setValue(clientType);
                    view.show();
                }
            },
            'operationList button#updateButton':{
                click:function(btn){
                    var operationList = btn.up('operationList');
                    var record = operationList.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('operationEdit');
                    var clientType = operationList.clientType;
                    view.down('button#save').target = 'moduleMain operationList[clientType='+clientType+']';
                    view.down('form').loadRecord(record);
                    view.show();
                }
            },
            'operationList button#viewButton':{
            	click:function(btn){
                    var operationList = btn.up('operationList');
                    var record = operationList.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('operationView');
                    view.down('form').loadRecord(record);
                    view.show();
                }
            },
            'operationList button#deleteButton':{
                click:function(btn){
                    var grid = btn.up("operationList");
                    this.GridDoActionUtil.doDelete(grid,'text',this.getOperationDeleteBtn());
                }
            },
            'operationList button#addReviewButton':{
                click:function(btn){
                    var module = btn.up("moduleMain").down("moduleList").getSelectionModel().getSelection()[0];
                    if(module == null){
                        alert("请选择模块！");
                        return;
                    }
                    var grid = btn.up("operationList"),
                        store = grid.getStore(),
                        operationId = module.get('moduleId')+'_review';
                    if(store.find('operationId',operationId) != -1){
                        Ext.example.msg('提示', '该模块已有点评操作！');
                        return;
                    }
                    this.GridDoActionUtil.doAjax('operation/insert',{module:module.get('id'),operationId: operationId,type:'add',showWin:true,clientType:'all',optRecords:'no',text:'点评',iconCls:'table_add',vw:'reviewAdd',auto:true,autodisabled:true},store,false);
                }
            }
        });
    }
});