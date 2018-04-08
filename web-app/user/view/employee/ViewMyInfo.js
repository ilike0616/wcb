/**
 * Created by like on 2015/9/8.
 */
Ext.define("user.view.employee.ViewMyInfo",{
    extend: 'public.BaseWin',
    alias: 'widget.employeeViewMyInfo',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
                readOnly:true
            },
            viewId:'EmployeeViewMyInfo',
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});