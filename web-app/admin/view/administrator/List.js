/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.administrator.List', {
    extend: 'public.BaseList',
    alias: 'widget.administratorList',
    autoScroll: true,
    store: Ext.create('admin.store.AdministratorStore'),
    title: '管理员管理',
    split:true,
    alertName: 'name',
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'administratorAdd'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'administratorEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,autoDelete:true},
        {xtype:'button',text:'密码初始化',itemId:'initPasswordButton',iconCls:'table_remove',autodisabled:true,disabled:true}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'管理员姓名',
            dataIndex:'name'
        },
        {
            text:'管理员账号',
            dataIndex:'adminId'
        },
        {
            text:'密码',
            dataIndex:'password'
        },
        {
            text:'邮箱',
            dataIndex:'email'
        },
        {
            text:'电话',
            dataIndex:'phone'
        },
        {
            text:'手机',
            dataIndex:'mobile'
        },
        {
            text:'传真',
            dataIndex:'fax'
        }
    ]
});