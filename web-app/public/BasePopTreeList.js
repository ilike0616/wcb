/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('public.BasePopTreeList', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.basePopTreeList',
    autoScroll: true,
    split:true,
    forceFit:true,
    initComponent: function() {
        var me = this;
        // 默认只显示一列
        var columns = [{
            xtype: 'treecolumn',
            text: me.fieldLabel,
            dataIndex: me.dataIndexName
        }];
        // 想多列的话，自己前台传过来columns
        if(Ext.typeOf(me.columns) != 'undefined'){
            columns = me.columns;
        }
        Ext.applyIf(me, {
            columns: columns
        })
        me.callParent(arguments);
    }
});