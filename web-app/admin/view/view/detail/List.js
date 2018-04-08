Ext.define("admin.view.view.detail.List", {
	extend : 'Ext.panel.Panel',
	alias : 'widget.viewDetailList',
	modal : true,
	layout : 'fit',
	height:400,
	autoScroll : true,
	title : '自定义明细',
    listeners:{
        afterrender:function(){
			var view = this.paramsObj.view?this.paramsObj.view:0;
			Ext.apply(this.down('grid').getStore().proxy.extraParams, {view:view});
			this.down('grid').getStore().load();
        }
    },
	items : [ {
		xtype : 'baseList',
		forceFit:true,
		viewConfig:{
			getRowClass: function(record){
				if(record.get("inputType") == 'start'){
					return "boldFont";
				}else if(record.get('inputType') == 'end'){
					return "splitterEnd";
				}else{
					return "";
				}
			}
		},
		tbar:{
			xtype: 'container',
			layout: 'anchor',
			defaults: {anchor: '0',border:0},
			defaultType: 'toolbar',
			items:[{
				items:[
					{xtype:'button',text:'上移',itemId:'up',iconCls:'Arrowup',disabled:true,autodisabled:true},
					{xtype:'button',text:'下移',itemId:'down',iconCls:'Arrowdown',disabled:true,autodisabled:true},
					{xtype:'button',text:'保存',itemId:'save',iconCls:'Disk'},
					{xtype:'button',text:'新增',itemId:'addButton',iconCls:'table_add'},
					{xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true},
					{xtype:'button',text:'预览',itemId:'previewButton',iconCls:'table_view'}
				]
			},{
				items:[
					{xtype:'button',text:'分隔符',itemId:'addSplitter',iconCls:'table_add'}
				]
			}]
		},
        store : Ext.create('admin.store.ViewDetailStore'),
		columns : [
		   {
			   xtype : 'rownumberer',
			   width : 20,
			   sortable : false
		   },{
	            text:'字段标题',
	            dataIndex:'userField.text',
				renderer:function(value,metaData,record){
					if(!value) {
						return record.get('label');
					}
					return value;
				}
		   },{
	            text:'pageType',
	            dataIndex:'pageType'
		   },{
	            text:'字段名',
	            dataIndex:'userField.fieldName',
				renderer:function(value,metaData,record){
					if(!value) {
						return record.get('inputType');
					}
					return value;
				}
		   },{
	            text:'默认值',
	            dataIndex:'defValue'
		   }
		],
		 dockedItems:[]
	} ]
})