Ext.define('admin.view.dept.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.deptMain',
    layout: 'border',
    items: [{
            region:'west',
            title:'用户列表',
            xtype: 'deptUserList',
            width: 300,
            collapsible: true,
            layout: 'fit'
        },{
            region:'center',
            xtype: 'panel',
            layout: 'border',
            items: [{
                    region:'center',
                    xtype: 'deptList',
                    collapsible: true,
                    title : '部门列表'
                },{
                    region:'south',
                    xtype:'tabpanel',
                    layout: 'fit',
                    items:[
                        {
                            xtype:'employeeList',
                            store : Ext.create('admin.store.EmployeeStore'),
                            title : '员工'
                        },{
                            xtype: 'privilegeList',
                            store : Ext.create('admin.store.PrivilegeStore'),
                            title: '权限'
                        }
                    ]
                }
            ]
        }
    ]
});