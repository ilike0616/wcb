/**
 * Created by guozhen on 2015/04/23
 */
Ext.define('admin.view.portal.List', {
    extend: 'public.BaseList',
    alias: 'widget.portalList',
    autoScroll: true,
    store: Ext.create('admin.store.PortalStore'),
    title: 'Portal管理',
    split:true,
    alertName: 'title',
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'portalEdit',optRecords: 'one'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,optRecords: 'many'}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'标题',
            dataIndex:'title'
        },
        {
            text:'类型',
            dataIndex:'type',
            renderer:function(value){
                if(value == 1) return '动态'
                else if(value == 2) return '图表'
            }
        },
        {
            text:'xtype',
            dataIndex:'xtype'
        },
        {
            text:'高度',
            dataIndex:'height'
        },{
            text:'穿透查询viewId',
            dataIndex:'viewId'
        },{
            text:'穿透查询viewStore',
            dataIndex:'viewStore'
        },{
            text:'创建时间',
            dataIndex:'dateCreated'
        }
    ]
});