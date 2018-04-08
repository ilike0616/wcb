/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.CustomerAddRankingList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.customerAddRankingList',
    autoScroll: true,
    forceFit:true,
    title: '新增客户排行',
    height:330,
    items:[
        {
            xtype:'baseStatisticCharts',
            moduleId:'customer',
            statField:'name'
        }
    ]
});