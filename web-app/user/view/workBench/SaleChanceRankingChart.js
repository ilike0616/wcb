/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.SaleChanceRankingChart', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.saleChanceRankingChart',
    autoScroll: true,
    forceFit:true,
    title: '销售商机漏斗',
    height:330,
    items:[
        {
            title1:'销售商机数量',
            moduleId:'sale_chance',
            groupField:'followPhase',// 分组和汇总字段(count)
            xtype:'baseFunnelChart'
        }
    ]
});