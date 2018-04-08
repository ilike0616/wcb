/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.field.List', {
    extend: 'public.BaseList',
    alias: 'widget.fieldList',
    autoScroll: true,
    store: Ext.create('admin.store.FieldStore'),
    title: '字段管理',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'查看',itemId:'viewButton',iconCls:'table_save'}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'字段名称',
            dataIndex:'fieldName'
        },
        {
            text:'数据类型',
            dataIndex:'dbType'
        },
        {
            text:'关系',
            dataIndex:'relation'
        },
        {
            text:'备注说明',
            dataIndex:'remark'
        }
    ]
});