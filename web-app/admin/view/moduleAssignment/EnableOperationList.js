/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('admin.view.moduleAssignment.EnableOperationList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.enableOperationList',
    autoScroll: true,
    store: Ext.create('admin.store.UserOperationStore'),
    title: '操作管理',
    split:true,
    forceFit:true,
    enableBasePaging : false,
    initComponent: function() {
        var me = this;
        var columns = [{
            text:'是否启用',
            width : 50,
            align : 'center',
            xtype:'checkcolumn',
            dataIndex:'isEnable'
        },{
            text:'操作名称',
            dataIndex:'text',
            editor: {
                allowBlank: false
            }
        },{
            text: '图标',
            dataIndex: 'iconCls',
            editor: {
            }
        }]
        if (Ext.typeOf(me.enableToolbar) == 'undefined') {
            Ext.applyIf(me, {
                tbar:[
                    {xtype:'button',text:'启用',itemId:'enableButton',iconCls:'table_save'},
                    {xtype:'button',text:'设置',itemId:'userOptConditionButton',iconCls:'table_save'}
                ],
                plugins : [
                    Ext.create('Ext.grid.plugin.RowEditing', {
                        saveBtnText: '确定',
                        cancelBtnText: '取消',
                        clicksToEdit: 2
                    })
                ],
                columns : columns
            })
        }else{
            Ext.applyIf(me, {
                columns : columns
            })
        }
        me.callParent(arguments);
    }
});