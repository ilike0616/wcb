/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.CustomerFollowRankingList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.customerFollowRankingList',
    autoScroll: true,
    forceFit:true,
    title: '客户跟进排行',
    height:330,
    items:[
        {
            xtype:'baseStatisticCharts',
            moduleId:'customer_follow',
            statField:'subject'
        }
    ]
});