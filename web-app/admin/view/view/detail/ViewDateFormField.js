/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewDateFormField",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewDateFormField',
    modal: true,
    title: '日期字段',
    layout : 'border',
    items: [{
        region:'west',
        xtype: 'form',
        width:230,
        overflowY:'auto',
        buttonAlign:'center',
        bodyStyle: 'padding:5px 5px 0',
        fieldDefaults: {
            align : 'center',
            labelAlign: 'right',
            width:200,
            labelWidth: 70
        },
        defaults: {
            msgTarget: 'side',
            xtype: 'textfield'
        },
        items: [
            {
                fieldLabel: '字段标题',
                name: 'userField.text',
                allowBlank : false
            },{
                fieldLabel: '显示方式',
                name : 'pageType',
                xtype : 'combo',
                autoSelect:true,
                forceSelection:true,
                emptyText:'-- 请选择 --',
                allowBlank : false,
                fieldType : 'Date',
                viewType:'Form',
                store:[
                    ['datefield','日期'],
                    ['datetimefield','日期+时间'],
                    ['timefield','时间']
                ]
            },{
                fieldLabel: '日期格式',
                name : 'userField.dateFormat',
                queryMode: 'local',
                xtype : 'combo',
                autoSelect:true,
                editable:true,
                forceSelection:true,
                emptyText:'-- 请选择 --',
                allowBlank : true,
                store:[['','']]
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否必填',
                name:'userField.bitian',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否只读',
                name:'readOnly',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                note:'该字段值会提交到后台'
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否禁用',
                name:'disabled',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                note:'值为true,则该字段值不会提交到后台'
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否提交该值',
                name:'isSubmitValue',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                hidden:true,
                note:'值为true,则该字段值不会提交到后台'
            },{
                fieldLabel: '字段说明',
                name : 'remark'
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否当前时间',
                name:'isCurrentDate',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                note:'值为true,则默认为当前时间',
                listeners:{
                    change:function( o, newValue, oldValue, eOpts ){
                        var form = o.up('form');
                        var defValue = form.down('datetimefield[name=defValue]');
                        if(defValue){
                            if(newValue == true){
                                defValue.setValue(null);
                                defValue.setDisabled(true);
                            }else{
                                defValue.setDisabled(false);
                            }
                        }
                    }
                }
            },{
                fieldLabel:'默认值',
                name:'defValue',
                xtype : 'datetimefield',
                format : 'Y-m-d H:i:s',
                submitFormat : 'Y-m-d H:i:s',
                listeners:{
                    change:function(o, newValue, oldValue, eOpts ){
                        if(!o.validateValue(newValue)){
                            o.setValue(new Date());
                        }
                    }
                }
            },{
                fieldLabel: '初始字段',
                name : 'initName',
                xtype : 'textfield'
            },{
                name : 'id',
                xtype : 'hidden'
            },{
                name : 'user.id',
                xtype : 'hiddenfield'
            }
        ],
        buttons: [{
            text:'保存',iconCls:'table_save',itemId : 'save',autoUpdate:true,target:'viewDetailList grid'
        }]
    },{
        region: 'center',
        xtype : 'viewDetailPropertyList',
        name : 'propertyGridView'
    }]
})