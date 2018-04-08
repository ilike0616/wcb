/**
 * Created by guozhen on 2015/12/15.
 */
Ext.define('admin.view.view.detail.ViewDetailCondList', {
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailCondList',
    layout: 'anchor',
    modal: true,
    title: '条件明细',
    width: 700,
    height: 350,
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                xtype: 'baseList',
                autoScroll: true,
                forceFit:true,
                enableBasePaging:false,
                store:Ext.create('admin.store.ViewDetailCondStore'),
                tbar:[
                    {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
                    {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true},
                    {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true}
                ],
                columns:[{
                        xtype: 'rownumberer',
                        width: 40,
                        sortable: false
                    },{
                        text:'字段名称',
                        dataIndex:'fieldNameText'
                    },{
                        text:'操作符',
                        dataIndex:'operatorText'
                    },{
                        text:'值',
                        dataIndex:'userFieldTextValue'
                    },{
                        text:'开始时间',
                        dataIndex:'startDate',
                        xtype: 'datecolumn',
                        format:'Y-m-d'
                },{
                        text:'结束时间',
                        dataIndex:'endDate',
                        xtype: 'datecolumn',
                        format:'Y-m-d'
                    }
                ],
                listeners:{
                    scope:me,
                    afterrender:function(o, eOpts ){
                        var me = this;
                        var extraConditionTextareaVal = me.extraConditionTextarea.getValue();
                        if(extraConditionTextareaVal){
                            var gridStore = me.down('grid').getStore();
                            var objArr = Ext.JSON.decode(extraConditionTextareaVal);
                            Ext.Array.each(objArr,function(obj){
                                gridStore.add(obj);
                            })
                        }
                    }
                }
            }],
            buttons: [{
                text:'保存',itemId:'generateJSONCondition',iconCls:'table_save'
            }]
        });
        me.callParent(arguments);
    }
});