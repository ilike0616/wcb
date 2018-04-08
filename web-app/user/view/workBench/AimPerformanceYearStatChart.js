/**
 * Created by guozhen on 2015-8-10.
 */
Ext.define('user.view.workBench.AimPerformanceYearStatChart', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.aimPerformanceYearStatChart',
    autoScroll: true,
    title: '目标业绩年度排行',
    height:330,
    items:[
        {
            xtype:'baseStackedCharts',
            url:'aimPerformance/aimPerformanceYearStatChart'
        }
    ]
})

