/**
 * Created by shqv on 2014-9-13.
 */
Ext.define("user.controller.DataDictController",{
    extend:'Ext.app.Controller',
    views:['dict.List','dict.Main','dict.item.List','dict.item.Add','dict.item.Edit','dict.item.View'] ,
    stores:['DataDictStore','DataDictItemStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref : 'dictItemList',
            selector : 'dictItemList'
        },
        {
            ref:'dictItemAddForm',
            selector:'dictItemAdd form'
        },
        {
            ref:'dictItemAddWin',
            selector:'dictItemAdd'
        },
        {
            ref:'dictItemEditForm',
            selector:'dictItemEdit form'
        },
        {
            ref:'dictItemEditWin',
            selector:'dictItemEdit'
        },
        {
            ref: 'dictItemDeleteBtn',
            selector: 'dictItemList button#del'
        },
        {
            ref: 'dictItemUpdateBtn',
            selector: 'dictItemList button#update'
        }
    ],
    init:function(){
        this.control({
            'dictList':{
                select:function(selectModel, record, index, eOpts){
                    var store = eOpts.up('dictMain').down('dictItemList').getStore();
                    Ext.apply(store.proxy.extraParams, {dict:record.get('id')});
                    store.load();
                }
            },
            'dictItemList':{
                select:function(selectModel, record, index, eOpts){
                    if(index==0){
                        eOpts.up('baseList').down("button#up").setDisabled(true);
                    }
                    if(eOpts.up('baseList').getStore().getCount()-1==index){
                        eOpts.up('baseList').down("button#down").setDisabled(true);
                    }
                },edit:function(editor,ctx) {
                }
            },
            'dictItemList button#up':{
                click:function(btn){
                    var grid = btn.up('dictItemList');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index - 1, rec);
                    store.each(function(record,index){
                        record.set('seq',index+1);
                    });
                    grid.getSelectionModel().select(rec);

                    var mr = store.getModifiedRecords();
                    var sbtn = btn.up('dictItemList').down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'dictItemList button#down':{
                click:function(btn){
                    var grid = btn.up('dictItemList');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index + 1, rec);
                    store.each(function(record,index){
                        record.set('seq',index+1);
                    });
                    grid.getSelectionModel().select(rec);
                    var mr = store.getModifiedRecords();
                    var sbtn = btn.up('dictItemList').down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'dictItemList button#save':{
                click:function(btn){
                    this.GridDoActionUtil.doSave(btn.up('dictItemList'),btn);
                }
            },
            'dictItemList button#add': {
                click: function (btn) {
                    var addForm = Ext.widget("dictItemAdd");
                    var dictListSelection = btn.up('dictMain').down('dictList').getSelectionModel().getSelection()[0];
                    if(dictListSelection){
                        var value = dictListSelection.get("id");
                        this.getDictItemAddForm().down("hiddenfield[name=dict]").setValue(value);
                        this.getDictItemAddForm().down("hiddenfield[name=seq]").setValue(btn.up("dictItemList").getStore().getCount() + 1);
                    }else{
                        Ext.Msg.alert("提示","请先选择左边的种类");
                        return;
                    }
                    addForm.show();
                }
            },
            'dictItemAdd button#save': {
                click: function (btn) {
                    this.GridDoActionUtil.doInsert(this.getDictItemList(), this.getDictItemAddForm(), this.getDictItemAddWin());
                }
            },
            'dictItemList button#update': {
                click: function (btn) {
                    var record = btn.up('dictItemList').getSelectionModel().getSelection()[0];
                    var view = Ext.widget('dictItemEdit');
                    view.down('form').loadRecord(record);
                    view.show();
                }
            },
            'dictItemEdit button#save': {
                click: function (btn) {
                    this.GridDoActionUtil.doUpdate(this.getDictItemList(), this.getDictItemEditForm(), this.getDictItemEditWin());
                }
            },
            'dictItemList button#del': {
                click: function (btn) {
                    var grid = btn.up("dictItemList");
                    this.GridDoActionUtil.doDelete(grid, 'text',this.getDictItemDeleteBtn());
                }
            }
        });
    }
});