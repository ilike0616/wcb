/**
 * Created by shqv on 2014-9-12.
 */
Ext.define("admin.view.userField.ExtBooleanField",{
    extend: 'Ext.window.Window',
    alias: 'widget.extBooleanField',
    modal: true,
    title: '布尔型扩展字段',
    layout : 'anchor',
    width: 400,
    height: 300,
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
                        value:'布尔型',
                        xtype: 'displayfield'
                    },{
                        fieldLabel: '是否必填',
                        name : 'bitian',
                        inputValue:true,
                        uncheckedValue:false,
                        xtype : 'checkboxfield'
                    },{
                        fieldLabel:'默认值',
                        name:'defValue',
                        inputValue:true,
                        uncheckedValue:false,
                        xtype : 'checkboxfield'
                    },{
                        fieldLabel: '显示方式',
                        name : 'userFieldPageType',
                        xtype : 'combo',
                        autoSelect:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        allowBlank : false,
                        store:[
                            ['checkbox','勾选']
                        ]
                    },{
                        fieldLabel: '字段说明',
                        name : 'note',
                        xtype : 'textarea',
                        grow:true
                    },{
                        name: 'user',
                        value:me.user,
                        xtype: 'hiddenfield'
                    },{
                        name: 'module',
                        value: me.module,
                        xtype: 'hiddenfield'
                    },{
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