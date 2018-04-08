/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewGridField",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewGridField',
    modal: true,
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
                fieldType : 'Grid',
                viewType:'Form',
                store:[
                    ['grid','表格']
                ]
            },{
                border : 0,
                fieldLabel: '关联视图',
                name:'listView.title',
                xtype : 'baseSpecialTextfield',
                store : 'ViewStore',
                viewId:'ViewList',
                hiddenName:'listView',
                fromType:'admin',
                paramName:'user.id',
                allowBlank : false,
                operateBtn : false,
                enableComplexQuery:false,
                extraParams: {clientType:'pc',viewType:'list'},
                userId:0,
                columns:[
                    {
                        xtype: 'rownumberer',
                        width: 40,
                        sortable: false
                    },
                    {
                        text:'标题',
                        dataIndex:'title'
                    },
                    {
                        text:'视图Id',
                        dataIndex:'viewId'
                    },
                    {
                        text:'所属用户',
                        dataIndex:'user.name'
                    },
                    {
                        text:'所属模块',
                        dataIndex:'module.moduleName'
                    }
                ]
            },{
                xtype: 'checkboxfield',
                fieldLabel:'是否只读',
                name:'readOnly',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                note:'该字段值会提交到后台'
            },{
                fieldLabel: '字段说明',
                name : 'remark',
                xtype : 'textareafield'
            },{
                fieldLabel: '默认值',
                name : 'defValue',
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