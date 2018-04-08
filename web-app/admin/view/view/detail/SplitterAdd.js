/**
 * Created by guozhen on 2014/11/18.
 */
Ext.define("admin.view.view.detail.SplitterAdd",{
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailSplitterAdd',
    modal: true,
    width: 450,
    layout: 'anchor',
    title: '添加分隔符',
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
                        labelWidth: 150
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield',
                        width: 350
                    },
                    items: [
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '分隔符类型',
                            columns: 2,
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
                            xtype:'hiddenfield',
                            name:'view.id'
                        },{
                            xtype:'hiddenfield',
                            name:'user.id'
                        },{
                            xtype:'hiddenfield',
                            name:'pageType',
                            value:'splitter'
                        },{
                            xtype:'hiddenfield',
                            name:'module.id'
                        }],
                    buttons: [{
                        text:'保存',iconCls:'table_save',itemId : 'save'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})