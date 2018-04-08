Ext.define("public.AddBalance",{
    extend: 'Ext.window.Window',
    alias: 'widget.addBalance',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '充值',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    border:0,
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 350
                    },
                    defaults: {
                        msgTarget: 'side',
                        margin:'5 5 0 10'
                    },
                    items: [{
                            fieldLabel : '充值名称',
                            name : 'name',
                            value : me.name,
                            xtype:'displayfield'
                        },{
                            fieldLabel : '充值账号',
                            value : me.accountId,
                            xtype:'displayfield'
                        },{
                            xtype:'numberfield',
                            fieldLabel : '充值金额',
                            value:0,
                            name:'balance'
                        },{
                            xtype:'numberfield',
                            fieldLabel : '实际金额',
                            value:0,
                            name:'realAddBalance'
                        },{
                            xtype:'textareafield',
                            fieldLabel: '备注',
                            name:'remark'
                        },{
                            xtype:'hiddenfield',
                            name:'objectId',
                            value:me.objectId
                        },{
                            xtype:'hiddenfield',
                            name:'kind',
                            value : me.kind
                        }
                    ],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',listeners:{scope:me,click:me.addBalance}
                    }]
                }
            ]
        });
        me.callParent(arguments);
    },
    addBalance:function(){
        var me = this;
        var grid = me.listDom;
        var form = me.down('form');
        if (!form.getForm().isValid()) return;
        form.submit({
            waitMsg: '正在提交数据',
            waitTitle: '提示',
            url:'addBalance/addBalance',
            method: 'POST',
            submitEmptyText : false,
            success: function(form, action) {
                grid.getStore().load();
                grid.getSelectionModel().deselectAll();
                Ext.example.msg('提示', '充值成功');
                me.close();
            },
            failure:function(form,action){
                var result = Util.Util.decodeJSON(action.response.responseText);
                Ext.example.msg('提示', result.msg);
            }
        });
    }
})