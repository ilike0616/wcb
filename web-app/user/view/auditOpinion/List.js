Ext.define('user.view.auditOpinion.List', {
    extend: 'public.BaseList',
    alias: 'widget.auditOpinionList',
    autoScroll: true,
    store:Ext.create('user.store.AuditOpinionStore'),
    title: '已审核意见',
    split:true,
    forceFit:true,
    //renderLoad:true,
    operateBtn:false,
    viewId:'AuditOpinionList'
});