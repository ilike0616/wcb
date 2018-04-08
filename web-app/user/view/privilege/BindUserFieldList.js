/**
 * Created by guozhen on 2014/12/19.
 */
Ext.define('user.view.privilege.BindUserFieldList', {
    extend: 'public.BaseList',
    alias: 'widget.bindUserFieldList',
    autoScroll: true,
    store: Ext.create('user.store.BindPrivilegeUserFieldStore'),
    split:true,
    forceFit:true,
    enableBasePaging : false,
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 2
        })
    ],
    tbar:[
        {xtype:'button',text:'绑定',itemId:'bindBtn',iconCls:'table_save'}
    ],
    columns:[{
        text:'字段标题',
        dataIndex:'text',
        editable:true
    },{
        text:'查看是否屏蔽',
        width : 50,
        align : 'center',
        xtype:'checkcolumn',
        dataIndex:'unShow'
    },{
        text:'编辑是否屏蔽',
        width : 50,
        align : 'center',
        xtype:'checkcolumn',
        dataIndex:'unEdit'
    }]
});