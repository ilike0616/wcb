Ext.define("admin.view.view.detail.PropertyList", {
	extend : 'public.BaseList',
	alias : 'widget.viewDetailPropertyList',
	layout : 'fit',
	autoScroll : true,
	enableBasePaging : false,
	alertName:'paramName',
	tbar:[
		{text:'新增',itemId:'addLine',iconCls:'table_add'},
		{text:'修改',itemId:'editLine',iconCls:'table_save',disabled:true,autodisabled:true,optRecords: 'one'},
		{text:'删除',itemId:'removeLine',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,optRecords: 'many'}
	],
	store : Ext.create('admin.store.ViewDetailExtendStore'),
	columns : [{
		text:'名称',
		dataIndex:'paramName'
	},{
		text:'值',
		dataIndex:'paramValue'
	},{
		text:'editor?',
		dataIndex:'isBelongToEditor'
	}
	]
})