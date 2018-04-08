/**
 * Created by like on 2015-04-29.
 */
Ext.define('admin.view.edition.List', {
    extend: 'public.BaseList',
    alias: 'widget.editionList',
    autoScroll: true,
    store: Ext.create('admin.store.EditionStore'),
    title: '版本管理',
    split:true,
    alertName: 'name',
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'editionAdd'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'editionEdit'}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'版本名称',
            dataIndex:'name'
        },
        {
            text:'种类',
            dataIndex:'kind',
            xtype:'rowselecter',
            arry:[
                ['1','销售通'],
                ['2','门店通'],
                ['3','服务通']
            ]
        },
        {
            text:'版本',
            dataIndex:'ver',
            xtype:'rowselecter',
            arry:[
                ['1','免费版'],
                ['2','标准版'],
                ['3','高级版']
            ]
        },
        {
            text:'费用/人▪月',
            dataIndex:'monthlyFee'
        },
        {
            text:'企业模板',
            dataIndex:'templateUserName'
        },
        {
            text:'版本说明',
            dataIndex:'remark'
        }
    ]
});