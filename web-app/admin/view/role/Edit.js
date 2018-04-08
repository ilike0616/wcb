Ext.define("admin.view.role.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.roleEdit',
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '修改角色',
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
                        labelWidth: 110
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        beforeLabelTextTpl: required
                    },
                    items: [
                       {
                            fieldLabel: '角色名称',
                            name: 'roleName',
                            allowBlank : false
                        },{
                            fieldLabel: '选择权限',
                            name: 'privileges',
                            xtype: 'combo',
                            width:500,
                            autoSelect:true,
                            forceSelection:true,
                            multiSelect:true,
                            displayField: 'name',
                            valueField: 'id',
                            typeAhead : true,
                            store: 'PrivilegeStore'
                        },{
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