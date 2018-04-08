/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.SaleChanceMoneyRankingChart', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.saleChanceMoneyRankingChart',
    autoScroll: true,
    forceFit:true,
    title: '销售商机漏斗',
    height:330,
    items:[
        {
            title1:'销售商机金额',
            moduleId:'sale_chance',
            statType:'sum', // 统计类型：count或sum
            statField:'discountMoney', // 汇总字段
            groupField:'followPhase', // 分组字段
            xtype:'baseFunnelChart'
        }
    ]
});