/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('admin.view.userMenu.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.userMenuList',
    autoScroll: true,
    store: Ext.create('admin.store.UserMenuStore'),
    title: '菜单管理',
    forceFit:true,
    reserveScrollbar: true,
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            displayField : 'text',
            containerScroll: true
        }
    },
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 2
        })
    ],
    tbar:[
        {xtype:'button',text:'保存',itemId:'saveButton',iconCls:'table_save'}
    ],
    columns: [{
        xtype: 'treecolumn', //this is so we know which column will show the tree
        text: '菜单名称',
        width : '30%',
        dataIndex: 'text',
        editor: {
            allowBlank: false
        }
    },{
        text: '图标',
        dataIndex: 'iconCls',
        editor: {
        }
    },{
        text: '初始化名称',
        dataIndex: 'moduleName'
    }]
});