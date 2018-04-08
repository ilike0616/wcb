Ext.define("admin.view.moduleStore.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.moduleStoreEdit',
    modal: true,
    plain:true,
    layout: 'anchor',
    title: '修改模块Store',
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
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save',autoUpdate:true,target:'moduleStoreList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})