/**
 * Created by like on 2015-05-06.
 */
Ext.define('admin.view.paymentRecord.List', {
    extend: 'public.BaseList',
    alias: 'widget.paymentRecordList',
    autoScroll: true,
    store: Ext.create('admin.store.PaymentRecordStore'),
    title: '扣费记录',
    split:true,
    alertName: '',
    forceFit:true,
    tbar:[],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'企业名称',
            dataIndex:'user.name'
        },
        {
            text:'扣费金额',
            dataIndex:'amountFee'
        },
        {
            text:'扣前余额',
            dataIndex:'preBalance'
        },
        {
            text:'扣后余额',
            dataIndex:'postBalance'
        },
        {
            text:'使用人数',
            dataIndex:'syNum'
        },
        {
            text:'扣费人数',
            dataIndex:'kfNum'
        },
        {
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d'
        }
    ]
});