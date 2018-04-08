Ext.define('user.view.product.comProductMain', {
    extend: 'public.BaseWin',
    alias: 'widget.comProductMain',
    layout : 'border',
 	width: 1000,
    height:600,
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'productProductKindList',
                    title : '产品分类',
                    region:'west',
                    layout : 'fit',
                    split: true,
                    collapsible: true,
                    width:200
                },{
                    xtype: 'productList',
                    title : '产品列表',
                    operateBtn : false,
                    region:'center',
                    layout : 'fit',
                    split: true
                }
            ],
            buttons: [{
	             text:'添加',iconCls:'table_save',itemId:'confirm',
	             handler:me.choiceProduct
         	},{
	             text:'关闭',iconCls:'cancel',
	             handler:function(btn){
	            	btn.up('window').close();
	        	 }
	         }]
        });
        me.callParent(arguments);
    },
    'choiceProduct':function(btn){
    	var records = btn.up('window').down('grid').getSelectionModel().getSelection();
		var store = btn.up('window').listDom.getStore();
		if(records.length > 0){
			var models = [];
			records.forEach(function(record, index, array){
				var dod = Ext.create(store.model, {price:record.get('salePrice')});
				dod.set('product.id',record.get('id'));
				dod.set('product.name',record.get('name'));
				models.push(dod);
			});
			//附加本条记录
			store.loadData(models,true);
			Ext.example.msg('提示', '已添加，请继续选择！！');
            btn.up('window').close();
		}else{
			Ext.example.msg('提示', '您没有选择任何产品！');
		}
    }
});