/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.userField.List', {
    extend: 'public.BaseList',
    alias: 'widget.userFieldList',
    autoScroll: true,
    enableComplexQuery:false,
    store:Ext.create('admin.store.UserFieldStore'),
//    title: '字段管理',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'启用固定字段',itemId:'enableFixedField',iconCls:'table_add'},
        {xtype:'button',text:'启用关联字段',itemId:'enableRelatedField',iconCls:'table_add'},
        {xtype:'button',text:'启用扩展字段',itemId:'enableExtField',iconCls:'table_add'},
        {xtype:'button',text:'模糊查询字段',itemId:'setMohuSearchField',iconCls:'table_add'},
        {xtype:'button',text:'合计字段',itemId:'setHejiField',iconCls:'table_add'},
        {xtype:'button',text:'组合约束',itemId:'associatedRequiredButton',iconCls:'table_add'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'fieldName'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'所属公司',
            sortName:'user.id',
            dataIndex:'user.name'
        },
        {
            text:'所属模块',
            sortName:'module.id',
            dataIndex:'module.moduleName'
        },
        {
            text:'字段名称',
            dataIndex:'fieldName'
        },
        {
            text:'字段标题',
            dataIndex:'text'
        },
        {
            text:'数据类型',
            sortName:'dbType',
            dataIndex :'dbTypeName'
        },
        {
            text:'关系',
            dataIndex:'relation'
        },
        {
            text:'数据字典',
            sortName : 'dict.id',
            dataIndex:'dict.text'
        }
    ]
});