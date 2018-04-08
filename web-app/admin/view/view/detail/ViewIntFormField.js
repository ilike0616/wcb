/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewIntFormField",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewIntFormField',
    modal: true,
    title: '整型字段',
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
                fieldType : 'Integer',
                viewType:'Form',
                store:[
                    ['numberfield','文本'],
                    ['combo','下拉'],
                    ['radio','单选'],
                    ['hidden','隐藏']
                ]
            },{
                xtype:'fieldset',
                itemId:'mostFieldFS',
                border:false,
                padding:'0 0 0 0',
                margin:'0 0 0 0',
                items :[{
                    xtype:'fieldset',
                    itemId:'minAndMaxFS',
                    border:false,
                    padding:'0 0 0 0',
                    margin:'0 0 0 0',
                    items :[{
                        xtype: 'numberfield',
                        fieldLabel:'最小值',
                        name:'userField.min',
                        minValue:0,
                        boxLabelAlign:'before'
                    },{
                        xtype: 'numberfield',
                        fieldLabel:'最大值',
                        name:'userField.max',
                        boxLabelAlign:'before'
                    }]
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
                    fieldLabel: '字典',
                    name : 'userField.dict',
                    itemId:'dict',
                    hidden:true,
                    queryMode: 'local',
                    xtype : 'combo',
                    autoSelect:true,
                    editable:true,
                    forceSelection:true,
                    emptyText:'-- 请选择 --',
                    displayField : 'text',
                    valueField : 'id',
                    allowBlank : true,
                    store:Ext.create('admin.store.DataDictStore')
                }]
            },{
                fieldLabel: '字段说明',
                name : 'remark',
                xtype : 'textfield'
            },{
                fieldLabel: '默认值',
                name : 'defValue',
                xtype : 'textfield'
            },{
                xtype:'fieldset',
                itemId:'defValueComboFS',
                border:false,
                padding:'0 0 0 0',
                margin:'0 0 0 0',
                hidden:true,
                layout:'hbox',
                items :[{
                    fieldLabel: '默认值',
                    width:'87%',
                    name : 'defValue',
                    submitValue:false,
                    queryMode: 'local',
                    xtype : 'combo',
                    autoSelect:true,
                    editable:true,
                    forceSelection:true,
                    emptyText:'-- 请选择 --',
                    displayField : 'text',
                    valueField : 'itemId',
                    allowBlank : true,
                    store:Ext.create('admin.store.DataDictItemStore')
                },{
                    iconCls:'table_save',
                    name : 'editUserFieldDict',
                    xtype : 'button'
                }]
            },{
                fieldLabel: '初始字段',
                name : 'initName',
                xtype : 'textfield'
            },{
                xtype:'fieldset',
                itemId:'isMoneyFS',
                border:false,
                padding:'0 0 0 0',
                margin:'0 0 0 0',
                items :[{
                    xtype: 'checkboxfield',
                    fieldLabel:'是否金额',
                    name:'userField.isMoney',
                    boxLabelAlign:'before',
                    uncheckedValue : false,
                    inputValue:true
                },{
                    fieldLabel: '格式',
                    xtype : 'textfield',
                    name: 'userField.dateFormat',
                    note:'格式如：0,000'
                }]
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