Ext.define('admin.view.module.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.moduleList',
    autoScroll: true,
    store: Ext.create('admin.store.ModuleStore'),
    title: '系统模块',
    split:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true}
    ],
    columns:[{
            text:'模块名称',
            dataIndex:'moduleName',
            width:170,
            xtype: 'treecolumn'
        },{
            text:'模块Id',
            width:200,
            dataIndex:'moduleId'
        }
    ]
});