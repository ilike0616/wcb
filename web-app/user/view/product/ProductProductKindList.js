/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('user.view.product.ProductProductKindList', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.productProductKindList',
    autoScroll: true,
    store:Ext.create('user.store.ProductKindStore'),
    title: '产品分类',
    forceFit:true,
    reserveScrollbar: true,
    rootVisible : false,
    tools:[{
        type:'refresh',
        tooltip: '刷新',
        handler: function(event, toolEl, panelHeader) {
            var refresh = this;
            refresh.setDisabled(true);
            var treePanel = this.up('treepanel');
            Ext.create("admin.util.GridDoActionUtil").loadStore(treePanel);
            Ext.defer(function(){
                refresh.setDisabled(false);
            },10000);
        }
    },{
        type:'close',
        tooltip: '全部',
        handler: function(event, toolEl, panelHeader) {
            var main = this.up('productMain');
            var productKindList = main.down('productProductKindList');
            var productList = main.down('productList');
            if(Ext.typeOf(productKindList.getSelectionModel()) != 'undefined'){
                productKindList.getSelectionModel().deselectAll();
            }
            var productStore = productList.getStore();
            Ext.apply(productStore.proxy.extraParams,{productKind:null});
            productStore.load();
        }
    }],
    columns: [{
        xtype: 'treecolumn',
        text: '分类名称',
        dataIndex: 'name'
    }]
});