Ext.define("user.view.attendanceModel.Map",{
    extend: 'Ext.window.Window',
    alias: 'widget.attendanceModelMap',
    modal: true,
    width: 700,
    height: 500,
    resizable : false,
    title: '请选择坐标点',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    border: 0,
                    padding:'3 0 0 0',
                    layout: {
                        type: 'vbox'
                    },
                    defaults: {
                        msgTarget: 'side'
                    },
                    items:[
                        {
                            xtype:'fieldcontainer',
                            itemId:'locationContainer',
                            layout:{
                                type:'hbox',
                                columns:3
                            },
                            fieldDefaults:{
                                labelWidth:150
                            },
                            items:[{
                                xtype:'textfield',
                                name : 'location',
                                emptyText:'请输入要定位的地点',
                                allowBlank : false,
                                beforeLabelTextTpl: required,
                                margin:'0 10 0 40',
                                width:295
                            },{
                                xtype:'button',
                                text:'搜索',
                                itemId:'mapLocationButton',
                                width:50
                            },{
                                xtype:'displayfield',
                                value:'（搜索后点击或直接点击地图获取位置信息）'
                            }]
                        },{
                            xtype:'panel',
                            itemsId:'mapPanel',
                            layout:'anchor',
                            height:390,
                            width:700,
                            margin:'5 0 0 0',
                            border:0
                        }
                    ],
                    buttons: [{
                        text:'关闭',itemId:'close',iconCls:'table_close',listeners:{scope:me,click:me.closeWin}
                    }]
                }
            ]
        });
        me.callParent(arguments);
    },
    closeWin : function(o){
        this.close();
    }
})