Ext.define("admin.view.view.detail.PropertyAdd", {
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailPropertyAdd',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '新增属性',
    items: [
        {
            xtype: 'form',
            layout: 'vbox',
            defaults: {
                msgTarget: 'side',
                xtype: 'textfield',
                padding: '3 5 3 5',
                labelWidth : 120
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'paramName',
                    allowBlank: false,
                    beforeLabelTextTpl: required
                },
                {
                    fieldLabel: '值类型',
                    name: 'paramType',
                    xtype: 'combo',
                    autoSelect:true,
                    forceSelection:true,
                    allowBlank : false,
                    value:'String',
                    beforeLabelTextTpl: required,
                    store: [
                        ['String','字符型'],
                        ['Integer','数值型'],
                        ['Date','日期型'],
                        ['Boolean','布尔型']
                    ]
                },
                {
                    fieldLabel: '值',
                    name: 'paramValue',
                    allowBlank: false,
                    beforeLabelTextTpl: required
                },
                {
                    fieldLabel: '是否editor',
                    name: 'isBelongToEditor',
                    xtype: "checkbox",
                    checked: false,
                    uncheckedValue: false,
                    inputValue: true
                },{
                    xtype:'hiddenfield',
                    name:'viewDetail'
                }
            ]
        }
    ],
    buttons: [
        {
            text: '保存', itemId: 'save', iconCls: 'table_save', autoInsert: true, target: 'viewDetailPropertyList'
        }
    ]
})