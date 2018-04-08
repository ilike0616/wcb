Ext.define('user.view.locus.DetailList', {
	extend : 'public.BaseList',
	alias : 'widget.locusDetailList',
	autoScroll : true,
	store : Ext.create('user.store.LocusDetailStore'),
	title : '轨迹明细',
	title1 : '轨迹明细',
	split : true,
	forceFit : true,
	renderLoad : true,
	extraBtn : [{
		"xtype": "button",
		"text": "定位",
		"operationId": "locus_detail_location",
		"auto": true,
		"optType": "view",
		"optRecords": null,
		"showWin": true,
		"vw": "locusMap",
		"iconCls": "table_view",
		"disabled": true,
		"autodisabled": true
	}],
	columns : [ {
		xtype : "rownumberer",
		width : 40,
		sortable : false
	}, {
		text : "员工姓名",
		dataIndex : "employee.name",
		orderIndex : 2
	}, {
		text : "位置",
		dataIndex : "location",
		orderIndex : 3
	}, {
		text : "定位时间时间",
		dataIndex : "locusDate",
		orderIndex : 4,
		xtype : "datecolumn",
		format : "Y-m-d H:i:s"
	}, {
		text : "MAC地址",
		dataIndex : "mac",
		orderIndex : 5
	}, {
		text : "手机型号",
		dataIndex : "model",
		orderIndex : 6
	}, {
		text : "创建时间",
		dataIndex : "dateCreated",
		orderIndex : 7,
		xtype : "datecolumn",
		format : "Y-m-d H:i:s"
	} ]
});