Ext.define("user.view.taskAssigned.Reply",{
    extend: 'public.BaseWin',
    alias: 'widget.taskAssignedReply',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '回复',
    initComponent: function() {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'form',
                    moduleId: 'task_assigned',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'column'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 330
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textarea'
                    },
                    items: [{
                            fieldLabel : '回复：',
                            name : 'content',
                            allowBlank : false,
                            beforeLabelTextTpl: required
                        },{
                            xtype:'hiddenfield',
                            name:'id'
                        }
                    ],
                    buttons: [{
                        text:'保存',itemId:'reply',iconCls:'table_save'
                    }]
                }
            ]
        });
        this.callParent(arguments);
    }
})