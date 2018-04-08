/**
 * Created by like on 2015-05-12.
 */
Ext.define('user.view.Register', {
    extend: 'Ext.window.Window',
    alias: 'widget.register',
    autoShow: true,
    modal: true,
//	height:170,
    width: 500,
    layout: {
        type: 'fit'
    },
    iconCls: 'key',
    title: '注册账号',
    items: [
        {
            xtype: 'form',
            frame: false,
            bodyPadding: 15,
            layout: {
                type: 'table',
                columns: 2
            },
            defaults: {
                xtype: 'textfield',
                width: 420,
                labelWidth: 90,
                colspan: 2,
                allowBlank: false,
                msgTarget: 'under'
            },
            fieldDefaults: {
                labelAlign: 'right'
            },
            items: [
                {
                    name: 'userId',
                    fieldLabel: '企业帐号',
                    emptyText: '6~18个字符，字母、数字、下划线，需以字母开头',
                    vtype: 'unique',
                    allUser:true,
                    regex:/^[a-zA-Z0-9_]+$/,
                    domainClass:'com.uniproud.wcb.User',
                    minLength: 6,
                    maxLength: 18
                },
                {
                    name: 'name',
                    fieldLabel: '企业名称',
                    emptyText: '企业名称'
                },
                {
                    name: 'email',
                    fieldLabel: '邮箱',
                    vtype: 'email',
                    emptyText: 'user@example.com'
                },
                {
                    name: 'mobile',
                    fieldLabel: '手机号码',
                    regex:/^[0-9]+$/,
                    emptyText: ''
                },
                {
                    name: 'phone',
                    fieldLabel: '固定电话',
                    regex:/^[0-9-]+$/,
                    allowBlank: true
                },
                {
                    fieldLabel: '选择版本',
                    name: 'edition',
                    allowBlank: false,
                    xtype: 'combo',
                    valueField: 'id',
                    displayField: 'name',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['id', 'name'],
                        proxy: {
                            type: 'ajax',
                            api: {
                                read: 'edition/list'
                            },
                            reader: {
                                type: 'json',
                                root: 'data',
                                successProperty: 'success',
                                totalProperty: 'total'
                            },
                            simpleSortMode: true
                        },
                        autoLoad: false
                    })
                },
                {
                    xtype: 'numberfield',
                    name: 'allowedNum',
                    fieldLabel: '使用人数',
                    minValue:1
                },
                {
                    name: 'verify',
                    fieldLabel: '短信验证码',
                    width: 200,
                    colspan: 1
                },
                {
                    text: '获取验证码',
                    xtype: 'button',
                    itemId: 'verify',
                    width: 200,
                    colspan: 1
                }
            ],
            buttons: [
                {
                    itemId: 'cancel',
                    iconCls: 'cancel',
                    text: '取消'
                },
                {
                    text: '提交',
                    itemId: 'submit',
                    iconCls: 'table_save'
                }
            ]
        }
    ]
});