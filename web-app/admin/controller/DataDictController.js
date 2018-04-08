/**
 * Created by shqv on 2014-9-13.
 */
Ext.define("admin.controller.DataDictController",{
    extend:'Ext.app.Controller',
    views:['dict.List','dict.Main','dict.Add','dict.Edit','dict.item.List','dict.item.Add','dict.item.Edit'],
    stores:['DataDictStore','DataDictItemStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
    ],
    init:function(){
        this.control({
            'dictList combo[name=user]':{
                change:function( field, newValue, oldValue, eOpts ){
                    var store = field.up('dictList').getStore();
                    Ext.apply(store.proxy.extraParams, {user:newValue});
                    store.load();
                }
            },
            'dictList':{
                select:function(selectModel, record, index, eOpts){
                    var itemList = eOpts.up('dictMain').down('dictItemList');
                    if(record.get('issys') == true){
                        itemList.down('button#addButton').setDisabled(true);
                        itemList.down('button#updateButton').setDisabled(true);
                        itemList.down('button#deleteButton').setDisabled(true);
                    }else{
                        itemList.down('button#addButton').setDisabled(false);
                    }
                    var store = itemList.getStore();
                    Ext.apply(store.proxy.extraParams, {dict:record.get('id'),user:record.get('user')});
                    store.load();
                }
            },
            'dictList button#addButton':{
                click:function(btn){
                    var dictGrid = btn.up('dictMain').down('dictList');
                    var userValue = dictGrid.down('combo[name=user]').getValue();
                    var view = Ext.widget("dictAdd");
                    view.down('combo[name=user]').setValue(userValue);
                    view.show();
                }
            },
            'dictItemList button#addButton':{
                click:function(btn){
                    var dictRecord = btn.up('dictMain').down('dictList').getSelectionModel().getSelection()[0];
                    var userValue = dictRecord.get('user');
                    var dictValue = dictRecord.get('id');
                    if(userValue == null || userValue == ""){
                        alert("请选择数据字典记录！");
                        return;
                    }
                    var view = Ext.widget("dictItemAdd");
                    view.down('hiddenfield[name=user]').setValue(userValue);
                    view.down('hiddenfield[name=dict]').setValue(dictValue);
                    view.show();
                }
            },
            'dictItemList':{
                select:function(selectModel, record, index, eOpts){
                    var dictRecord = eOpts.up('dictMain').down('dictList').getSelectionModel().getSelection()[0];
                    var itemList = eOpts.up('dictMain').down('dictItemList');
                    if(dictRecord.get('issys') == true){
                        itemList.down('button#addButton').setDisabled(true);
                        itemList.down('button#updateButton').setDisabled(true);
                        itemList.down('button#deleteButton').setDisabled(true);
                    }else{
                        itemList.down('button#addButton').setDisabled(false);
                    }
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
            }
        });
    }
});