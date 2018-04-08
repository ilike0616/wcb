Ext.define("admin.view.dept.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.deptAdd',
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '添加部门',
    initComponent: function() {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 150,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                            fieldLabel : '部门名称',
                            name : 'name',
                            allowBlank : false,
                            beforeLabelTextTpl: required
                        },{
                            fieldLabel: '所属用户',
                            name : 'user',
                            xtype : 'combobox',
                            id : 'deptAddUser',
                            autoSelect : true,
                            forceSelection:true,
                            displayField : 'name',
                            valueField : 'id',
                            emptyText : '-- 请选择 --',
                            beforeLabelTextTpl: required,
                            store : 'UserStore'
                        },{
                            fieldLabel: '选择角色',
                            name: 'roles',
                            xtype: 'combo',
                            width:500,
                            autoSelect:true,
                            forceSelection:true,
                            multiSelect:true,
                            displayField: 'roleName',
                            valueField: 'id',
                            emptyText:'-- 请选择 --',
                            store:'RoleStore'
                        },{
                            xtype : 'hiddenfield',
                            itemId : 'deptAddParentDept',
                            name : 'parentDept'
                        }
                    ],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save'
                    }]
                }
            ]
        });
        this.callParent(arguments);
    }
})