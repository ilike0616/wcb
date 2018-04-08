Ext.define("admin.view.portal.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.portalEdit',
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '修改Portal',
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
                        labelWidth: 150,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel : '标题',
                        name : 'title',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        fieldLabel : '类型',
                        name : 'type',
                        xtype:'combo',
                        forceSelection:true,
                        store:[
                            [1,'动态'],
                            [2,'图表']
                        ]
                    },{
                        fieldLabel : '高度',
                        name : 'height',
                        xtype: 'numberfield',
                        value: 330,
                        minValue: 50
                    },{
                        fieldLabel: 'xtype',
                        name: 'xtype'
                    },{
                        fieldLabel: '穿透查询viewId',
                        name: 'viewId'
                    },{
                        fieldLabel: '穿透查询viewStore',
                        name: 'viewStore'
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'portalList'
                    }]
                }
            ]
        });

        me.callParent(arguments);
    }
})