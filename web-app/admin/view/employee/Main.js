/**
 * Created by guozhen on 2015/4/2.
 */
Ext.define('admin.view.employee.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeMain',
    layout : {
        type: 'border',
        pack : 'start ',
        align: 'stretch'
    },
    defaults:{
        split:true,
        border: false
    },
    items: [
        {
            xtype: 'employeeList',
            store:Ext.create('admin.store.EmployeeStore'),
            region: 'center',
            flex : 1 ,
            split: true,
            floatable: true
        },{
            xtype:'tabpanel',
            hidden:true,
            activeIndex:0,
            region: 'south',
            flex : 1,
            split: true,
            floatable: true,
            items:[{
                    xtype: 'privilegeList',
                    store:Ext.create('admin.store.PrivilegeStore')
                }
            ]
        }
    ]
});