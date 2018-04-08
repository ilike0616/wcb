/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('admin.view.dept.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.deptList',
    autoScroll: true,
    store: Ext.create('admin.store.DeptStore'),
    title: '部门管理',
    forceFit:true,
    reserveScrollbar: true,
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            displayField : 'name',
            containerScroll: true
        }
    },
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true}
    ],
    columns: [{
        xtype: 'treecolumn', //this is so we know which column will show the tree
        text: '部门名称',
        width : '50%',
        dataIndex: 'name'
    },{
        text: '创建日期',
        dataIndex: 'dateCreated',
        align: 'center',
        width : '20%',
        renderer: Ext.util.Format.dateRenderer('Y-m-d H:m:s')
    },{
        text: '修改时间',
        dataIndex: 'lastUpdated',
        width : '20%',
        renderer: Ext.util.Format.dateRenderer('Y-m-d H:m:s')
    }]
});