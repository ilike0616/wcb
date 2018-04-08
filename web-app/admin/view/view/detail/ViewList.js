Ext.define("admin.view.view.detail.ViewList", {
	extend: 'Ext.panel.Panel',
	alias: 'widget.viewDetailViewList',
	autoScroll: true,
	layout : 'fit',
	width :350,
	title : '模块视图',
    listeners:{
        afterrender:function(){
			var grid = this.down('grid');
			grid.down('combo').setValue(this.paramsObj.userId);
        }
    },
	items : [ {
		xtype : 'grid',
		forceFit:true,
		tbar:[
			{
				xtype: 'combo',
				fieldLabel: '所属用户',
				labelAlign: 'right',
				name: 'user',
				emptyText: '请选择...',
				autoSelect : true,
				forceSelection:true,
				displayField : 'name',
				valueField : 'id',
				queryMode: 'local',
				store:Ext.create('admin.store.UserStore',{pageSize:9999999})
			}
		],
        store : Ext.create('admin.store.ViewStore'),
		columns : [
			{
				text:'视图Id',
				dataIndex:'viewId'
			},
			{
				text:'备注',
				dataIndex:'clientType',
				width:'80',
				renderer:function(value,metaData,record){
					var clientTypeText = "pc端";
					if(value == 'mobile'){
						clientTypeText = "手机端";
					}
					var viewTypeText = "表单";
					if(record.get('viewType') == 'list'){
						viewTypeText = "列表";
					}
					return clientTypeText + "【" + viewTypeText + "】";
				}
			}
		]
	} ]
})