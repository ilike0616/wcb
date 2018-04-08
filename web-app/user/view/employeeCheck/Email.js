/**
 * Created by like on 2015/5/26.
 */
Ext.define("user.view.employeeCheck.Email", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeCheckEmail',
    title: '邮箱验证',
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
            fieldLabel: '邮箱地址',
            name: 'email',
            allowBlank: false,
            beforeLabelTextTpl: required
        }],
        buttons: [{
            text: '认证', itemId: 'email_check_button', iconCls: 'table_save'
        }]
    }]
});