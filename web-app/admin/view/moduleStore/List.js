Ext.define('admin.view.moduleStore.List', {
    extend: 'public.BaseList',
    alias: 'widget.moduleStoreList',
    autoScroll: true,
    store: Ext.create('admin.store.ModuleStoreStore'),
    title: '系统模块',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'moduleStoreEdit',optRecords: 'one'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,optRecords: 'many'}
    ],
    columns:[{
            text:'标题',
            dataIndex:'name',
        },{
            text:'store名称',
            dataIndex:'store'
        },{
            text:'创建时间',
            xtype:'datecolumn',
            dataIndex:'dateCreated',
            format:'Y-m-d H:i:s'
        },{
            text:'修改时间',
            xtype:'datecolumn',
            dataIndex:'lastUpdated',
            format:'Y-m-d H:i:s'
        }
    ]
});