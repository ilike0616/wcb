Ext.define('user.view.goOutApply.List', {
    extend: 'public.BaseList',
    alias: 'widget.goOutApplyList',
    autoScroll: true,
    store:Ext.create('user.store.GoOutApplyStore'),
    title: '外出申请',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'GoOutApplyList'
});