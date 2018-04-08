/**
 * Created by guozhen on 2015/04/23
 */
Ext.define('agent.view.addBalance.List', {
    extend: 'public.BaseList',
    alias: 'widget.addBalanceList',
    autoScroll: true,
    store: Ext.create('agent.store.AddBalanceStore'),
    title: '充值记录',
    split:true,
    alertName: 'name',
    forceFit:true,
    tbar:[
        /*{xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'agentAdd'}*/
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'操作者',
            dataIndex:'operatorName'
        },
        {
            text:'被充值用户',
            dataIndex:'prepaidUserName'
        },
        {
            text:'充值前余额',
            dataIndex:'preBalance'
        },
        {
            text:'充值后余额',
            dataIndex:'postBalance'
        },
        {
            text:'充值金额',
            dataIndex:'balance'
        },{
            text:'充值实际金额',
            dataIndex:'realAddBalance'
        },{
            text:'备注',
            dataIndex:'remark'
        },{
            text:'充值类型',
            dataIndex:'type',
            renderer:function(value){
                if (value === 1) {
                    return '充值';
                }
                return '退费';
            }
        },{
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});