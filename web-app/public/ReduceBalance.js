Ext.define("public.ReduceBalance",{
    extend: 'Ext.window.Window',
    alias: 'widget.reduceBalance',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '退费',
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
                            fieldLabel : '退费名称',
                            name : 'name',
                            value : me.name,
                            xtype:'displayfield'
                        },{
                            fieldLabel : '退费账号',
                            value : me.accountId,
                            xtype:'displayfield'
                        },{
                            fieldLabel: '账户余额',
                            xtype: 'displayfield',
                            itemId:'availableBalance',
                            value:me.availableBalance
                        },{
                            xtype:'numberfield',
                            fieldLabel : '退费金额',
                            value:0,
                            maxValue:0,
                            name:'balance'
                        },{
                            xtype:'numberfield',
                            fieldLabel : '实际退费金额',
                            value:0,
                            maxValue:0,
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
        var availableBalance = form.down('displayfield#availableBalance').getValue();
        if(availableBalance <= 0){
            Ext.Msg.alert('提示','当前账户余额为0，禁止退费！');
            return;
        }
        return;
        if (!form.getForm().isValid()) return;
        form.submit({
            waitMsg: '正在提交数据',
            waitTitle: '提示',
            url:'addBalance/reduceBalance',
            method: 'POST',
            submitEmptyText : false,
            success: function(form, action) {
                grid.getStore().load();
                grid.getSelectionModel().deselectAll();
                Ext.example.msg('提示', '退费成功');
                me.close();
            },
            failure:function(form,action){
                var result = Util.Util.decodeJSON(action.response.responseText);
                Ext.example.msg('提示', result.msg);
            }
        });
    }
})