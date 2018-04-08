Ext.define("admin.view.operation.View",{
    extend: 'Ext.window.Window',
    alias: 'widget.operationView',
    modal: true,
    width: 450,
    layout: 'anchor',
    title: '查看操作',
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
                        labelWidth: 110
                    },
                    defaults: {
                        readOnly:true,
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        width: 350
                    },
                    items: [
                        {
                            fieldLabel: '操作ID',
                            name: 'operationId'
                        },{
                            fieldLabel: '操作名称',
                            name: 'text'
                        },{
                        	xtype:'combobox',
                            fieldLabel: '操作类型',
                            name: 'type',
                            store:[
                               ['add','新增操作'],
                               ['update','修改操作'],
                               ['view','查看操作'],
                               ['del','删除操作']
                            ],
                            value:'add'
                        },{
                        	xtype:'combo',
                        	fieldLabel:'客户端类型',
                        	name:'clientType',
                        	store:[
                               ['all','pc和mobile都可见'],
                               ['pc','pc端可见'],
                               ['mobile','mobile可见']
                            ]
                        },{
                        	xtype:'checkbox',
                            fieldLabel: '是否打开窗口',
                            name: 'showWin',
                            inputValue:true,
                            uncheckedValue:false
                        },{
                        	xtype:'checkbox',
                            fieldLabel: '默认启用',
                            name: 'defSelected',
                            inputValue:true,
                            uncheckedValue:false
                        },{
                        	xtype:'checkbox',
                            fieldLabel: '是否系统自动实现方法',
                            name: 'auto',
                            inputValue:true,
                            uncheckedValue:false
                        },{
                            xtype: 'checkbox',
                            fieldLabel: '窗口是否自定义',
                            name: 'isCustom',
                            inputValue: true,
                            uncheckedValue: false
                        },{
                        	xtype:'checkbox',
                            fieldLabel: '按钮选中时可用',
                            name: 'autodisabled',
                            inputValue:true,
                            uncheckedValue:false
                        },{
                            fieldLabel: '图标',
                            name: 'iconCls'
                        },{
                            fieldLabel: '视图',
                            name: 'vw'
                        },{
                            fieldLabel: '关联列表',
                            name: 'targetEl'
                        },{
                            xtype:'radiogroup',
                            fieldLabel: '操作记录条数',
                            columns: 3,
                            items: [
                                { boxLabel: '无', name: 'optRecords', inputValue: 'no', checked: true},
                                { boxLabel: '单条', name: 'optRecords', inputValue: 'one'},
                                { boxLabel: '多条', name: 'optRecords', inputValue: 'many'}
                            ]
                        },{
                            xtype:'hiddenfield',
                            name:'module'
                        }]
                }
            ]
        });
        me.callParent(arguments);
    }
})