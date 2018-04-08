Ext.define('admin.view.moduleAssignment.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.moduleAssignmentList',
    autoScroll: true,
    store: Ext.create('admin.store.ModuleStoreForEditCheck'),
    title: '系统模块',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'保存',itemId:'saveButton',iconCls:'table_save'}
    ],
    columns:[{
            text:'模块名称',
            dataIndex:'text',
            xtype: 'treecolumn'
        },{
            text:'模块Id',
            dataIndex:'moduleId'
        }
    ]
});