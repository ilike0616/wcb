/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.model.List', {
    extend: 'public.BaseList',
    alias: 'widget.modelList',
    store: Ext.create('admin.store.ModelStore'),
    title: '用户管理',
    split:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'modelAdd'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true,autoShowEdit:true,target:'modelEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'备注说明',
            dataIndex:'remark'
        },
        {
            text:'class',
            width: 250,
            dataIndex:'modelClass'
        }
    ]
});