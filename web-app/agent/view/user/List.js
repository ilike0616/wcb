/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('agent.view.user.List', {
    extend: 'public.BaseList',
    alias: 'widget.userList',
    autoScroll: true,
    store: Ext.create('agent.store.UserStore'),
    title: '用户管理',
    split:true,
    forceFit:true,
    tbar:[
        //{xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'userAdd',optRecords: 'no'},
        //{xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'userEdit',optRecords: 'one'},
        //{xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,optRecords: 'many'},
        {xtype:'button',text:'充值',itemId:'addBalanceButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
        {xtype:'button',text:'退费',itemId:'reduceBalanceButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'}
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'企业账号',
            dataIndex:'userId'
        },{
            text:'用户名称',
            dataIndex:'name'
        },{
            text:'总充值金额',
            dataIndex:'sumAddBalance'
        },{
            text:'总充值实际金额',
            dataIndex:'sumAddRealBalance'
        },{
            text:'余额',
            dataIndex:'balance'
        },{
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        },{
            text:'修改时间',
            dataIndex:'lastUpdated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});