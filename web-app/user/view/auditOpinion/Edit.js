Ext.define("user.view.auditOpinion.Edit",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.auditOpinionEdit',
    requires: [
       'public.BaseForm'
    ],
    itemId: 'auditOpinionEdit',
    //modal: true,
    width: 700,
    layout: 'fit',
    title: '修改审核意见',
    items: [
        {
            xtype: 'baseForm',
            changeWinTitle:false,
            paramColumns:1,
            viewId:'AuditOpinionEdit'
        }
    ]
});
