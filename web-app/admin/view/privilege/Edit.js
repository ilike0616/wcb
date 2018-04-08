Ext.define("admin.view.privilege.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.privilegeEdit',
    modal: true,
    width: 550,
    layout: 'anchor',
    title: '修改权限',
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
                        labelWidth: 110,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        beforeLabelTextTpl: required
                    },
                    items: [
                        {
                            fieldLabel: '权限名称',
                            name: 'name',
                            allowBlank : false
                        },{
                            xtype:'hiddenfield',
                            name:'id'
                        },{
                            xtype:'hiddenfield',
                            name:'user'
                        }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'privilegeList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})