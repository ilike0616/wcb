Ext.define('user.view.contractOrder.DetailList', {
    extend: 'public.BaseList',
    alias: 'widget.contractOrderDetailList',
    autoScroll: true,
    store:Ext.create('user.store.ContractOrderDetailStore'),
    title: '订单明细',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'ContractOrderDetailList'
});