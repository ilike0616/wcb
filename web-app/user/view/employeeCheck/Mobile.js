/**
 * Created by like on 2015/5/26.
 */
Ext.define("user.view.employeeCheck.Mobile", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeCheckMobile',
    title: '手机验证',
    items: [{
        xtype: 'form',
        bodyStyle: 'padding:5px 5px 5px',
        layout: {
            type: 'table',
            columns: 2
        },
        fieldDefaults: {
            colspan: 2,
            labelWidth: 100,
            width: 400
        },
        defaults: {
            msgTarget: 'side', //qtip title under side
            xtype: 'textfield'
        },
        items: [
            {
                fieldLabel: '手机号',
                name: 'mobile',
                allowBlank: false,
                beforeLabelTextTpl: required
            }, {
                name: 'verify',
                fieldLabel: '短信验证码',
                width: 200,
                colspan: 1
            }, {
                text: '获取验证码',
                xtype: 'button',
                itemId: 'verify',
                width: 200,
                colspan: 1
            }],
        buttons: [{
            text: '认证', itemId: 'mobile_check_button', iconCls: 'table_save'
        }]
    }]
});