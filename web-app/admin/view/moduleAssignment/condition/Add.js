Ext.define("admin.view.moduleAssignment.condition.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.conditionAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    width:350,
    title: '添加条件',
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
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel: '名称',
                        name: 'name',
                        beforeLabelTextTpl: required,
                        allowBlank : false
                    },{
                        fieldLabel: '说明',
                        name: 'remark'
                    },{
                        xtype:'hiddenfield',
                        name:'userOperation',
                        value:me.userOperation
                    },{
                        xtype:'hiddenfield',
                        name:'user',
                        value:me.user
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'conditionList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})