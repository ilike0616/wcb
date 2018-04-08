/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewFileFormField",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewFileFormField',
    modal: true,
    title: '文件字段',
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
                fieldType : 'File',
                viewType:'Form',
                store:[
                    ['baseUploadField','多文件'],
                    ['baseUploadImageField','多图片'],
                    ['baseImageField','单图片']
                ]
            },{
                fieldLabel: '文件类型',
                name : 'userField.dateFormat',
                xtype : 'textfield',
                note :'限制文件类型多个文件逗号(;)分隔.如：*.gif;*.jpg'
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否必填',
                name:'userField.bitian',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true
            },{
                fieldLabel: '字段说明',
                name : 'remark',
                xtype:'textfield'
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