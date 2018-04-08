Ext.define('user.view.contractOrder.List', {
    extend: 'public.BaseList',
    alias: 'widget.contractOrderList',
    autoScroll: true,
    store:Ext.create('user.store.ContractOrderStore'),
    title: '订单管理',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'ContractOrderList'
});