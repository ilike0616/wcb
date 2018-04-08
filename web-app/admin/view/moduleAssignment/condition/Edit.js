Ext.define("admin.view.moduleAssignment.condition.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.conditionEdit',
    modal: true,
    plain:true,
    layout: 'fit',
    title: '修改条件',
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
                        fieldLabel: '名称',
                        name: 'name',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        fieldLabel: '说明',
                        name: 'remark'
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save',autoUpdate:true,target:'conditionList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})