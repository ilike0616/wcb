/**
 * Created by guozhen on 2015/04/23
 */
Ext.define('admin.view.subAgent.List', {
    extend: 'public.BaseList',
    alias: 'widget.subAgentList',
    autoScroll: true,
    store: Ext.create('admin.store.SubAgentStore'),
    title: '代理商管理',
    split:true,
    alertName: 'name',
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'subAgentAdd'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'subAgentEdit',optRecords: 'one'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,autoDelete:true,optRecords: 'many'},
        {xtype:'button',text:'充值',itemId:'addBalanceButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
        {xtype:'button',text:'退费',itemId:'reduceBalanceButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
        {xtype:'button',text:'密码初始化',itemId:'initPasswordButton',iconCls:'table_remove',autodisabled:true,disabled:true,optRecords: 'one'}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'代理商姓名',
            dataIndex:'name'
        },
        {
            text:'代理商账号',
            dataIndex:'agentId'
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
        },{
            text:'总充值金额',
            dataIndex:'sumAddBalance'
        },{
            text:'总充值实际金额',
            dataIndex:'sumAddRealBalance'
        },{
            text:'余额',
            dataIndex:'balance'
        }
    ]
});