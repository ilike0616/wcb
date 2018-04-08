Ext.define("user.view.saleAim.Set",{
    extend: 'Ext.window.Window',
    alias: 'widget.saleAimSet',
    modal: true,
    layout: 'anchor',
    title: '销售目标设置',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    initComponent: function() {
        var me = this;
        var store = Ext.create('user.store.SaleAimSetStore');
        Ext.apply(store.proxy.extraParams,{owner:me.owner,aimType:me.aimType,aimYear:me.aimYear});
        store.load();
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:10px 10px 10px',
                    layout: {
                        type: 'vbox'
                    },
                    items: [{
                        xtype: 'panel',
                        layout: 'hbox',
                        bodyStyle: 'padding:10px 10px 10px',
                        border:0,
                        defaults:{
                            labelWidth:70
                        },
                        items: [{
                            xtype: 'displayfield',
                            fieldLabel: '用户名称',
                            value:me.ownerName
                        },{
                            xtype: 'displayfield',
                            fieldLabel: '目标年份',
                            value:me.aimYear,
                            margin:'0 0 0 40'
                        }]
                    }, {
                        xtype: 'panel',
                        layout: 'anchor',
                        forceFit:true,
                        width:1300,
                        items: [{
                            xtype: 'gridpanel',
                            autoScroll: true,
                            forceFit:true,
                            store: store,
                            selType: 'cellmodel',
                            plugins: [
                                Ext.create('Ext.grid.plugin.CellEditing', {
                                    clicksToEdit: 1,
                                    listeners:{
                                        edit:function(editor, e, eOpts){
                                            var columns = 12;
                                            var record = e.record;
                                            var aimSum = 0;
                                            for(var i=1;i<=columns;i++){
                                                aimSum += record.get('aim_'+ i);
                                            }
                                            record.set('aim_sum',aimSum);
                                        }
                                    }
                                })
                            ],
                            columns: me.generateColumns(me.aimYear)
                        }]
                        }
                    ],
                    buttons: [{
                        text:'保存',
                        itemId:'save',
                        iconCls:'table_save',
                        listeners:{
                            click:function(btn){
                                me.GridDoActionUtil.doSave(btn.up('form').down('gridpanel'),Object,{owner:me.owner,aimType:me.aimType,aimYear:me.aimYear});
                                me.parentGrid.store.load();
                                me.close();
                            }
                        }
                    }]
                }
            ]
        });
        me.callParent(arguments);
    },
    generateColumns:function(aimYear){
        var columns = [{
            text: '类别',
            dataIndex: 'aimKindName'
        }];
        var currentDate = new Date();
        if(aimYear != null && aimYear != "") currentDate.setFullYear(aimYear);
        var num = 0;
        var header = "";
        for(var i=0;i<13;i++){ // 此处多循环一次，多循环的一次为统计
            if(i == 12){
                num = 'sum';
                header = '总计';
            }else{
                num = i + 1;
                currentDate.setMonth(i);
                header = Ext.Date.format(currentDate, 'Y-m');
            }
            columns.push({
                header: header,
                dataIndex: 'aim_'+num,
                editor:'numberfield'
            })
        }
        return columns;
    }
})