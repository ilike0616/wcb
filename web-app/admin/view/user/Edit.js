Ext.define("admin.view.user.Edit", {
    extend: 'Ext.window.Window',
    alias: 'widget.userEdit',
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '修改企业信息',
    items: [
        {
            xtype: 'form',
            layout: 'column',
            defaults: {
                msgTarget: 'side',
                xtype: 'textfield',
                padding: '3 5 3 5',
                labelWidth : 120,
                beforeLabelTextTpl: required
            },
            items: [
                {
                    fieldLabel: '企业账号',
                    name: 'userId',
                    allowBlank: false,
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '企业名称',
                    name: 'name',
                    allowBlank: false,
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '选择版本',
                    name: 'edition',
                    allowBlank: false,
                    columnWidth: 1 / 2,
                    xtype: 'combo',
                    disabled:true,
                    readOnly: true,
                    valueField: 'id',
                    displayField: 'name',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['id', 'name', 'monthlyFee'],
                        proxy: {
                            type: 'ajax',
                            api: {
                                read: 'edition/list'
                            },
                            reader: {
                                type: 'json',
                                root: 'data',
                                successProperty: 'success',
                                totalProperty: 'total'
                            },
                            simpleSortMode: true
                        },
                        autoLoad: true
                    })
                },
                {
                    fieldLabel: '所属代理商',
                    name: 'agent',
                    displayField: 'name',
                    valueField: 'id',
                    xtype: 'combo',
                    autoSelect: true,
                    forceSelection: true,
                    emptyText: '-- 请选择 --',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['id', 'name'],
                        proxy: {
                            type: 'ajax',
                            api: {
                                read: 'agent/commonList'
                            },
                            reader: {
                                type: 'json',
                                root: 'data',
                                successProperty: 'success',
                                totalProperty: 'total'
                            },
                            simpleSortMode: true
                        },
                        autoLoad: false
                    }),
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '使用人数',
                    name: 'allowedNum',
                    xtype: 'numberfield',
                    minValue: 0,
                    allowBlank: false,
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '月费用',
                    name: 'monthlyFee',
                    readOnly: true,
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '是否试用',
                    name: 'isTest',
                    xtype: "checkbox",
                    checked: true,
                    uncheckedValue: false,
                    inputValue: true,
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '试用截止日期',
                    name: 'testDueDate',
                    xtype: "datefield",
                    format: "Y-m-d",
                    submitFormat: "Y-m-d H:i:s",
                    columnWidth: 1 / 2
                },
                {
                    fieldLabel: '扣费日期',
                    name: 'dueDate',
                    xtype: "datefield",
                    hidden: true,
                    format: "Y-m-d",
                    submitFormat: "Y-m-d H:i:s",
//                    minValue : new Date(),
                    columnWidth: 1 / 2
                },
                {
                    xtype: 'hiddenfield',
                    name: 'id'
                }
            ]
        }
    ],
    buttons: [
        {
            text: '保存', itemId: 'save', iconCls: 'table_save', autoUpdate: true, target: 'userList'
        }
    ]
})