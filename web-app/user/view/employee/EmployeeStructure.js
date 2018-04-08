/**
 * Created by guozhen on 2014/12/31.
 */
Ext.define('user.view.employee.EmployeeStructure', {
    extend: 'public.BaseWin',
    alias:'widget.employeeStructure',
    title: '员工组织架构',
    height: 480,
    width: 350,
    layout:'fit',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: {
                xtype: 'baseTreeGrid',
                store: Ext.create('user.store.EmployeeTreeStore'),
                columns:[{
                    xtype: 'treecolumn',
                    text: '员工名称',
                    dataIndex: 'name'
                }],
                forceFit:true,
                operateBtn: false,
                border: false
            },
            buttons: [
                {text: '关闭', itemId: 'close', iconCls: 'table_save', listeners: {click: function () {
                    me.close()
                }}}
            ]
        })
        me.callParent(arguments);
    }
})