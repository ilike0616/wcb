/**
 * Created by guozhen on 2015-01-06.
 */
Ext.define("admin.view.view.detail.ViewSplitterField",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewSplitterField',
    title: '分隔符',
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
                xtype: 'radiogroup',
                fieldLabel: '分隔符类型',
                columns: 1,
                vertical: true,
                items: [
                    { boxLabel: '开始标记', name: 'inputType', inputValue: 'start',checked: true},
                    { boxLabel: '结束标记', name: 'inputType', inputValue: 'end'}
                ]
            },{
                fieldLabel: '分割标题',
                name: 'label',
                note: '如果为空，则默认为分隔符xxx。其中xxx为序号，从1开始'
            },{
                xtype : 'checkboxfield',
                name: 'defExpanded',
                fieldLabel: '默认是否展开',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true,
                checked : true
            },{
                xtype : 'checkboxfield',
                name: 'isCollapsible',
                fieldLabel: '是否允许展开或关闭',
                boxLabelAlign:'before',
                uncheckedValue : false,
                inputValue:true
            },{
                name : 'id',
                xtype : 'hidden'
            },{
                xtype:'hiddenfield',
                name:'user.id'
            },{
                xtype:'hiddenfield',
                name:'pageType',
                value:'splitter'
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