Ext.define('user.view.audit.List', {
    extend: 'public.BaseList',
    alias: 'widget.auditList',
    autoScroll: true,
    store:Ext.create('user.store.AuditStore'),
    title: '审批申请',
    split:true,
    forceFit:true,
    //renderLoad:true,
    viewId:'AuditList'
});