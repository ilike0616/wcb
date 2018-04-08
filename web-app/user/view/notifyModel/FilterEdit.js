/**
 * Created by like on 2015-04-17.
 */
Ext.define("user.view.notifyModel.FilterEdit", {
    extend: 'public.BaseWin',
    alias: 'widget.notifyModelFilterEdit',
    requires: [
        'public.BaseForm'
    ],
    title: '修改条件组',
    items: [
        {
            xtype: 'baseForm',
            paramColumns: 1,
            items: [
                {
                    xtype: "hidden",
                    name: "id"
                },
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
                    fieldLabel: "描述",
                    allowBlank : false
                },
                {
                    fieldLabel: "子条件关系",
                    name: "childRelation",
                    xtype: "combo",
                    autoSelect: true,
                    forceSelection: true,
                    typeAhead: true,
                    allowBlank : false,
                    value:1,
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
                    valueField: 'id',
                    rootVisible: false,
                    store: Ext.create('user.store.NotifyModelFilterForEditStore')

                }
            ],
            buttons: [
                {
                    text: '保存', itemId: 'save', iconCls: 'table_save', autoUpdate: true, target: 'notifyModelFilterTree'
                }
            ]
        }
    ]
});