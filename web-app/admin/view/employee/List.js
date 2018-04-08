/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.employee.List', {
    extend: 'public.BaseList',
    alias: 'widget.employeeList',
    autoScroll: true,
    store: Ext.create('admin.store.EmployeeStore'),
    title: '员工管理',
    split:true,
    alertName: 'name',
    maxHeight:400,
    requires: [
        'public.BaseSearchField'
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
                        {width:300,xtype : 'baseSearchField',store : store,emptyText:'请输入员工姓名、员工账号、所属用户'}
                    ]
                },{
                    items: [
                        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',optRecords:'no'},
                        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true,optRecords:'one'},
                        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true,optRecords:'many'},
                        {xtype:'button',text:'绑定权限',itemId:'bindPrivilegeButton',iconCls:'table_add',autodisabled:true,disabled:true,optRecords:'one'},
                        {xtype:'button',text:'更改密码',itemId:'modify_pwd',iconCls:'table_add',autodisabled:true,disabled:true,autoShowEdit:true,target:'employeeModifyPwd',optRecords:'one'}
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
        },
        {
            text:'员工姓名',
            width: 150,
            dataIndex:'name'
        },
        {
            text:'手机',
            width: 150,
            dataIndex:'mobile'
        },
        {
            text:'邮箱',
            width: 280,
            dataIndex:'email'
        },
        {
            text:'电话',
            width: 150,
            dataIndex:'phone'
        },
        {
            text:'传真',
            width: 150,
            dataIndex:'fax'
        },
        {
            text:'是否启用',
            width: 70,
            xtype : 'booleancolumn',
            dataIndex : 'enabled',
            trueText : '是',
            falseText : '否'
        },
        {
            text : '是否锁定',
            width: 70,
            xtype : 'booleancolumn',
            dataIndex : 'accountLocked',
            trueText : '是',
            falseText : '否'
        },
        {
            text : '上级',
            width: 100,
            dataIndex : 'parentEmployeeName'
        },
        {
            text : '所属用户',
            width: 150,
            dataIndex : 'userName'
        }
    ]
});