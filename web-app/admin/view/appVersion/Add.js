/**
 * Created by like on 2015-04-24.
 */
Ext.define("admin.view.appVersion.Add", {
    extend: 'Ext.window.Window',
    alias: 'widget.appVersionAdd',
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '添加App版本',
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width: 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [
                        {
                            fieldLabel: '应用平台',
                            name: 'platform',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            emptyText: '-- 请选择 --',
                            allowBlank: false,
                            store: [
                                ['Android', 'Android'],
                                ['IOS', 'IOS']
                            ]
                        },
                        {
                            fieldLabel: '版本分类',
                            name: 'edition',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            emptyText: '-- 请选择 --',
                            allowBlank: false,
                            store: [
                                ['1', '销售通'],
                                ['2', '门店通'],
                                ['3', '服务通'],
                                ['99', 'CRM'],
                                ['100', 'CRM100'],
                                ['101', 'CRM101'],
                                ['102', 'CRM102'],
                                ['103', 'CRM103'],
                                ['104', 'CRM104'],
                                ['105', 'CRM105'],
                                ['106', 'CRM106'],
                                ['107', 'CRM107'],
                                ['108', 'CRM108'],
                                ['109', 'CRM109']
                            ]
                        },
                        {
                            fieldLabel: '版本号',
                            name: 'appVersion',
                            allowBlank: false
                        },
                        {
                            xtype: 'filefield',
                            name: 'appPackage',
                            fieldLabel: '升级包',
                            msgTarget: 'side',
                            allowBlank: false,
                            anchor: '100%',
                            buttonText: '选择'
                        },
                        {
                            fieldLabel: '升级说明',
                            name: 'remark',
                            xtype: 'textarea',
                            emptyText:'请输入升级说明，描述一下升级的内容!',
                            grow: true
                        }
                    ],
                    buttons: [
                        {
                            text: '保存', itemId: 'save', iconCls: 'table_save', autoInsert: true, target: 'appVersionList'
                        }
                    ]
                }
            ]
        });
        me.callParent(arguments);
    }
})