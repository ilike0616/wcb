/**
 * Created by like on 2015-04-07.
 */
Ext.define("user.view.employee.EmployeeModifyPwd",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeModifyPwd',
    title: '更改密码',
    layout:{
        align: 'center',
        pack: 'center',
        type: 'vbox'
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                xtype: 'panel',
                title: '更改密码',
                items: [{
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width: 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel: '原密码',
                        name: 'oldPassword',
                        allowBlank: false,
                        beforeLabelTextTpl: required
                    }, {
                        fieldLabel: '新密码',
                        name: 'newPassword',
                        inputType: 'password',
                        allowBlank: false,
                        beforeLabelTextTpl: required,
                        maxLength: 50
                    }, {
                        fieldLabel: '确认新密码',
                        name: 'confirmPassword',
                        inputType: 'password',
                        allowBlank: false,
                        beforeLabelTextTpl: required,
                        maxLength: 50,
                        vtype:'validPwd'
                    }],
                    buttons: [{
                        text: '修改', itemId: 'modify_password_button', iconCls: 'table_save'
                    }]
                }]
            }]
        });
        me.callParent(arguments);
    }
})