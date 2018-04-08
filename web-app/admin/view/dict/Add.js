Ext.define("admin.view.dict.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.dictAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    title: '添加数据字典',
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
                        width:400 ,
                        beforeLabelTextTpl: required
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
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'dictList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})