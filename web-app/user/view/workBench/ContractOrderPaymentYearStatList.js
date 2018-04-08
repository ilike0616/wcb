/**
 * Created by guozhen on 2015-7-1.
 */
Ext.define('user.view.workBench.ContractOrderPaymentYearStatList', {
    extend: 'Ext.app.Portlet',
    alias: 'widget.contractOrderPaymentYearStatList',
    autoScroll: true,
    title: '订单回款年度排行',
    height:330,
    items:[
        {
            xtype:'baseYearStatList',
            statType: 'CONTRACT_ORDER_PAYMENT',
            store:Ext.create('user.store.workBench.ContractOrderYearStatStore'),
            firstHeader:'员工名称',
            firstHeaderDataIndex:'employeeName'
        }
    ]
});