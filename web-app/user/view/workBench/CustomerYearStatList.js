/**
 * Created by guozhen on 2015-7-1.
 */
Ext.define('user.view.workBench.CustomerYearStatList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.customerYearStatList',
    autoScroll: true,
    title: '客户年度排行',
    height:330,
    items:[
        {
            xtype:'baseYearStatList',
            statType: 'CUSTOMER_NUM',
            store:Ext.create('user.store.workBench.CustomerYearStatStore'),
            firstHeader:'员工名称',
            firstHeaderDataIndex:'employeeName'
        }
    ]
});