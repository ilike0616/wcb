/**
 * Created by shqv on 2014-9-12.
 */
Ext.define("admin.view.userField.StringField",{
    extend: 'Ext.window.Window',
    alias: 'widget.stringField',
    modal: true,
    title: '字符字段',
    layout : 'border',
    width: 700,
    height: 400,
    items: [{
        region:'west',
        xtype: 'form',
        width:330,
        overflowY:'auto',
        bodyStyle: 'padding:5px 5px 0',
        fieldDefaults: {
            align : 'center',
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
                fieldLabel: '所属用户',
                name : 'user.name',
                xtype : 'displayfield'
            },{
                fieldLabel: '所属模块',
                name : 'module.moduleName',
                xtype : 'displayfield'
            },{
                fieldLabel: '字段标题',
                name: 'text'
            },{
                fieldLabel: '字段名',
                name : 'fieldName',
                xtype : 'displayfield'
            },{
                fieldLabel: '数据类型',
                name : 'dbTypeName',
                xtype : 'displayfield'
            },{
                fieldLabel: '最大长度',
                name : 'maxSize',
                xtype : 'displayfield'
            },{
                fieldLabel: '是否必填',
                name : 'bitian',
                inputValue:true,
                uncheckedValue:false,
                xtype : 'checkboxfield'
            },{
                fieldLabel: '是否唯一',
                name : 'weiyi',
                inputValue:true,
                uncheckedValue:false,
                xtype : 'checkboxfield'
            },{
                fieldLabel: '模糊查询',
                name : 'mohu',
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
                    ['textfield','文本框'],
                    ['textarea','多行文本'],
                    ['multichoice','多选框'],
                    ['htmleditor','富文本框'],
                    ['baseSpecialTextfield','弹出选择'],
                    ['hidden','隐藏']
                ]
            },{
                fieldLabel: '字典',
                name : 'dict',
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
                name : 'id',
                xtype : 'hidden'
            },{
                name : 'user.id',
                xtype : 'hidden'
            }
        ]
    },{
        region: 'center',
        xtype : 'propertygrid',
        name : 'propertyGrid',
        allowDeselect : true,
        tbar: [{
            text:'新增一行',
            itemId:'add_line',
            iconCls:'table_add'
        }
        ],
        source: {
        }
    }],
    buttons: [{
        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'userFieldMain userFieldList[dbtype=string]'
    }]
})