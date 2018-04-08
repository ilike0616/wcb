Ext.define("admin.view.administrator.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.administratorEdit',
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '修改员工',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 0',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel : '管理员姓名',
                        name : 'name',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        fieldLabel : '管理员账号',
                        name : 'adminId',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        fieldLabel : '密码',
                        name : 'password',
                        inputType: 'password',
                        allowBlank : false,
                        beforeLabelTextTpl: required,
                        maxLength : 50
                    },{
                        fieldLabel : '邮箱',
                        name : 'email',
                        vtype: 'email',
                        allowBlank : false,
                        vtypeText : '邮箱格式不正确！'
                    },{
                        fieldLabel : '电话',
                        name : 'phone'
                    },{
                        fieldLabel : '手机',
                        name : 'mobile'
                    },{
                        fieldLabel : '传真',
                        name : 'fax'
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }
                    ],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'administratorList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})