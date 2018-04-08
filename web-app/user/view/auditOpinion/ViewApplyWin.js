/**
 * Created by like on 2015/8/6.
 */
Ext.define("user.view.auditOpinion.ViewApplyWin",{
    extend: 'public.BaseWin',
    alias: 'widget.auditOpinionViewApplyWin',
    title: '查看申请单',
    width: 720,
    initComponent: function() {
        var me = this,items;
        var applyView = {
            title: '',
            xtype: 'auditOpinionViewApply',
            itemId: 'auditOpinionViewApply',
            viewId: me.viewId,
            paramColumns:2,
            changeWinTitle:false,
            split: true
        };
        items =  [applyView]
        Ext.applyIf(me, {
            items : items
        });
        me.callParent(arguments);
    }
});