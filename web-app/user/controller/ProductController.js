/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.controller.ProductController',{
    extend : 'Ext.app.Controller',
    views:['product.List','product.Add','product.Edit','product.Main'
        ,'product.ProductProductKindList'] ,
    stores:['ProductStore','ProductKindStoreForEdit'],
    models:['ProductModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'productList',
            selector : 'productList'
        },
        {
            ref:'productMainProductKind',
            selector:'productMain productProductKindList'
        }
    ],
    init:function(){
        var productKindController = this.application.getController("ProductKindController");
        this.control({
            'productMain productList button[operationId=product_add]':{
                click:function(btn){
                    var productAdd = Ext.widget("productAdd");
                    var productKindRecord = this.getProductMainProductKind().getSelectionModel().getSelection()[0];
                    if(productKindRecord){
                        var productKind = productAdd.down('form baseComboBoxTree');
                        if(productKind != null && productKind != 'undefined'){
                            productKind.setValue(productKindRecord.get("id"));
                        }
                    }
                    productAdd.show();
                }
            },
            'productMain productList button[operationId=product_update]':{
                click:function(btn){
                    var grid = btn.up("productList");
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('productEdit').show();
                    view.down('form').loadRecord(record);
                    var productKind = view.down('form baseComboBoxTree');
                    productKind.setValue(record.get("productKind.id"));
                    productKind.setRawValue(record.get("productKind.name"));
                }
            },
            'productProductKindList':{
                itemclick : function(view, record, item, index, e, eOpts ){
                    var productStore = this.getProductList().getStore();
                    Ext.apply(productStore.proxy.extraParams,{productKind:record.get("id")});
                    productStore.load();
                }
            },
            'productMain productProductKindList > treeview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    var baseList = view.up('treepanel');
                    if(e.getKey()==e.ESC){
                        baseList.getSelectionModel().deselectAll();
                        var productStore = this.getProductList().getStore();
                        Ext.apply(productStore.proxy.extraParams,{productKind:null});
                        productStore.load();
                    }
                }
            }
        });
    }
})
