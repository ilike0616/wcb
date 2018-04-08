Ext.define("admin.view.subAgent.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.subAgentAdd',
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '添加代理商',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 150,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                            fieldLabel : '代理商姓名',
                            name : 'name',
                            allowBlank : false,
                            beforeLabelTextTpl: required
                        },{
                            fieldLabel : '代理商账号',
                            name : 'agentId',
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
                            fieldLabel: '是否允许有下级代理商',
                            name: 'isAllowLowerAgent',
                            xtype:'checkboxfield',
                            boxLabelAlign:'before',
                            inputValue:true,
                            uncheckedValue:false
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
                        }
                    ],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'subAgentList'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})