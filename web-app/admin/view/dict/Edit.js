Ext.define("admin.view.dict.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.dictEdit',
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
                        labelWidth: 120
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
                        xtype: 'combo',
                        fieldLabel: '所属用户',
                        labelAlign: 'right',
                        name: 'user',
                        emptyText: '请选择...',
                        autoSelect : true,
                        forceSelection:true,
                        displayField : 'name',
                        valueField : 'id',
                        queryMode: 'local',
                        store:Ext.create('admin.store.UserStore',{pageSize:9999999})
                    },{
                        xtype: 'radiogroup',
                        fieldLabel: '是否系统数据',
                        columns: 2,
                        margin:'0 5 0 5',
                        items: [
                            { boxLabel: '是', name: 'issys', inputValue: 'true' },
                            { boxLabel: '否', name: 'issys', inputValue: 'false', checked: true},
                        ]
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'dictList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})