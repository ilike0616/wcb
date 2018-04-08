Ext.define('admin.view.moduleAssignment.condition.List', {
    extend: 'public.BaseList',
    alias: 'widget.conditionList',
    store: Ext.create('admin.store.UserOptConditionStore'),
    autoScroll: true,
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autoShowEdit:true,target:'conditionEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autoDelete:true}
    ],
    columns:[{
            text:'名称',
            dataIndex:'name'
        },{
            text:'说明',
            dataIndex:'remark'
        }
    ]
});