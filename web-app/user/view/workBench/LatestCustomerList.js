/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.workBench.LatestCustomerList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.latestCustomerList',
    autoScroll: true,
    title:'最新客户',
    height:330,
    items:[
        {
            xtype:'baseList',
            store:Ext.create('user.store.workBench.LatestCustomerStore'),
            split:true,
            selType: 'rowmodel',
            forceFit:true,
            renderLoad:true,
            operateBtn : false, //关闭操作按钮
            enableBasePaging : false,//false 关闭翻页按钮
            enableSearchField:false,//false 关闭搜索框
            enableComplexQuery:false,//false 关闭查询功能
            enableToolbar:false, //关闭工具条
            viewId:'LatestCustomerList'
        }
    ]
});