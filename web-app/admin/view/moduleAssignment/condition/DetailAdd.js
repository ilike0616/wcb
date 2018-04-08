Ext.define("admin.view.moduleAssignment.condition.DetailAdd",{
    extend: 'Ext.window.Window',
    alias: 'widget.conditionDetailAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    width:550,
    title: '添加条件明细',
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
                        labelAlign: 'right',
                        labelWidth: 100
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        beforeLabelTextTpl: required,
                        xtype: 'textfield',
                        width: 400
                    },
                    items: [{
                        fieldLabel: '字段名称',
                        name: 'fieldName',
                        displayField: 'fieldText',
                        valueField: 'fieldName',
                        xtype: 'combo',
                        autoSelect: true,
                        forceSelection: true,
                        emptyText: '-- 请选择 --',
                        store:''
                    },{
                        fieldLabel: '操作符',
                        name: 'operator',
                        xtype: 'combo',
                        autoSelect: true,
                        forceSelection: true,
                        value:'==',
                        store:[
                            ['==', '等于'],
                            ['!=', '不等于'],
                            ['>', '大于'],
                            ['>=', '大于等于'],
                            ['<', '小于'],
                            ['<', '小于等于'],
                            ['in', '包含'],
                            ['notin', '不包含']
                        ]
                    },{
                        fieldLabel: '值',
                        name: 'value',
                        itemId : 'value',
                        allowBlank : false
                    },{
                        xtype:'checkbox',
                        fieldLabel : 'me？',
                        name : 'valueMe',
                        itemId : 'valueMe',
                        boxLabelAlign: 'before',
                        inputValue: 'me',
                        hidden:true
                    },{
                        fieldLabel: '值标识',
                        name: 'valueFlag',
                        xtype: 'combo',
                        autoSelect: true,
                        forceSelection: true,
                        value:'field',
                        store:[
                            ['field', '字段值'],
                            ['literal', '字面值']
                        ]
                    },{
                        xtype:'hiddenfield',
                        name:'userOptCondition',
                        value:me.userOptCondition
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'conditionDetailList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})