/**
 * Created by shqv on 2014-9-14.
 */
/**
 * Created by shqv on 2014-9-13.
 */
Ext.define('user.view.dict.item.List', {
    extend: 'public.BaseList',
    alias: 'widget.dictItemList',
    autoScroll: true,
    store:Ext.create('user.store.DataDictItemStore'),
    title: '字典明细',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'add',iconCls:'table_add'},
        {xtype:'button',text:'删除',itemId:'del',iconCls:'table_remove',disabled:true,autodisabled:true},
        {xtype:'button',text:'修改',itemId:'update',iconCls:'table_save',disabled:true,autodisabled:true},
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
