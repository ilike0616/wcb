/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('admin.view.operation.List', {
    extend: 'public.BaseList',
    alias: 'widget.operationList',
    autoScroll: true,
    store: Ext.create('admin.store.OperationStore'),
    title: '操作管理',
    split:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true},
        {xtype:'button',text:'查看',itemId:'viewButton',iconCls:'table_view',disabled:true,autodisabled:true},
        {xtype:'button',text:'增加点评',itemId:'addReviewButton',iconCls:'table_add'}
    ],
    columns:[{
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'操作ID',
            width:280,
            dataIndex:'operationId'
        },{
        	text:'类型',
            width:100,
        	dataIndex:'type'
        },{
            text:'操作名称',
            width:100,
            dataIndex:'text'
        },{
            text: '图标',
            width:150,
            dataIndex: 'iconCls',
            renderer:function(val){
            	if(val){
            		return '<span class="'+val+'" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>:'+val;
            	}else{
            		return "";
            	}
            }
        },{
            text: '视图',
            width:200,
            dataIndex: 'vw'
        }
    ]
});