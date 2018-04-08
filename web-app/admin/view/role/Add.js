Ext.define("admin.view.role.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.roleAdd',
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '添加角色',
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
                            emptyText:'-- 请选择 --',
                            store:'PrivilegeStore'
                        },{
                            xtype:'hiddenfield',
                            name:'user',
                            id:'roleAddUser'
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