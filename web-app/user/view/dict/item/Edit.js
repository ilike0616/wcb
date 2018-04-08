Ext.define("user.view.dict.item.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.dictItemEdit',
    requires: [
        'public.BaseComboBoxTree'
    ],
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '修改字典明细',
    initComponent: function() {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'column'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 330
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel : '名称',
                        name : 'text',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save'
                    }]
                }
            ]
        });

        this.callParent(arguments);
    }
})