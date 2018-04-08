/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('admin.view.moduleAssignment.EnableUserPortalList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.enableUserPortalList',
    autoScroll: true,
    store: Ext.create('admin.store.UserPortalStore'),
    title: '用户Portal',
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
            text:'是否显示',
            width : 50,
            align : 'center',
            xtype:'checkcolumn',
            dataIndex:'isShow'
        },{
            text:'标题',
            dataIndex:'title',
            editor: {
                allowBlank: false
            }
        },{
            text: '高度',
            dataIndex: 'height',
            width : 50,
            editor: {
            }
        }]
        Ext.applyIf(me, {
            tbar:[
                {xtype:'button',text:'保存',itemId:'enableUserPortalButton',iconCls:'table_save'}
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
        me.callParent(arguments);
    }
});