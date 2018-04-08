/**
 * Created by shqv on 2014-9-14.
 */
/**
 * Created by shqv on 2014-9-13.
 */
Ext.define('admin.view.dict.item.List', {
    extend: 'public.BaseList',
    alias: 'widget.dictItemList',
    autoScroll: true,
    store:Ext.create('admin.store.DataDictItemStore'),
    title: '字典明细',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true,autoShowEdit:true,target:'dictItemEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'text'},
        {xtype:'button',text:'上移',itemId:'up',iconCls:'Arrowup',disabled:true,autodisabled:true},
        {xtype:'button',text:'下移',itemId:'down',iconCls:'Arrowdown',disabled:true,autodisabled:true},
        {xtype:'button',text:'保存',itemId:'save',iconCls:'Disk'}
    ],
    dockedItems: [],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'id',
            columnWidth : 50,
            dataIndex:'itemId'
        },{
            text:'名称',
            dataIndex:'text'
        },{
            text:'显示顺序',
            dataIndex:'seq'
        }
    ]
});
