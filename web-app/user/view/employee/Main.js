Ext.define('user.view.employee.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeMain',
    layout: 'border',
    items: [
        {
            region:'west',
            title:'部门',
            xtype: 'employeeDeptList',
            width: 300,
            split: true,
            collapsible: true,
            layout: 'fit'
        },
        {
            region:'center',
            xtype: 'employeeList',
            title: '员工管理',
            split: true,
            layout: 'fit'
        }
    ]
});