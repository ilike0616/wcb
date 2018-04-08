Ext.define("user.view.auditOpinion.WinList",{
    extend: 'public.BaseWin',
    alias: 'widget.auditOpinionWinList',
    title: '查看审核意见',
    width: 720,
    items: [
   		{
   			title: '',
           	xtype: 'auditOpinionList',
           	store:Ext.create('user.store.AuditOpinionStore'),
           	viewId:'AuditOpinionList',
           	enableToolbar:false
       	}
       ]
});
