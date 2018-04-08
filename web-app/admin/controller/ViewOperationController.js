/**
 * Created by shqv on 2014-8-27.
 */
Ext.define('admin.controller.ViewOperationController',{
    extend : 'Ext.app.Controller',
    views:['viewOperation.List','viewOperation.Add'] ,
    stores:['ViewOperationStore'],
    models:['ViewOperationModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'viewOperationList',
            selector : 'viewOperationList'
        },
        {
            ref: 'viewOperationDeleteBtn',
            selector: 'viewOperationList button#deleteButton'
        }

    ],
    init:function() {
        this.control({
            'viewOperationList grid':{
                select:function(selectModel, record, index, eOpts){
                    if(index==0){
                        eOpts.up('viewOperationList').down("button#up").setDisabled(true);
                    }
                    if(eOpts.up('grid').getStore().getCount()-1==index){
                        eOpts.up('viewOperationList').down("button#down").setDisabled(true);
                    }
                }
            },
            'viewOperationList button#up':{
                click:function(btn){
                    var grid = btn.up('viewOperationList grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index - 1, rec);
                    store.each(function(record,index){
                        record.set('orderIndex',index+1);
                    });
                    grid.getSelectionModel().select(rec);

                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'viewOperationList button#down':{
                click:function(btn){
                    var grid = btn.up('viewOperationList grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index + 1, rec);
                    store.each(function(record,index){
                        record.set('orderIndex',index+1);
                    });
                    grid.getSelectionModel().select(rec);
                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'viewOperationList button#save': {
                click:function(btn){
                    this.GridDoActionUtil.doSave(btn.up('grid'),btn);
                }
            },
            'viewOperationList button#addButton':{
                click:function(btn){
                    var grid = btn.up('viewOperationList');
                    var win = Ext.widget('viewOperationAdd',{
                        grid:grid
                    });
                    win.show();
                }
            },
            'viewOperationList button#deleteButton':{
                click:function(btn){
                    var win = btn.up("viewOperationList");
                    var grid = win.down('grid');
                    this.GridDoActionUtil.doDelete(grid, 'userOperation.text',this.getViewOperationDeleteBtn());
                }
            }
        })
    }
});
