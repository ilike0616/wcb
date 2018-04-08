/**
 * Created by GuoZhen on 2015-5-22.
 */
Ext.define("admin.view.userField.ExtDateField",{
    extend: 'Ext.window.Window',
    alias: 'widget.extDateField',
    modal: true,
    title: '日期型扩展字段',
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
                        value:'日期型',
                        xtype : 'displayfield'
                    },{
                        fieldLabel: '是否必填',
                        name : 'bitian',
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
                        value:'datetimefield',
                        store:[
                            ['datefield','日期'],
                            ['datetimefield','日期+时间'],
                            ['timefield','时间']
                        ]
                    },{
                        fieldLabel: '日期格式',
                        name : 'dateFormat',
                        queryMode: 'local',
                        xtype : 'textfield',
                        note:'日期格式:Y:年  m:月 d:日  H:时  i:分  s:秒 <br>默认格式为：Y-m-d H:i:s',
                        emptyText:'默认格式为：Y-m-d H:i:s'
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
            buttons: [{text:'保存',itemId:'extXXXFieldSave',iconCls:'table_save'}]
        })
        me.callParent(arguments);
    }
})