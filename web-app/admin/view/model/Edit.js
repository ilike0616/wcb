Ext.define("admin.view.model.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.modelEdit',
    modal: true,
    plain:true,
    layout: 'fit',
    width:450,
    title: '修改模块',
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
                        beforeLabelTextTpl: required,
                        width:400
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
                    },
                    {
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'modelList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})