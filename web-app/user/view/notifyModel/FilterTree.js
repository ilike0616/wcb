/**
 * Created by like on 2015-04-15.
 */
Ext.define('user.view.notifyModel.FilterTree', {
    extend: 'public.BasePopTreeList',
    alias: 'widget.notifyModelFilterTree',
    autoScroll: true,
    store: Ext.create('user.store.NotifyModelFilterStore'),
    title: '条件组',
    forceFit: true,
    showRowNumber: false,
    autoRender: true,
    tbar: [
        {
            xtype: "button",
            text: "添加",
            operationId: "filter_add",
            iconCls: "table_add"
        },
        {
            xtype: "button",
            text: "修改",
            operationId: "filter_update",
            iconCls: "table_save",
            disabled: true,
            autodisabled:true
        },
        {
            xtype: "button",
            text: "删除",
            operationId: "filter_delete",
            disabled: true,
            autodisabled:true,
            auto:true,
            optType:'del'
        }
    ],
    columns: [
        {
            text: "描述",
            dataIndex: "name",
            orderIndex: 1,
            xtype: "treecolumn"
        },
        {
            text: "子条件关系",
            dataIndex: "childRelation",
            orderIndex: 2,
            xtype: "rowselecter",
            width: 50,
            arry: [
                [
                    1,
                    "并且"
                ],
                [
                    2,
                    "或者"
                ]
            ]

        }
    ]
});