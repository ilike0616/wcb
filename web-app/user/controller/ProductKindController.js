/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define('user.controller.ProductKindController',{
    extend : 'Ext.app.Controller',
    views:['productKind.List','productKind.Add'] ,
    stores:['ProductKindStore','ProductKindStoreForEdit'],
    models:['ProductKindModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'productKindList',
            selector : 'productKindList'
        },
        {
            ref:'productKindAddWin',
            selector:'productKindAdd'
        },
        {
            ref:'productKindEditWin',
            selector:'productKindEdit'
        }
    ],
    init:function() {
        this.control({
            /*'productKindList': {
                itemcontextmenu : function( view, record, item, index, e, eOpts ){
                    var controllerThis = this;
                    var contextMenu = Ext.create('Ext.menu.Menu',{
                        items:[{
                            text:"新增",
                            iconCls:'table_add',
                            handler:function(){
                                controllerThis.onContextMenuInsert(this,controllerThis,record);
                            }
                        },{
                            text:"修改",
                            iconCls:'table_save',
                            handler:function(){
                                controllerThis.onContextMenuUpdate(this,controllerThis,record);
                            }
                        },{
                            text:"删除",
                            iconCls:'table_remove',
                            handler:function(){
                                this.up("menu").destroy();
                                controllerThis.GridDoActionUtil.doDeleteById(controllerThis.getProductKindList(),record.get("id"),record.get("name"));
                            }
                        }]
                    })
                    e.preventDefault();
                    contextMenu.showAt(e.getXY());
                }
            },*/
            'productKindList > treeview': {
                drop: function (node, data, overModel, dropPosition, eOpts) {
                    var me = this;
                    var store = data.view.store.treeStore;
                    var url = store.proxy.api['save'];
                    var records = data.records[0];
                    var data ={};
                    data["id"] = records.get("id");
                    if(dropPosition == 'append'){
                        data["parentKind"] = overModel.get("id");
                    }else{
                        var parentKindId = overModel.get("parentKind.id");
                        data["parentKind"] = parentKindId;
                    }
                    Ext.Ajax.request({
                        url:url,
                        params:{data:Ext.JSON.encode(data)},
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            me.GridDoActionUtil.loadStore(Ext.getCmp('userViewProductKindBaseTreeGrid'));
                            if(d.success){
                                Ext.example.msg('提示', '保存成功');
                            }else{
                                console.info(d.errors);
                                Ext.example.msg('提示', '保存失败！');
                            }
                        },
                        failure:function(response,opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                },
                nodedragover : function(targetNode, position, dragData, e, eOpts ){
                    if (targetNode.get('leaf')){
                        targetNode.set('leaf',false);
                    }
                    return true;
                }
            },
            'productKindList button[operationId=product_kind_add]': {
                click: function (btn) {
                    var view = Ext.widget('productKindAdd');
                    view.show();
                    var productKindRecord = btn.up('productKindList').getSelectionModel().getSelection()[0];
                    var productKind = view.down('baseComboBoxTree[name=parentKind]');
                    if(productKindRecord && Ext.typeOf(productKind) != 'undefined'){
                        view.down('baseComboBoxTree[name=parentKind]').setValue(productKindRecord.get("id"));
                        view.down('baseComboBoxTree[name=parentKind]').setRawValue(productKindRecord.get("name"));
                    }
                }
            }
        });
    }
})
