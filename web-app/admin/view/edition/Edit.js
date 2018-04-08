/**
 * Created by like on 2015-04-29.
 */
Ext.define("admin.view.edition.Edit", {
    extend: 'Ext.window.Window',
    alias: 'widget.editionEdit',
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '修改版本',
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
                            fieldLabel: '版本名称',
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            fieldLabel: '种类',
                            name: 'kind',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            emptyText: '-- 请选择 --',
                            allowBlank: false,
                            store: [
                                ['1', '销售通'],
                                ['2', '门店通'],
                                ['3', '服务通']
                            ]
                        },
                        {
                            fieldLabel: '版本',
                            name: 'ver',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            emptyText: '-- 请选择 --',
                            allowBlank: false,
                            store: [
                                ['1', '免费版'],
                                ['2', '标准版'],
                                ['3', '高级版']
                            ]
                        },
                        {
                            fieldLabel: '费用/人▪月',
                            name: 'monthlyFee',
                            allowBlank: false
                        },
                        {
                            fieldLabel: '企业模板',
                            name: 'templateUser',
                            allowBlank : false,
                            xtype : 'combobox',
                            autoSelect : true,
                            forceSelection:true,
                            displayField : 'name',
                            valueField : 'id',
                            emptyText : '-- 请选择 --',
                            beforeLabelTextTpl: required,
                            store : Ext.create('admin.store.UserStore',{pageSize:9999999,
                                listeners:{
                                    load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                        if(me.listDom) {
                                            var uv = me.listDom.selectedUser;
                                            if (uv != "" && uv != null) {
                                                me.down("combo[name=user]").setValue(uv);
                                            }
                                        }
                                    }
                                }
                            }),
                            minChars: 1,
                            queryParam : 'searchValue',
                            matchFieldWidth: true,
                            listConfig: {
                                loadingText: '正在查找...',
                                emptyText: '没有找到匹配的数据'
                            }
                        },
                        {
                            fieldLabel: '版本说明',
                            name: 'remark',
                            xtype: 'textarea',
                            grow: true
                        },
                        {
                            xtype: 'hiddenfield',
                            name: 'id'
                        }
                    ],
                    buttons: [
                        {
                            text: '保存', itemId: 'save', iconCls: 'table_save', autoUpdate: true, target: 'editionList'
                        }
                    ]
                }
            ]
        });
        me.callParent(arguments);
    }
})