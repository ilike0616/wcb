Ext.define('user.view.leaveApply.List', {
    extend: 'public.BaseList',
    alias: 'widget.leaveApplyList',
    autoScroll: true,
    store:Ext.create('user.store.LeaveApplyStore'),
    title: '请假申请',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'LeaveApplyList'
});