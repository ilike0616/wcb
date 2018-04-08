Ext.define("admin.view.user.SwitchVersion", {
    extend: 'Ext.window.Window',
    alias: 'widget.userSwitchVersion',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '版本切换',
    items: [
        {
            xtype: 'form',
            layout: 'anchor',
            defaults: {
                msgTarget: 'side',
                xtype: 'textfield',
                padding: '3 5 3 5',
                labelWidth : 120,
                beforeLabelTextTpl: required
            },
            items: [
                {
                    fieldLabel: '版本',
                    name: 'edition',
                    displayField: 'name',
                    valueField: 'id',
                    xtype: 'combo',
                    autoSelect: true,
                    forceSelection: true,
                    emptyText: '-- 请选择 --',
                    allowBlank: false,
                    store:Ext.create('admin.store.EditionStore', {
                        fields: ['id', 'name','monthlyFee'],
                        proxy: {
                            type: 'ajax',
                            api: {
                                read: 'edition/list?tplUser=1'
                            },
                            reader: {
                                type: 'json',
                                root: 'data',
                                successProperty: 'success',
                                totalProperty: 'total'
                            },
                            simpleSortMode: true
                        },
                        autoLoad: false
                    })
                },{
                    xtype:'hiddenfield',
                    name:'id'
                },{
                    xtype:'hiddenfield',
                    name:'switchVersion',
                    value:'switchVersion'
                }
            ]
        }
    ],
    buttons: [
        {
            text: '保存', itemId: 'save', iconCls: 'table_save', autoUpdate: true, target: 'userList'
        }
    ]
})