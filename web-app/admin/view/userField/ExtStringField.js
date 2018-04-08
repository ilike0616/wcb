/**
 * Created by shqv on 2014-9-12.
 */
Ext.define("admin.view.userField.ExtStringField",{
    extend: 'Ext.window.Window',
    alias: 'widget.extStringField',
    modal: true,
    title: '字符型扩展字段',
    layout : 'anchor',
    width: 400,
    height: 350,
    extBtn:null,
    listDom:null,
    initComponent: function() {
        var me = this;
        extBtn = me.btn;
        listDom = me.listDom;
        Ext.applyIf(me, {
            items: [{
                xtype: 'form',
                padding: '5 5 5 20',
                border:0,
                fieldDefaults: {
                    labelAlign: 'right',
                    width: 300,
                    labelWidth: 70
                },
                defaults: {
                    msgTarget: 'side',
                    xtype: 'textfield'
                },
                items: [
                    {
                        fieldLabel: '字段标题',
                        name: 'text',
                        value:me.fieldName
                    },
                    {
                        fieldLabel: '数据类型',
                        value:'字符型',
                        xtype: 'displayfield'
                    }, {
                        fieldLabel: '是否必填',
                        name: 'bitian',
                        inputValue: true,
                        uncheckedValue: false,
                        xtype: 'checkboxfield'
                    }, {
                        fieldLabel: '是否唯一',
                        name: 'weiyi',
                        inputValue: true,
                        uncheckedValue: false,
                        xtype: 'checkboxfield'
                    }, {
                        fieldLabel: '模糊查询',
                        name: 'mohu',
                        inputValue: true,
                        uncheckedValue: false,
                        xtype: 'checkboxfield'
                    }, {
                        fieldLabel: '显示方式',
                        name: 'userFieldPageType',
                        xtype: 'combo',
                        autoSelect: true,
                        forceSelection: true,
                        emptyText: '-- 请选择 --',
                        allowBlank: false,
                        store: [
                            ['textfield', '文本框'],
                            ['textarea', '多行文本'],
                            ['multichoice', '多选框'],
                            ['htmleditor', '富文本框'],
                            ['baseSpecialTextfield', '弹出选择'],
                            ['hidden', '隐藏']
                        ]
                    }, {
                        fieldLabel: '字典',
                        name: 'dict',
                        hidden: true,
                        queryMode: 'local',
                        xtype: 'combo',
                        autoSelect: true,
                        editable: true,
                        forceSelection: true,
                        emptyText: '-- 请选择 --',
                        displayField: 'text',
                        valueField: 'id',
                        allowBlank: true,
                        store: Ext.create('admin.store.DataDictStore')
                    }, {
                        fieldLabel: '字段说明',
                        name: 'note',
                        xtype: 'textarea',
                        grow: true
                    }, {
                        fieldLabel: '默认值',
                        name: 'defValue',
                        xtype: 'textfield'
                    }, {
                        name: 'user',
                        value:me.user,
                        xtype: 'hiddenfield'
                    }, {
                        name: 'module',
                        value: me.module,
                        xtype: 'hiddenfield'
                    }, {
                        name: 'dbType',
                        value: me.dbType,
                        xtype: 'hiddenfield'
                    }
                ]
            }],
            buttons: [{text: '启用',itemId: 'extXXXFieldSave',iconCls: 'table_save'}]
        })
        me.callParent(arguments);
    }
})