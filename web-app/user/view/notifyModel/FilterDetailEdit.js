/**
 * Created by like on 2015-04-17.
 */
Ext.define("user.view.notifyModel.FilterDetailEdit", {
    extend: 'public.BaseWin',
    alias: 'widget.notifyModelFilterDetailEdit',
    requires: [
        'public.BaseForm'
    ],
    title: '添加条件',
    items: [
        {
            xtype: 'baseForm',
            paramColumns:1,
            items: [
                {
                    xtype: "hidden",
                    name: "id"
                },
                {
                    xtype: "hidden",
                    name: "isDict"
                },
                {
                    xtype: "hidden",
                    name: "dbType",
                    fieldLabel: "字段类型"
                },
                {
                    xtype: "textfield",
                    name: "name",
                    fieldLabel: "描述"
                },
                {
                    xtype: "combo",
                    name: "fieldName",
                    fieldLabel: "字段名",
                    valueField:'fieldName',
                    displayField:'fieldText',
                    allowBlank : false,
                    forceSelection : true,
                    store:''
                },
                {
                    fieldLabel:"仅需修改",
                    name:"justEdit",
                    xtype:"checkbox",
                    note:null,
                    uncheckedValue:false,
                    inputValue:true
                },
                {
                    xtype: "combo",
                    name: "operator",
                    fieldLabel: "运算符",
                    allowBlank : false,
                    value:'==',
                    store:[
                        ['==', '等于'],
                        ['!=', '不等于'],
                        ['>', '大于'],
                        ['>=', '大于等于'],
                        ['<', '小于'],
                        ['<', '小于等于'],
                        ['in','包含'],
                        ['notin','不包含']
                    ]
                },
                //{
                //    fieldLabel: "期望值类型",
                //    name: "expectType",
                //    xtype: "combo",
                //    autoSelect: true,
                //    forceSelection: true,
                //    typeAhead: true,
                //    emptyText: "-- 请选择 --",
                //    store: [
                //        [
                //            1,
                //            "实际值"
                //        ],
                //        [
                //            2,
                //            "字段名"
                //        ]
                //    ]
                //},
                {
                    fieldLabel: '预期值',
                    name: 'expectValue',
                    itemId : 'expectValue',
                    allowBlank : false
                }, {
                    xtype: "hidden",
                    name: "expectType",
                    value:1
                }
            ],
            buttons: [
                {
                    text: '保存', itemId: 'save', iconCls: 'table_save', autoUpdate: true, target: 'notifyModelFilterDetail'
                }
            ]
        }
    ]
});