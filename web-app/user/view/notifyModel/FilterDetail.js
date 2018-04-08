/**
 * Created by like on 2015-04-15.
 */
Ext.define('user.view.notifyModel.FilterDetail', {
    extend: 'public.BaseList',
    alias: 'widget.notifyModelFilterDetail',
    autoScroll: true,
    store: Ext.create('user.store.NotifyModelFilterDetailStore'),
    split: true,
    forceFit: true,
    renderLoad: true,
    enableSearchField: false,
    enableComplexQuery: false,
    notifyModule: '',
    tbar: [
        {
            xtype: "button",
            text: "添加",
            operationId: "filter_detail_add",
            iconCls: "table_add",
            disabled: true
        },
        {
            xtype: "button",
            text: "修改",
            operationId: "filter_detail_update",
            iconCls: "table_save",
            disabled: true,
            autodisabled: true,
            optType: 'update'
        },
        {
            xtype: "button",
            text: "删除",
            operationId: "filter_detail_delete",
            disabled: true,
            autodisabled: true,
            auto: true,
            optType: 'del'
        }
    ],
    columns: [
        {
            xtype: 'rownumberer',
            width: 20,
            sortable: false
        },
        {
            text: '描述',
            dataIndex: 'name'
        },
        {
            text: '字段',
            dataIndex: 'fieldText'
        },

        {
            text: '字段类型',
            dataIndex: 'dbTypeName'
        },
        //{
        //    text: '期望值类型',
        //    dataIndex: 'expectType',
        //    xtype: "rowselecter",
        //    arry: [
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
            text: '期望值',
            dataIndex: 'expectText'
        },
        {
            text: '运算符',
            dataIndex: 'operator',
            xtype: "rowselecter",
            arry: [
                ['==', '等于'],
                ['!=', '不等于'],
                ['>', '大于'],
                ['>=', '大于等于'],
                ['<', '小于'],
                ['<', '小于等于'],
                ['in','包含'],
                ['notin','不包含']
            ]
        }
    ]
});