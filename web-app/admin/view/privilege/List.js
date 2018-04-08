/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.view.privilege.List', {
    extend: 'public.BaseList',
    alias: 'widget.privilegeList',
    autoScroll: true,
    store:Ext.create('admin.store.PrivilegeStore'),
    title: '权限管理',
    split:true,
    forceFit:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true,autoShowEdit:true,target:'privilegeEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true}
    ],
    columns:[{
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'权限名称',
            dataIndex:'name'
        }
    ]
});