/**
 * Created by shqv on 2014-9-10.
 */
Ext.define("admin.view.view.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewEdit',
    modal: true,
    plain:true,
    layout: 'fit',
    width:350,
    title: '修改视图',
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
                        fieldLabel: '所属用户',
                        name : 'user.name',
                        xtype : 'displayfield'
                    },{
                        fieldLabel: '视图Id',
                        name: 'viewId',
                        regex:/^\w+$/,
                        regexText : '字母或者数字',
                        allowBlank : false
                    },{
                        fieldLabel: '客户端类型',
                        name: 'clientType',
                        xtype: 'combo',
                        autoSelect:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        allowBlank : false,
                        store:[
                            ['pc','pc端'],
                            ['mobile','手机端']
                        ]
                    },{
                        fieldLabel: '视图类型',
                        name: 'viewType',
                        xtype: 'combo',
                        autoSelect:true,
                        forceSelection:true,
                        emptyText:'-- 请选择 --',
                        allowBlank : false,
                        store:[
                            ['list','列表'],
                            ['form','表单']
                        ]
                    },{
                        fieldLabel: '标题',
                        name: 'title',
                        allowBlank : false
                    },{
                    	xtype:'numberfield',
                    	fieldLabel: '显示列',
                    	minValue:1,
                    	maxValue:4,
                    	value:2,
                        name: 'columns'
                    },{
                        xtype: 'checkboxfield',
                        fieldLabel:'自动分配列宽',
                        name:'forceFit',
                        boxLabelAlign:'before',
                        note:'当视图类型为列表时，此选项才会生效',
                        uncheckedValue : false,
                        inputValue:true
                    },{
                        xtype: 'checkboxfield',
                        fieldLabel:'查询视图',
                        name:'isSearchView',
                        boxLabelAlign:'before',
                        note:'如果已存在查询视图，则会被覆盖掉！',
                        uncheckedValue : false,
                        inputValue:true
                    },{
                        xtype: 'checkboxfield',
                        fieldLabel: '是否导入视图',
                        name: 'isImportOrExportView',
                        boxLabelAlign: 'before',
                        note: '如果已存在导入视图，则会被覆盖掉！',
                        uncheckedValue: false,
                        inputValue: true
                    },{
                        xtype: 'checkboxfield',
                        fieldLabel: '是否可编辑',
                        name: 'editable',
                        boxLabelAlign: 'before',
                        note: '视图是否可以编辑数据',
                        uncheckedValue: false,
                        inputValue: true
                    },{
                        xtype:'hiddenfield',
                        name:'module.id'
                    },
                    {
                        xtype:'hiddenfield',
                        name:'model.id'
                    },
                    {
                        xtype:'hiddenfield',
                        name:'id'
                    }],
                    buttons: [{
                        text:'保存',iconCls:'table_save',autoUpdate:true,target:'viewMain viewList'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})