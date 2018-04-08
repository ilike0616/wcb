Ext.define('user.view.overtimeApply.List', {
    extend: 'public.BaseList',
    alias: 'widget.overtimeApplyList',
    autoScroll: true,
    store:Ext.create('user.store.OvertimeApplyStore'),
    title: '加班申请',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'OvertimeApplyList'
});