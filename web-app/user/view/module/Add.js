Ext.define("admin.view.module.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.moduleAdd',
    plain:true,
    width:350,
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
                        beforeLabelTextTpl: required
                    },
                    items: [{
                        fieldLabel: '模块Id',
                        name: 'moduleId',
                        regex:/^\w+$/,
                        regexText : '字母或者数字',
                        allowBlank : false
                    },{
                        fieldLabel: '模块名称',
                        name: 'moduleName',
                        allowBlank : false
                    },{
                        xtype:'hiddenfield',
                        id : 'moduleAddParentModule',
                        name:'parentModule'
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