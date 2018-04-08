Ext.define("admin.view.dict.item.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.dictItemAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    title: '添加字典明细',
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
                        name: 'text',
                        allowBlank : false
                    },{
                        xtype:'hiddenfield',
                        name:'dict'
                    },{
                        xtype:'hiddenfield',
                        name:'user'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'dictItemList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})