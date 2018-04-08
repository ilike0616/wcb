/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.ContractOrderAmountRankingList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.contractOrderAmountRankingList',
    autoScroll: true,
    forceFit:true,
    title: '销售额排行',
    height:330,
    items:[
        {
            xtype:'baseStatisticCharts',
            moduleId:'customer_follow',
            statField:'discountMoney',
            statType:'sum'
        }
    ]
});