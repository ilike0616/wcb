/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewStringFormField",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewStringFormField',
    title: '字符字段',
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
        items: [{
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
                fieldType : 'String',
                viewType:'Form',
                store:[
                    ['textfield','文本框'],
                    ['textfield2','通行文本'],
                    ['textarea','多行文本'],
                    ['multichoice','多选框'],
                    ['htmleditor','富文本框'],
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
                        fieldLabel:'最小长度',
                        name:'userField.min',
                        minValue:0,
                        boxLabelAlign:'before',
                        note:'输入的最小字符个数'
                    },{
                        xtype: 'numberfield',
                        fieldLabel:'最大长度',
                        name:'userField.max',
                        boxLabelAlign:'before',
                        note:'输入的最大字符个数'
                    }]
                },{
                    xtype:'fieldset',
                    itemId:'colsAndRowsFS',
                    border:false,
                    padding:'0 0 0 0',
                    margin:'0 0 0 0',
                    items :[{
                        xtype: 'numberfield',
                        fieldLabel:'显示宽度',
                        name:'cols',
                        boxLabelAlign:'before'
                    },{
                        xtype: 'numberfield',
                        fieldLabel:'显示高度',
                        name:'rows',
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
                    fieldLabel: '输入格式',
                    name : 'inputFormat',
                    xtype : 'combo',
                    autoSelect:true,
                    forceSelection:true,
                    emptyText:'-- 请选择 --',
                    belongToName:'ViewStringField',
                    store:[
                        ['email','邮件格式'],
                        ['url','URL地址'],
                        ['phone','电话格式'],
                        ['baseSpecialTextfield','弹窗'],
                        ['baseMultiSelectTextareaField','多文本框选择']
                    ]
                },{
                    xtype:'fieldset',
                    itemId:'inputFormatParamFS',
                    border:false,
                    padding:'0 0 0 0',
                    margin:'0 0 0 0',
                    hidden:true,
                    items :[{
                        fieldLabel: 'store',
                        name : 'paramStore',
                        queryMode: 'local',
                        xtype : 'combo',
                        autoSelect:true,
                        editable:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        displayField : 'name',
                        valueField : 'store',
                        allowBlank : true,
                        store:Ext.create('admin.store.ModuleStoreStore')
                    },{
                        fieldLabel: 'viewId',
                        name : 'paramViewId',
                        queryMode: 'local',
                        xtype : 'combo',
                        autoSelect:true,
                        editable:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        displayField : 'title',
                        valueField : 'viewId',
                        allowBlank : true,
                        store:Ext.create('admin.store.ViewStore')
                    },{
                        xtype:'fieldset',
                        itemId:'conditionFS',
                        border:false,
                        padding:'0 0 0 0',
                        margin:'0 0 0 0',
                        layout:'hbox',
                        items :[{
                            width:'87%',
                            xtype:'textarea',
                            name:'extraCondition',
                            fieldLabel:'附加条件',
                            rows:10,
                            readOnly:true
                        },{
                            iconCls:'table_save',
                            name : 'configExtraCondition',
                            xtype : 'button'
                        }]
                    }]
                },{
                    fieldLabel: '字段说明',
                    name : 'remark',
                    xtype : 'textfield'
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
                    multiSelect:true,
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