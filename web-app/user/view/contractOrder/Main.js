/**
 * Created by like on 2015/7/17.
 */
Ext.define('user.view.contractOrder.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.contractOrderMain',
    layout : {
        type: 'border',
        pack : 'start ',
        align: 'stretch'
    },
    defaults:{
        split:true,
        border: false
    },
    items: [
        {
            xtype: 'contractOrderList',
            store:Ext.create('user.store.ContractOrderStore'),
            region: 'center',
            flex : 1 ,
            split: true,
            floatable: true
        },
        {
            xtype:'tabpanel',
            hidden:true,
            activeIndex:0,
            region: 'south',
            flex : 1,
            split: true,
            floatable: true,
            items:[
                {
                    xtype: 'contractOrderDetailList',
                    store:Ext.create('user.store.ContractOrderDetailStore'),
                    enableSearchField:false,//false 关闭搜索框
                    enableComplexQuery:false//false 关闭查询功能
                },
                {
                    xtype: 'financeIncomeList',
                    store:Ext.create('user.store.FinanceIncomeStore'),
                    enableSearchField:false,//false 关闭搜索框
                    enableComplexQuery:false//false 关闭查询功能
                },{
                    xtype: 'sfaExecuteMain'
                }
            ]
        }
    ]
});