/**
 * Created by like on 2015/8/18.
 */
Ext.define('user.controller.MyTaskController', {
    extend : 'Ext.app.Controller',
    views : [ 'myTask.Main', 'myTask.List',  'myTask.View', 'myTask.Reply',
        'myTask.Report', 'myTask.Comment' ],
    stores : [ 'MyTaskStore' ],
    models : ['CommentModel'],
    GridDoActionUtil : Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init : function() {
        this.control({
            'myTaskMain myTaskList' : {
                render : function(grid) {
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams, {
                        taskType : grid.taskType
                    });
                    store.load();
                }
            },
            'myTaskMain button[operationId=my_task_reply]' : {
                click : function(btn) {
                    var view = Ext.widget("myTaskReply", {
                        listDom : btn.up('baseList')
                    }).show();
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    view.down('form').down("field[name=id]").setValue(record.get('id'));
                }
            },
            'myTaskReply button[itemId=reply]' : {
                click : function(btn) {
                    this.GridDoActionUtil.doInsert(btn.up('window').listDom, btn.up('form'), btn.up('window'),null, 'comment');
                }
            },
            'myTaskMain button[operationId=my_task_report]' : {
                click : function(btn) {
                    var view = Ext.widget("myTaskReport", {
                        listDom : btn.up('baseList')
                    }).show();
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    view.down('form').down("field[name=id]").setValue(record.get('id'));
                }
            },
            'myTaskReport button[itemId=report]' : {
                click : function(btn) {
                    this.GridDoActionUtil.doInsert(btn.up('window').listDom, btn.up('form'), btn.up('window'), null, 'comment');
                }
            },
            'myTaskMain button[operationId=my_task_view]' : {
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
            }
        });
    }
});