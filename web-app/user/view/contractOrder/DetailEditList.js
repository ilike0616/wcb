Ext.define('user.view.contractOrder.DetailEditList', {
    extend: 'public.BaseEditList',
    alias: 'widget.contractOrderDetailEditList',
    autoScroll: true,
    store:Ext.create('user.store.ContractOrderDetailStore'),
    title: '订单明细',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'ContractOrderDetailEditList'
});