Ext.define("admin.view.user.SortUserPortalList", {
	extend : 'Ext.panel.Panel',
	alias : 'widget.sortUserPortalList',
	layout : 'fit',
    forceFit:true,
	autoScroll : true,
    listeners:{
        afterrender:function(){
            Ext.apply(this.down('grid').getStore().proxy.extraParams, {userId:this.userId});
            this.down('grid').getStore().load();
        }
    },
	items : [ {
		xtype : 'grid',
	    tbar:[
	        {xtype:'button',text:'上移',itemId:'up',iconCls:'Arrowup'},
	        {xtype:'button',text:'下移',itemId:'down',iconCls:'Arrowdown'},
	        {xtype:'button',text:'保存',itemId:'save',iconCls:'Disk'}
	    ],
        store : Ext.create('admin.store.UserPortalPureStore'),
		columns : [
		   {
			   xtype : 'rownumberer',
			   width : 40,
			   sortable : false
		   },{
                text:'标题',
                dataIndex:'title',
				width:150
            },{
                text: '高度',
                dataIndex: 'height'
            },{
                text:'显示顺序',
                dataIndex:'idx'
            }
		],
		 dockedItems:[]
	} ]
})