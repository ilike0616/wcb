/**
 * Created by like on 2015-04-24.
 */
Ext.define('admin.view.appVersion.List', {
    extend: 'public.BaseList',
    alias: 'widget.appVersionList',
    autoScroll: true,
    store: Ext.create('admin.store.AppVersionStore'),
    title: 'App版本管理',
    split:true,
    alertName: 'appVersion',
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'appVersionAdd'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'appVersionEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,autoDelete:true}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'应用平台',
            dataIndex:'platform',
            xtype:'rowselecter',
            arry:[
                ['Android','Android'],
                ['IOS','IOS']
            ]
        },
        {
            text:'版本分类',
            dataIndex:'edition',
            xtype:'rowselecter',
            arry:[
                ['1','销售通'],
                ['2','门店通'],
                ['3','服务通'],
                ['99', 'CRM'],
                ['100', 'CRM100'],
                ['101', 'CRM101'],
                ['102', 'CRM102'],
                ['103', 'CRM103'],
                ['104', 'CRM104'],
                ['105', 'CRM105'],
                ['106', 'CRM106'],
                ['107', 'CRM107'],
                ['108', 'CRM108'],
                ['109', 'CRM109']
            ]
        },
        {
            text:'版本号',
            dataIndex:'appVersion'
        },
        {
            text:'升级说明',
            dataIndex:'remark'
        },
        {
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});