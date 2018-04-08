/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('public.BasePopList', {
    extend: 'public.BaseList',
    alias: 'widget.basePopList',
    autoScroll: true,
    split:true,
    forceFit:true,
    initComponent: function() {
        var me = this;
        var mode = 'SINGLE';
        if(Ext.typeOf(me.mode) != 'undefined' && me.mode == 'MULTI'){
            mode = 'MULTI';
        }
        Ext.applyIf(me, {
            selModel : Ext.create("Ext.selection.CheckboxModel",{
                mode : mode,
                allowDeselect : true
            }),
            store : me.store,
            viewId : me.viewId,
            url:me.url,
            columns : me.columns,
            operateBtn : me.operateBtn, //关闭操作按钮
            enableBasePaging : me.enableBasePaging,//false 关闭翻页按钮
            enableSearchField:me.enableSearchField,//false 关闭搜索框
            enableComplexQuery:me.enableComplexQuery,//false 关闭查询功能
            enableToolbar:me.enableToolbar //关闭工具条
        })
        me.callParent(arguments);
    }
});