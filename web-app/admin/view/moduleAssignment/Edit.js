Ext.define("admin.view.module.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.moduleEdit',
    modal: true,
    plain:true,
//    layout: 'fit',
    /*,*/layout: {
        type: 'absolute'
    },
    title: '修改模块',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 0',
                    fieldDefaults: {
                        labelAlign: 'right',
                        labelWidth: 100
                    },
                    defaults: {
                        width: 300,
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        beforeLabelTextTpl: required
                    },
                    items: [{
                        fieldLabel: '模块Id',
                        name: 'moduleId',
                        allowBlank : false
                    },{
                        fieldLabel: '模块名称',
                        name: 'moduleName',
                        allowBlank : false
                    },
                    {
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})