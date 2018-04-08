Ext.define("admin.view.moduleStore.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.moduleStoreAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    width:350,
    title: '添加模块Store',
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
                        fieldLabel: '标题',
                        name: 'name'
                    },{
                        fieldLabel: 'store名称',
                        name: 'store',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        xtype:'hiddenfield',
                        name:'module'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'moduleStoreList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})