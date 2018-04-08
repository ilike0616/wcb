/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.SaleChanceFollowRankingList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.saleChanceFollowRankingList',
    autoScroll: true,
    forceFit:true,
    title: '商机跟进排行',
    height:330,
    items:[
        {
            xtype:'baseStatisticCharts',
            url:'saleChanceFollow/saleChanceFollowRankingList'
        }
    ]
});