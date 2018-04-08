/**
 * Created by guozhen on 2014-07-04.
 */
Ext.define('admin.view.role.List', {
    extend: 'public.BaseList',
    alias: 'widget.roleList',
    autoScroll: true,
    store:'RoleStore',
    title: '角色管理',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemid:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemid:'updateButton',iconCls:'table_save',disabled:true},
        {xtype:'button',text:'删除',itemid:'deleteButton',iconCls:'table_remove',disabled:true}
        /*,
         {xtype:'button',text:'保存',itemid:'saveButton',iconCls:'table_save',disabled:true}*/
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'角色名称',
            dataIndex:'roleName'
        },{
            text : '权限列表',
            dataIndex:'privilegesNames'
        }
    ]
});