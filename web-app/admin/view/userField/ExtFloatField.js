/**
 * Created by GuoZhen on 2015-5-22.
 */
Ext.define("admin.view.userField.ExtFloatField",{
    extend: 'Ext.window.Window',
    alias: 'widget.extFloatField',
    modal: true,
    title: '浮点型扩展字段',
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
                    width:300,
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
                    },{
                        fieldLabel: '数据类型',
                        value:'浮点型',
                        xtype : 'displayfield'
                    },{
                        fieldLabel: '是否必填',
                        name : 'bitian',
                        inputValue:true,
                        uncheckedValue:false,
                        xtype : 'checkboxfield'
                    },{
                        fieldLabel: '最小值',
                        name : 'min',
                        xtype : 'numberfield'
                    },{
                        fieldLabel: '最大值',
                        name : 'max',
                        xtype : 'numberfield'
                    },{
                        fieldLabel: '显示方式',
                        name : 'userFieldPageType',
                        xtype : 'combo',
                        autoSelect:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        allowBlank : false,
                        store:[
                            ['numberfield','文本']
                        ]
                    },{
                        fieldLabel: '小数位',
                        name : 'scale',
                        xtype : 'numberfield',
                        maxValue:5,
                        minValue:0
                    },{
                        fieldLabel: '字段说明',
                        name : 'note',
                        xtype : 'textarea',
                        grow:true
                    },{
                        fieldLabel: '默认值',
                        name : 'defValue',
                        xtype : 'textfield'
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
            buttons: [{text:'保存',itemId:'extXXXFieldSave',iconCls:'table_save'}]
        })
        me.callParent(arguments);
    }
})