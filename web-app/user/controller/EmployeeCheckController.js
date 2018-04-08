/**
 * Created by like on 2015/5/26.
 */
Ext.define('user.controller.EmployeeCheckController', {
    extend: 'Ext.app.Controller',
    views: ['employeeCheck.Main', 'employeeCheck.Email', 'employeeCheck.Mobile'],
    stores: ['EmployeeStore'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'emailCheckPanel',
            selector: 'employeeCheckMain employeeCheckEmail'
        }, {
            ref: 'emailCheckedPanel',
            selector: 'employeeCheckMain panel[itemId=emailChecked]'
        }, {
            ref: 'mobileCheckPanel',
            selector: 'employeeCheckMain employeeCheckMobile'
        }, {
            ref: 'mobileCheckedPanel',
            selector: 'employeeCheckMain panel[itemId=mobileChecked]'
        }, {
            ref: 'verifyBtn',
            selector: 'employeeCheckMain employeeCheckMobile form button#verify'
        }
    ],
    init: function () {
        this.control({
            'employeeCheckMain': {
                afterrender: function (view) {
                    var me = this;
                    Ext.Ajax.request({
                        url: 'employee/me',
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            if (d.success) {
                                var emp = d.data;
                                if (emp.checkMobile) {
                                    me.hasCheckedMobile(emp.mobile);
                                } else {
                                    me.getMobileCheckPanel().down('form').down('field[name=mobile]').setValue(emp.mobile);
                                }
                                if (emp.checkEmail) {
                                    me.hasCheckedEmail(emp.email);
                                } else {
                                    me.getEmailCheckPanel().down('form').down('field[name=email]').setValue(emp.email);
                                }
                            } else {
                                Ext.example.msg('提示', d.msg);
                            }
                        },
                        failure: function (response, opts) {
                        }
                    });
                }
            },
            //获取手机验证码
            'employeeCheckMain employeeCheckMobile form button#verify': {
                click: function (btn) {
                    var me = this;
                    var mobile = btn.up('form').down('field[name=mobile]').getValue();
                    if (mobile == "") {
                        Ext.example.msg('提示', '请输入手机号码！');
                        btn.up('form').down('field[name=mobile]').focus();
                        return;
                    }
                    //获取验证码
                    Ext.Ajax.request({
                        url: 'smsRecord/sendVerifyCode',
                        params: {mobile: mobile},
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            if (d.success) {
                                //启动间隔时间计时器
                                me.timeTask = {
                                    run: me.countDown,
                                    interval: 1000,
                                    scope: me
                                };
                                Ext.TaskManager.start(me.timeTask);
                            } else {
                                Ext.example.msg('提示', d.msg);
                            }
                        },
                        failure: function (response, opts) {
                        }
                    });
                }
            },
            //提交验证手机号
            'employeeCheckMain employeeCheckMobile form button#mobile_check_button': {
                click: function (btn) {
                    var me = this, frm = btn.up('form');
                    if (!frm.getForm().isValid())
                        return;
                    var mobile = frm.down('field[name=mobile]').getValue();
                    frm.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        url: 'employee/checkMobile',
                        method: 'POST',
                        params: '',
                        submitEmptyText: false,
                        success: function (form, action) {
                            me.hasCheckedMobile(mobile);
                        },
                        failure: function (form, action) {
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            },
            'employeeCheckMain employeeCheckEmail form button#email_check_button': {
                click: function (btn) {
                    var me = this, frm = btn.up('form');
                    if (!frm.getForm().isValid())
                        return;
                    var email = frm.down('field[name=email]').getValue();
                    frm.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        url: 'employee/sendCheckEmail',
                        method: 'POST',
                        params: '',
                        submitEmptyText: false,
                        success: function (form, action) {
                            Ext.example.msg('提示', "邮件已发送，请登录邮箱进行验证！");
                        },
                        failure: function (form, action) {
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            }
        });
    },
    hasCheckedMobile: function (mobile) {
        var mobilePanel = this.getMobileCheckPanel();
        var checked = this.getMobileCheckedPanel();
        mobilePanel.hide();
        checked.show();
        checked.up('panel').getEl().down("#mobile").setHTML("手机号码已认证：" + mobile);
    },
    hasCheckedEmail: function (email) {
        var emailPanel = this.getEmailCheckPanel();
        var checked = this.getEmailCheckedPanel();
        emailPanel.hide();
        checked.show();
        checked.up('panel').getEl().down("#email").setHTML("邮箱账号已认证：" + email);
    },
    remaining: 60,  //验证获取时间间隔
    countDown: function () {
        var btn = this.getVerifyBtn();
        btn.setDisabled(true);
        btn.setText(this.remaining-- + '秒后可以重新获取')
        if (this.remaining <= 0) {
            Ext.TaskManager.stop(this.timeTask);
            this.remaining = 60;
            btn.setDisabled(false);
            btn.setText('获取验证码')
        }
    }
});