Ext.define("admin.view.dept.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.deptEdit',
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
                        xtype:'hiddenfield',
                        name:'id'
                    },{
                        xtype:'hiddenfield',
                        name:'parentDept'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'deptList'
                    }]
                }
            ]
        });

        this.callParent(arguments);
    }
})