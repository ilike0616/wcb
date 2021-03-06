/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.LatestSaleChanceList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.latestSaleChanceList',
    autoScroll: true,
    title:'最新商机',
    height:330,
    items:[
        {
            xtype:'baseList',
            selType: 'rowmodel',
            store:Ext.create('user.store.workBench.LatestSaleChanceStore'),
            split: true,
            forceFit: true,
            renderLoad: true,
            operateBtn: false, //关闭操作按钮
            enableBasePaging: false,//false 关闭翻页按钮
            enableSearchField: false,//false 关闭搜索框
            enableComplexQuery: false,//false 关闭查询功能
            enableToolbar: false, //关闭工具条
            viewId: 'LatestSaleChanceList'
        }
    ]
});