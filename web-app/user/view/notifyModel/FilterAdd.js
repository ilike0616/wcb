/**
 * Created by like on 2015-04-16.
 */
Ext.define("user.view.notifyModel.FilterAdd", {
    extend: 'public.BaseWin',
    alias: 'widget.notifyModelFilterAdd',
    requires: [
        'public.BaseForm'
    ],
    title: '添加条件组',
    items: [
        {
            xtype: 'baseForm',
            paramColumns: 1,
            items: [
                {
                    xtype: "hidden",
                    name: "moduleId"
                },
                {
                    xtype: "hidden",
                    name: "notifyModel"
                },
                {
                    xtype: "textfield",
                    name: "name",
                    allowBlank : false,
                    fieldLabel: "描述"
                },
                {
                    fieldLabel: "子条件关系",
                    name: "childRelation",
                    xtype: "combo",
                    autoSelect: true,
                    forceSelection: true,
                    typeAhead: true,
                    allowBlank : false,
                    emptyText: "-- 请选择 --",
                    store: [
                        [
                            1,
                            "并且"
                        ],
                        [
                            2,
                            "或者"
                        ]
                    ]
                },
                {
                    xtype: 'baseComboBoxTree',
                    name: 'parentNotifyModelFilter',
                    fieldLabel: '上级条件组',
                    displayField: 'name',
                    valueField:'id',
                    rootVisible: false,
                    forceSelection: true,
                    store : Ext.create('user.store.NotifyModelFilterForEditStore')

                }
            ],
            buttons: [
                {
                    text: '保存', itemId: 'save', iconCls: 'table_save', autoInsert: true, target: 'notifyModelFilterTree'
                }
            ]
        }
    ]
});