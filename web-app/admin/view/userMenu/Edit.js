Ext.define("admin.view.dept.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.deptEdit',
    requires: [
        'public.BaseComboBoxTree'
    ],
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '修改部门',
    initComponent: function() {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 0',
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
                        xtype: 'baseComboBoxTree',
                        name : 'parentDept',
                        fieldLabel: '上级部门',
                        displayField: 'text',
                        value: '',
                        rootVisible: false,
                        minPickerHeight: 200,
                        store : 'DeptStoreForEdit'
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
                        typeAhead : true,
                        emptyText:'-- 请选择 --',
                        store:'RoleStore'
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

        this.callParent(arguments);
    }
})