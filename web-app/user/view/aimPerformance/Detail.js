Ext.define("user.view.aimPerformance.Detail",{
    extend: 'Ext.window.Window',
    alias: 'widget.aimPerformanceDetail',
    modal: true,
    layout: 'anchor',
    title: '目标明细',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    initComponent: function() {
        var me = this;
        var store = Ext.create('user.store.AimPerformanceDetailStore');
        Ext.apply(store.proxy.extraParams,{owner:me.owner,aimYear:me.aimYear,aimType:me.aimType});
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
                        width:1300,
                        autoScroll: true,
                        items: [{
                            xtype: 'gridpanel',
                            store: store,
                            columns: me.generateColumns(me.aimYear)
                        }]
                        }
                    ]
                }
            ]
        });
        me.callParent(arguments);
    },
    generateColumns:function(aimYear){
        var columns = [{
            text: '类别',
            width: 230,
            locked:true,
            dataIndex: 'aimKindName'
        }];
        var currentDate = new Date();
        if(aimYear != null && aimYear != "") currentDate.setFullYear(aimYear);
        var num = 0;
        var yearMonth = "";
        for(var i=0;i<13;i++){ // 此处多循环一次，多循环的一次为统计
            if(i == 12){
                num = 'sum';
                yearMonth = '总计';
            }else{
                num = i + 1;
                currentDate.setMonth(i);
                yearMonth = Ext.Date.format(currentDate, 'Y-m');
            }
            columns.push({
                text: yearMonth,
                menuDisabled:true,
                columns: [{
                    text: '目标',
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aim_'+num
                }, {
                    text: '完成',
                    align:'center',
                    width:70,
                    menuDisabled:true,
                    dataIndex: 'aimComplete_'+num
                }, {
                    text: '完成%',
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aimCompletePercent_'+num,
                    renderer: function(value){
                        if(value >= 100){
                            return '<span style="color:green">' + value + '%</span>';
                        }else{
                            return '<span style="color:red">' + value + '%</span>';
                        }
                    }
                }]
            })
        }
        return columns;
    }
})