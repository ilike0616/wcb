/**
 * Created by guozhen on 2014/11/18.
 */
Ext.define("admin.view.view.detail.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailEdit',
    modal: true,
    title: '修改视图明细',
    layout : 'border',
    width: 800,
    height: 400,
    items: [{
        region:'west',
        xtype: 'form',
        bodyStyle: 'padding:5px 5px 0',
        fieldDefaults: {
            align : 'center',
            labelAlign: 'right',
            width:300,
            labelWidth: 100
        },
        defaults: {
            msgTarget: 'side',
            xtype: 'textfield'
        },
        items: [{
            fieldLabel: '字段标题',
            name: 'label',
            allowBlank : false
        },{
            xtype : 'checkboxfield',
            fieldLabel: '是否开启公式',
            name: 'openFormula'
        },{
            xtype : 'checkboxfield',
            fieldLabel: '是否开启合计',
            name: 'openSum'
        },{
            fieldLabel: 'pageType',
            name: 'pageType',
            xtype: 'combo',
            autoSelect:true,
            forceSelection:true,
            emptyText:'-- 请选择 --',
            allowBlank : false,
            store:[
                ['textfield','文本框'],
                ['datefield','日期'],
                ['datetime','日期+时间'],
                ['combo','下拉框'],
                ['textarea','多行文本'],
                ['checkbox','勾选框'],
                ['checkboxgroup','复选框'],
                ['radio','单选'],
                ['url','超链接'],
                ['htmleditor','富文本框'],
                ['baseUploadField','文件'],
                ['baseImageField','图片'],
                ['percent','百分比'],
                ['email','邮件地址'],
                ['money','金额'],
                ['group','字段分组'],
                ['list','列表']
            ]
        },{
            fieldLabel: '格式',
            name: 'inputType'
        },{
            fieldLabel: '默认值',
            name: 'defValue'
        },{
            xtype : 'textarea',
            fieldLabel: '备注',
            name: 'remark',
            cols : 25,
            rows : 3
        },{
            xtype:'hiddenfield',
            name:'view.id'
        },{
            xtype:'hiddenfield',
            name:'id'
        }]
    },{
        region: 'center',
        xtype : 'viewDetailPropertyList',
        name : 'propertyGridView'
    }],
    buttons: [{
        text:'保存',iconCls:'table_save',itemId : 'save'
    }]
});