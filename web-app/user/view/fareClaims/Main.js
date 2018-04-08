/**
 * Created by like on 2015/7/22.
 */
Ext.define('user.view.fareClaims.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.fareClaimsMain',
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
            xtype: 'fareClaimsList',
            store:Ext.create('user.store.FareClaimsStore'),
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
                    xtype: 'financeExpenseList',
                    store:Ext.create('user.store.FinanceExpenseStore'),
                    enableSearchField:false,//false 关闭搜索框
                    enableComplexQuery:false//false 关闭查询功能
                }
            ]
        }
    ]
});