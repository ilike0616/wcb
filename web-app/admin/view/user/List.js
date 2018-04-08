/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.user.List', {
    extend: 'public.BaseList',
    alias: 'widget.userList',
    autoScroll: true,
    store: Ext.create('admin.store.UserStore'),
    title: '用户管理',
    split:true,
    requires: [
        'public.BaseSearchField',
    ],
    initComponent: function() {
        var me = this,
            store = this.store;
        var toolbar = {
            xtype: 'container',
            itemId:'tbarContainer',
            layout: 'anchor',
            defaults: {anchor: '0',border:0},
            defaultType: 'toolbar',
            items:[
                {
                    items:[
                        {width:300,xtype : 'baseSearchField',store : store,emptyText:'请输入企业账号、用户名称'},
                    ]
                },{
                    items: [
                        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'userAdd'},
                        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'userEdit',optRecords: 'one'},
                        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,optRecords: 'many'},
                        {xtype:'button',text:'充值',itemId:'addBalanceButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
                        {xtype:'button',text:'模块&操作',itemId:'moduleAssignButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
                        {xtype:'button',text:'工作台排序',itemId:'sortUserPortalButton',iconCls:'table_save',optRecords: 'no'},
                        {xtype:'button',text:'版本切换',itemId:'changeVersionButton',iconCls:'table_save',autodisabled:true,disabled:true,autoShowEdit:true,target:'userSwitchVersion',optRecords: 'one'},
                        {xtype:'button',text:'设为自定义模板',itemId:'setDefaultModulesButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'},
                        {xtype:'button',text:'启用个性化自定义',itemId:'enableCustom',iconCls:'table_save',autodisabled:true,disabled:true,optRecords: 'one'}
                    ]
                }
            ]
        }
        Ext.applyIf(me, {
            tbar:toolbar
        });
        me.callParent(arguments);
    },
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'企业账号',
            width: 150,
            dataIndex:'userId',
            locked:true
        },{
            text:'用户名称',
            width: 200,
            dataIndex:'name',
            locked:true
        },
       {
            text:'拷贝状态',
            width:100,
            dataIndex:'versionCopyState',
            xtype:'rowselecter',
            locked:true,
            arry:[
                ['1','拷贝中'],
                ['2', '已完成']
            ]
        },
        {
            text:'使用系统模板?',
            width: 70,
            dataIndex:'useSysTpl',
            xtype: 'booleancolumn',
            trueText:'是',
            falseText:'否'
        },
        {
            text:'是模板?',
            width: 70,
            dataIndex:'isTemplate',
            xtype: 'booleancolumn',
            trueText:'是',
            falseText:'否'
        },
        {
            text:'试用?',
            width: 70,
            dataIndex:'isTest',
            xtype: 'booleancolumn',
            trueText:'是',
            falseText:'否'
        },
        {
            text:'是否启用',
            width: 70,
            dataIndex:'enabled',
            xtype: 'booleancolumn',
            trueText:'是',
            falseText:'否'
        },{
            text:'总充值金额',
            width: 150,
            dataIndex:'sumAddBalance'
        },{
            text:'总充值实际金额',
            width: 150,
            dataIndex:'sumAddRealBalance'
        },{
            text:'余额',
            width: 150,
            dataIndex:'balance'
        },{
            text:'版本',
            width: 150,
            sortName:'edition',
            dataIndex:'editionName'
        },{
            text:'使用人数',
            width: 100,
            dataIndex:'allowedNum'
        },{
            text:'月费用',
            width: 100,
            dataIndex:'monthlyFee'
        },{
            text:'扣费日期',
            width: 150,
            dataIndex:'dueDate',
            xtype:'datecolumn',
            format:'Y-m-d'
        },{
            text:'创建时间',
            width: 200,
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s',
            locked:true
        },{
            text:'修改时间',
            width: 200,
            dataIndex:'lastUpdated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});