/**
 * Created by guozhen on 2014/12/17.
 */
Ext.define('user.view.privilege.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.privilegeMain',
    layout: 'border',
    items: [{
        region:'west',
        width: 320,
        collapsible: true,
        layout: 'fit',
        title:'用户权限',
        enableComplexQuery:false,
        xtype: 'privilegeList'
    },{
        region:'center',
        split: true,
        layout: 'fit',
        xtype: 'tabpanel',
        items : [{
            title: '绑定操作',
            name: 'bindUserOperation',
            xtype:'bindUserOperation'
        },
        {
            title: '绑定字段',
            name: 'bindUserFieldMain',
            xtype:'bindUserFieldMain'
        },
        {
            title: '绑定Portal',
            name: 'bindUserPortal',
            xtype:'bindUserPortal'
        },
        {
            title: '员工列表',
            name: 'employeeList',
            xtype: 'employeeList',
            store: Ext.create('user.store.EmployeeSubStore'),
            operateBtn:false
        }]
    }]
});