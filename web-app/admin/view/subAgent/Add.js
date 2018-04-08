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
                            fieldLabel : '上级代理商',
                            xtype: 'combo',
                            name : 'parentAgent',
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'id',
                            store:Ext.create('admin.store.AgentStore'),
                            forceSelection : true,
                            allowBlank : false,
                            beforeLabelTextTpl: required
                        },{
                            fieldLabel: '是否允许有下级代理商',
                            name: 'isAllowLowerAgent',
                            xtype:'checkboxfield',
                            boxLabelAlign:'before',
                            inputValue:true,
                            uncheckedValue:false
                        }, {
                            fieldLabel: '是否为内部员工',
                            name: 'isInner',
                            xtype: 'checkboxfield',
                            boxLabelAlign: 'before',
                            inputValue: true,
                            uncheckedValue: false
                        },{
                            fieldLabel: '业务范围',
                            name: 'provinces',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            emptyText: '-- 请选择 --',
                            allowBlank: true,
                            multiSelect: true,
                            disabled: true,
                            store: [
                                ['北京', '北京'],
                                ['天津', '天津'],
                                ['河北', '河北'],
                                ['山西', '山西'],
                                ['内蒙古', '内蒙古'],
                                ['辽宁', '辽宁'],
                                ['吉林', '吉林'],
                                ['黑龙江', '黑龙江'],
                                ['上海', '上海'],
                                ['江苏', '江苏'],
                                ['浙江', '浙江'],
                                ['安徽', '安徽'],
                                ['福建', '福建'],
                                ['江西', '江西'],
                                ['山东', '山东'],
                                ['河南', '河南'],
                                ['湖北', '湖北'],
                                ['湖南', '湖南'],
                                ['广东', '广东'],
                                ['广西', '广西'],
                                ['海南', '海南'],
                                ['重庆', '重庆'],
                                ['四川', '四川'],
                                ['贵州', '贵州'],
                                ['云南', '云南'],
                                ['西藏', '西藏'],
                                ['陕西', '陕西'],
                                ['甘肃', '甘肃'],
                                ['青海', '青海'],
                                ['宁夏', '宁夏'],
                                ['新疆', '新疆'],
                                ['香港', '香港'],
                                ['澳门', '澳门'],
                                ['台湾', '台湾']
                            ]
                        },{
                            fieldLabel: '其他地区',
                            name: 'isOthers',
                            xtype: 'checkboxfield',
                            boxLabelAlign: 'before',
                            inputValue: true,
                            uncheckedValue: false,
                            disabled: true
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