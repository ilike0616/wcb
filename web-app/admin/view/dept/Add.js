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
                            xtype : 'hiddenfield',
                            name : 'parentDept'
                        },{
                            xtype: 'hiddenfield',
                            name: 'user'
                        }
                    ],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'deptList'
                    }]
                }
            ]
        });
        this.callParent(arguments);
    }
})