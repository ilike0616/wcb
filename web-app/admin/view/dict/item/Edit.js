Ext.define("admin.view.dict.item.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.dictItemEdit',
    modal: true,
    plain:true,
    layout: 'fit',
    width:450,
    title: '修改字典明细',
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
                        name: 'text',
                        allowBlank : false
                    },{
                        xtype:'hiddenfield',
                        name:'dict'
                    },{
                        xtype:'hiddenfield',
                        name:'user'
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'dictItemList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})