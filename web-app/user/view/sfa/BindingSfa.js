/**
 * Created by like on 2015/9/15.
 */
Ext.define("user.view.sfa.BindingSfa", {
    extend: 'public.BaseWin',
    alias: 'widget.sfaBindingSfa',
    requires: [
        'public.BaseForm'
    ],
    width: 550,
    height: 350,
    initComponent: function () {
        var me = this,
            moduleId = me.moduleId,
            linkId = me.linkId,
            linkName = me.linkName;
        Ext.applyIf(me, {
            items: [
                {
                    region: 'north',
                    border: 0,
                    items: [{
                        region: 'north',
                        border: 0,
                        items: [{
                            xtype: 'fieldset',
                            title: '操作对象',
                            layout: 'column',
                            columns: 2,
                            margin: '0 5 0 5',
                            defaults: {
                                labelWidth: 70,
                                padding: '3 3 3 3'
                            },
                            items: [
                                {
                                    xtype: 'hidden',
                                    name: 'moduleId'
                                }, {
                                    xtype: 'hidden',
                                    name: 'linkId'
                                }, {
                                    xtype: 'label',
                                    text: linkName
                                }]
                        }]
                    }, {
                        region: 'center',
                        border: 0,
                        items: [{
                            xtype: 'baseFieldSet',
                            checkboxToggle: true,
                            title: '选择SFA方案',
                            margin: '0 5 0 5',
                            overflowY: 'auto',
                            height: 230,
                            items: [{
                                xtype: 'checkboxgroup',
                                name: 'chooseSfa',
                                columns: 4,
                                defaults: {
                                    padding: '5px 0px 5px 0px'
                                },
                                items: []
                            }]
                        }]
                    }
                    ],
                    buttons: [
                        {text: '保存', itemId: 'save', iconCls: 'table_save', autoInsert: false, target: ''},
                        {text: '关闭', itemId: 'close', iconCls: 'table_save', listeners: {scope: me, click: me.closeWin}}
                    ]
                }
            ]
        });
        me.callParent(arguments);
    },
    closeWin: function (o, e, eOpts) {   // 关闭
        var me = this;
        me.close();
    }
});