/**
 * Created by like on 2015-05-12.
 */
Ext.define('user.controller.RegisterController', {
    extend: 'Ext.app.Controller',
    requires: [],
    views: [
        'Register'
    ],
    refs: [
        {
            ref: 'registerWin',
            selector: 'register'
        },
        {
            ref: 'verifyBtn',
            selector: 'register form button#verify'
        },
        {
            ref: 'verifyField',
            selector: 'register form field[name=verify]'
        }
    ],
    init: function () {
        this.control({
            'register': {
                close: function () {
                    if (this.timeTask) {
                        Ext.TaskManager.stop(this.timeTask);
                        this.remaining = 60;
                    }
                }
            },
            'register form button#verify': {
                click: function (btn) {
                    var me = this;
                    var mobile = btn.up('form').down('field[name=mobile]').getValue();
                    if (mobile == "") {
                        Ext.example.msg('提示', '请输入手机号码！');
                        btn.up('form').down('field[name=mobile]').focus();
                        return;
                    }
                    //除验证码外是否输入正确
                    var verifyField = me.getVerifyField();
                    verifyField.allowBlank = true;
                    if (!btn.up('form').getForm().isValid()) {
                        verifyField.allowBlank = false;
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
                            console.info('操作失败，请重试！');
                        }
                    });
                }
            },
            'register form button#submit': {
                click: function (btn) {
                    var frm = btn.up('form');
                    if (!frm.getForm().isValid()) return;
                    frm.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        url: 'user/register',
                        method: 'POST',
                        submitEmptyText: false,
                        success: function (form, action) {
                            Ext.example.msg('提示', "注册成功");
                            this.getRegisterWin().close();
                        },
                        failure: function (form, action) {
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            },
            'register form button#cancel': {
                click: function (btn) {
                    btn.up('window').close();
                }
            }
        });
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
})
;