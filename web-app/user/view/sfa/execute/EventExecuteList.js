/**
 * Created by like on 2015/9/17.
 */
Ext.define('user.view.sfa.execute.EventExecuteList', {
    extend: 'public.BaseList',
    alias: 'widget.sfaExecuteEventExecuteList',
    autoScroll: true,
    enableComplexQuery: false,
    enableSearchField: false,
    enableStatisticBtn: false,
    enableToolbar: false,
    enableSummary: false,
    showRowNumber: false,
    enableBasePaging: false,
    margin: '5 5 0 5',
    overflowY: 'auto',
    selType: 'rowmodel',
    store: Ext.create('user.store.SfaEventExecuteStore', {autoLoad: false}),
    columns: [
        {
            xtype: 'rownumberer',
            width: 30,
            sortable: false
        }, {
            text: '状态',
            dataIndex: 'state',
            xtype: "rowselecter",
            width: 70,
            arry: [
                [1, '待执行'],
                [2, '已执行'],
                [3, '已禁止']
            ]
        }, {
            text: '接受者',
            width: 150,
            xtype: "templatecolumn",
            tpl: new Ext.XTemplate(
                '<tpl if="receiverType == 1 || receiverType == 3">',
                '<tpl for="employees"><tpl if="xindex &gt; 1">,</tpl>{name}</tpl>',
                '</tpl>',
                '<tpl if="receiverType == 2">',
                '[客户]{customer.name}',
                '</tpl>'
            )
        }, {
            text: '执行时间',
            dataIndex: 'executeDate',
            xtype: "datecolumn",
            width: 130,
            format: "Y-m-d H:i"
        }, {
            text: '内容',
            xtype: "templatecolumn",
            width: 400,
            tpl: new Ext.XTemplate(
                '<tpl if="isNotify">',
                '{notifyContent}',
                '</tpl>',
                '<tpl if="isSms">',
                '[短信]{smsContent}',
                '</tpl>',
                '<tpl if="isEmail">',
                '[邮件]{emailSubject}',
                '</tpl>'
            )
        }
    ]
});