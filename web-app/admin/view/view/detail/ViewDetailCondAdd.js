/**
 * Created by shqv on 2014-9-10.
 */
Ext.define("admin.view.view.detail.ViewDetailCondAdd",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailCondAdd',
    modal: true,
    plain:true,
    layout: 'fit',
    width:350,
    title: '添加条件',
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
                        labelAlign: 'right',
                        labelWidth: 100
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        beforeLabelTextTpl: required
                    },
                    items: [{
                        xtype:'combo',
                        name:'fieldName',
                        fieldLabel:'字段名',
                        allowBlank : false,
                        displayField : 'fieldLabel',
                        valueField : 'name',
                        store: Ext.create('Ext.data.Store',{
                            proxy: {
                                type: 'ajax',
                                url : 'viewDetail/viewDetailCondField?user='+me.user+"&module="+me.moduleId,
                                reader: {
                                    type: 'json',
                                    root: 'fields'
                                }
                            },
                            fields:['fieldLabel', 'name','pageType','store','dbType'],
                            autoLoad:true,
                            listeners:{
                                scope:me,
                                load:function(o, records, successful, eOpts){
                                    if(successful == true){
                                        if(me.record){
                                            var record = me.record;
                                            me.down('form').loadRecord(record);
                                            me.down('form').down('hiddenfield[name=originFieldName]').setValue(record.get('fieldName'));
                                            me.down('form').down('textfield[name=searchFieldTextValue]').setValue(record.get('userFieldValue'));
                                            me.down('form').down('numberfield[name=searchFieldNumberValue]').setValue(record.get('userFieldValue'));
                                            if(record.get('dbType') == 'java.lang.Integer'){
                                                me.down('form').down('combo[name=searchFieldComboValue]').setValue(parseInt(record.get('userFieldValue')));
                                            }else{
                                                me.down('form').down('combo[name=searchFieldComboValue]').setValue(record.get('userFieldValue'));
                                            }
                                            me.down('form').down('datefield[name=searchFieldStartDateValue]').setValue(record.get('startDate'));
                                            me.down('form').down('datefield[name=searchFieldEndDateValue]').setValue(record.get('endDate'));
                                        }
                                    }
                                }
                            }
                        })
                    },{
                        fieldLabel: '操作符',
                        name: 'operator',
                        xtype: 'combo',
                        allowBlank: false,
                        autoSelect:true,
                        editable:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        value:'ilike',
                        store:[
                            ['ilike','='],
                            ['eq','=='],
                            ['ne','!='],
                            ['gt','>'],
                            ['ge','>='],
                            ['lt','<'],
                            ['le','<=']
                        ]
                    },
                        {xtype:'textfield',name:'searchFieldTextValue',fieldLabel:'值'},
                        {xtype:'numberfield',name:'searchFieldNumberValue',fieldLabel:'值',invalidText:'只允许输入数值型数据',hidden:true},
                        {xtype:'combo',name:'searchFieldComboValue',fieldLabel:'值',store:[[1,'aa']],autoSelect:true,forceSelection:true,typeAhead : true,emptyText:'-- 请选择 --',hidden:true},
                        {xtype:'datefield',name:'searchFieldStartDateValue',fieldLabel:'开始时间',hidden:true,format:'Y-m-d',submitFormat:'y-m-d'},
                        {xtype:'datefield',name:'searchFieldEndDateValue',fieldLabel:'结束时间',hidden:true,format:'Y-m-d',submitFormat:'y-m-d'},
                        {
                            xtype:'hiddenfield',
                            name:'originFieldName'
                        }
                    ],
                    buttons: [{
                        text:'添加',itemId:'viewDetailCondSave',iconCls:'table_save'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})