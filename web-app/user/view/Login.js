Ext.define('user.view.Login', {
    extend: 'Ext.window.Window',
    alias: 'widget.login',
    autoShow: true,
//	height:170,
    width: 360,
    layout: {
        type: 'fit'
    },
    iconCls: 'key',
    title: '用户登陆',
    closeAction: 'hide',
    closable: false,
    items: [
        {
            xtype: 'tabpanel',
            items: [
                {
                    title: '邮箱或手机登陆',
                    itemId: 'emailForm',
                    xtype: 'form',
                    frame: false,
                    active:true,
                    bodyPadding: 15,
                    defaults: {
                        xtype: 'textfield',
                        anchor: '100%',
                        labelWidth: 80,
                        allowBlank: false,
                        msgTarget: 'under'
                    },
                    fieldDefaults: {
                        labelAlign: 'right'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            name: 'email',
                            fieldLabel: '邮箱账号',
                            emptyText: '请输入已认证邮箱地址或手机号'
                        },
                        {
                            inputType: 'password',
                            name: 'password',
                            fieldLabel: '密码',
                            emptyText: '请输入密码',
                            enableKeyEvents: true,
                            msgTarget: 'side'
                        }, {
                            xtype: 'hiddenfield',
                            name: 'clientType'
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items: [
                                {
                                    xtype: 'button',
                                    itemId: 'register',
                                    iconCls: 'menu_profile',
                                    text: '注册'
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'cancel',
                                    iconCls: 'cancel',
                                    text: '取消'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'Emailsubmit',
                                    formBind: true,
                                    iconCls: 'key-go',
                                    text: '登陆'
                                }
                            ]
                        }
                    ]
                },
                {
                    title: '账号登陆',
                    itemId:'accountForm',
                    xtype: 'form',
                    frame: false,
                    bodyPadding: 15,
                    defaults: {
                        xtype: 'textfield',
                        anchor: '100%',
                        labelWidth: 80,
                        allowBlank: false,
                        msgTarget: 'under'
                    },
                    fieldDefaults: {
                        labelAlign: 'right'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            name: 'userId',
                            fieldLabel: '企业帐号',
                            emptyText: '请输入企业账号'
                        },
                        {
                            xtype: 'textfield',
                            name: 'account',
                            emptyText: '请输入员工帐号',
                            fieldLabel: '员工帐号'
                        },
                        {
                            inputType: 'password',
                            name: 'password',
                            fieldLabel: '密码',
                            emptyText: '请输入密码',
                            enableKeyEvents: true,
                            msgTarget: 'side'
                        }, {
                            xtype: 'hiddenfield',
                            name: 'clientType'
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items: [
                                {
                                    xtype: 'button',
                                    itemId: 'register',
                                    iconCls: 'menu_profile',
                                    text: '注册'
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'cancel',
                                    iconCls: 'cancel',
                                    text: '取消'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'Accountsubmit',
                                    formBind: true,
                                    iconCls: 'key-go',
                                    text: '登陆'
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
});