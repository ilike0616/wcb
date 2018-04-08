Ext.define('user.view.aimPerformance.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.aimPerformanceMain',
    layout : 'border',
    items: [
        {
            xtype: 'aimPerformanceList',
            region: 'center'
        },
        {
            xtype: 'aimPerformanceChart',
            region: 'south',
            collapsible: true,
            title:'图表',
            height: 200,
            split: true
        }
    ]
});