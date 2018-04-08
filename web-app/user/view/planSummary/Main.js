Ext.define('user.view.planSummary.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planSummaryMain',
    layout: 'border',
    items: [{
        region:'center',
        xtype: 'tabpanel',
        tabPosition: 'top',
        items:[{
                title: '日报',
                planSummaryType:'1',
                xtype:'planSummaryList',
                viewId:'PlanSummaryDayList',
                store:Ext.create('user.store.PlanSummaryStore')
            },{
                title: '周报',
                planSummaryType:'2',
                xtype:'planSummaryList',
                viewId:'PlanSummaryWeekList',
                store:Ext.create('user.store.PlanSummaryStore')
            },{
                title: '月报',
                planSummaryType:'3',
                xtype:'planSummaryList',
                viewId:'PlanSummaryMonthList',
                store:Ext.create('user.store.PlanSummaryStore')
            }]
        }]
});