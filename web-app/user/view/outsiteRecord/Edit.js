Ext.define("user.view.onsiteObject.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.onsiteObjectEdit',
    requires: [
        'public.BaseComboBoxTree'
    ],
    title: '修改现场对象',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'table',
                        columns: 2
                    },
                    fieldDefaults: {
                        labelWidth: 80,
                        width : 330,
                        height: 25
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel : '对象名称',
                        name : 'name',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        fieldLabel : '客户',
                        name : 'customer',
                        itemid : 'customerId',
                        xtype : 'combobox',
                        autoSelect:true,
                        forceSelection:true,
                        typeAhead : true,
                        minChars : 2,
                        emptyText:'-- 请选择 --',
                        displayField: 'name',
                        valueField: 'id',
                        store: 'CustomerStore'
                    },{
                        fieldLabel : '联系人',
                        name : 'contact',
                        itemid : 'contactId',
                        xtype : 'combobox',
                        autoSelect:true,
                        forceSelection:true,
                        typeAhead : true,
                        queryMode : 'local',
                        emptyText:'-- 请选择 --',
                        displayField: 'name',
                        valueField: 'id',
                        store: 'ContactStore'
                    },{
                        fieldLabel : '地址',
                        name : 'address'
                    },{
                        fieldLabel : '电话',
                        name : 'phone'
                    },{
                        fieldLabel : '手机',
                        name : 'mobile'
                    },{
                        xtype : 'fieldset',
                        title : '附件',
                        autoHeight : true,
                        items:[{
                            xtype: 'filefield',
                            name: 'tempFiles',
                            fieldLabel: '附件',
                            anchor: '100%',
                            buttonText: '上传'
                        },{
                            xtype: 'displayfield',
                            fieldLabel: '原文件',
                            name : 'files',
                            renderer : function(value){
                                if(value == "") return "";
                                return "<a href='uploadFile/"+value+"'target='_blank'>"+value+"</a>"
                            }
                        }]
                    },{
                        xtype:'hiddenfield',
                        name:'employee'
                    },{
                        xtype:'hiddenfield',
                        name:'user'
                    },{
                        xtype:'hiddenfield',
                        name:'id'
                    }
                ],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})