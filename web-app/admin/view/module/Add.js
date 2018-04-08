Ext.define("admin.view.module.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.moduleAdd',
    modal: true,
    plain:true,
    layout: 'fit',
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
                        fieldLabel: 'ctrl',
                        name: 'ctrl',
                        allowBlank : false,
                        beforeLabelTextTpl:''
                    },{
                        fieldLabel: '视图',
                        name: 'vw',
                        allowBlank : false,
                        beforeLabelTextTpl:''
                    },{
                        fieldLabel: '图标',
                        name: 'iconCls',
                        beforeLabelTextTpl: ''
                    },{
                        xtype: 'checkboxfield',
                        name:'isMenu',
                        fieldLabel:'是否生成菜单',
                        boxLabelAlign:'before',
                        uncheckedValue : false,
                        inputValue:true
                    },{
                        xtype:'hiddenfield',
                        id : 'moduleAddParentModule',
                        name:'parentModule'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'moduleList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})