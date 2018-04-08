/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewHiddenField",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewHiddenField',
    modal: true,
    title: '隐藏字段',
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
                xtype: 'checkboxfield',
                fieldLabel:'是否提交该值',
                name:'isSubmitValue',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                hidden:true,
                note:'值为true,则该字段值不会提交到后台'
            },{
                fieldLabel: '显示方式',
                name : 'pageType',
                xtype : 'combo',
                autoSelect:true,
                forceSelection:true,
                emptyText:'-- 请选择 --',
                allowBlank : false,
                fieldType : 'Grid',
                viewType:'Form',
                store:[
                    ['hidden','隐藏']
                ]
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