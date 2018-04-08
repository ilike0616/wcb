Ext.define("admin.view.model.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.modelAdd',
    modal: true,
    plain:true,
    layout: 'fit',

    title: '添加模块',
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
                        xtype: 'textfield',
                        width:400 ,                        
                        beforeLabelTextTpl: required
                    },
                    items: [{
                        fieldLabel: '名称',
                        name: 'modelName',
                        allowBlank : false
                    },{
                        fieldLabel: 'class',
                        name: 'modelClass',

                        allowBlank : false
                    },{
                        fieldLabel: '备注说明',
                        name: 'remark',
                        xtype: 'textareafield',
                        beforeLabelTextTpl:''
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'modelList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})