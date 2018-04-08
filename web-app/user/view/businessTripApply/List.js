Ext.define('user.view.businessTripApply.List', {
    extend: 'public.BaseList',
    alias: 'widget.businessTripApplyList',
    autoScroll: true,
    store:Ext.create('user.store.BusinessTripApplyStore'),
    title: '出差申请',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'BusinessTripApplyList'
});