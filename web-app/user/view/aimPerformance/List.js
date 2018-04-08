/**
 * Created by guozhen on 2015/07/23
 */
Ext.define('user.view.aimPerformance.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.aimPerformanceList',
    autoScroll: true,
    split:true,
    forceFit:true,
    store : Ext.create('user.store.AimPerformanceStore'),
    selType: 'rowmodel',
    enableBasePaging: false,
    initComponent: function () {
        var me = this;
        Ext.applyIf(me,{
            tbar:{
                xtype: 'container',
                itemId:'tbarContainer',
                layout: 'anchor',
                defaults: {anchor: '0',border:0},
                defaultType: 'toolbar',
                items:[{
                    items:[
                        {xtype:'button',text:'明细',itemId:'aimDetailButton',iconCls:'table_add'},
                        {xtype: 'tbfill'},
                        {
                            xtype: 'baseComboBoxTree',
                            name: 'dept',
                            fieldLabel: '部门',
                            displayField: 'text',
                            valueField:'id',
                            labelWidth:50,
                            rootVisible: false,
                            minPickerHeight: 200,
                            width:400,
                            margin:'0 5 0 0',
                            store : Ext.create('user.store.DeptStoreForEdit'),
                            listeners:{
                                scope:me,
                                select:function(){
                                    me.search(me);
                                }
                            }
                        },
                        {
                            fieldLabel: '类型',
                            labelWidth:40,
                            name: 'aimKind',
                            xtype: 'combo',
                            autoSelect: true,
                            forceSelection: true,
                            allowBlank: false,
                            value:'aimCustomer',
                            store: [
                                ['aimCustomer', '客户数'],
                                ['aimOrderMoney', '销售额'],
                                ['aimPayMoney', '回款额']
                            ],
                            listeners:{
                                scope: me,
                                change:function(o, newValue, oldValue, eOpts){
                                    me.search(me);
                                }
                            }
                        },
                        {
                            xtype: 'numberfield',
                            labelWidth:70,
                            name: 'aimYear',
                            fieldLabel: '查询年份',
                            minValue: 2000,
                            maxValue: 9999,
                            value: new Date().getFullYear(),
                            listeners: {
                                scope: me,
                                change: function (o, newValue, oldValue, eOpts) {
                                    if (o.validate()) {
                                        me.search(me);
                                    }
                                }
                            }
                        },{
                            xtype:'button',
                            text:'查询',
                            handler:function(btn){
                               me.search(me);
                            }
                        }
                    ]
                }]
            },
            columns : me.generateColumns()
        })
        me.callParent();
    },
    generateColumns:function(aimYear){
        var columns = [{
            text: '用户',
            sortable: false,
            locked:true,
            width:300,
            xtype:'treecolumn',
            dataIndex: 'ownerName'
        }];
        var currentDate = new Date();
        if(aimYear != null && aimYear != "") currentDate.setFullYear(aimYear);
        var num = 0;
        var yearMonth = "";
        for(var i=0;i<13;i++){ // 此处多循环一次，多循环的一次为统计
            if(i == 12){
                num = 'sum';
                yearMonth = '总计';
            }else{
                num = i + 1;
                currentDate.setMonth(i);
                yearMonth = Ext.Date.format(currentDate, 'Y-m');
            }
            columns.push({
                text: yearMonth,
                menuDisabled:true,
                columns: [{
                    text: '目标',
                    sortable: false,
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aim_'+num
                }, {
                    text: '完成',
                    sortable: false,
                    align:'center',
                    width:70,
                    menuDisabled:true,
                    dataIndex: 'aimComplete_'+num
                }, {
                    text: '完成%',
                    sortable: false,
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aimCompletePercent_'+num,
                    renderer: function(value){
                        if(value >= 100){
                            return '<span style="color:green">' + value + '%</span>';
                        }else{
                            return '<span style="color:red">' + value + '%</span>';
                        }
                    }
                }]
            })
        }
        return columns;
    },
    search:function(me){
        var aimYear = me.down('numberfield[name=aimYear]');
        if (!aimYear.validate()) {
            return false;
        }
        var aimKindValue = me.down('combo[name=aimKind]').getValue();
        var deptValue = me.down('baseComboBoxTree[name=dept]').getValue();
        me.reBuildGrid(aimYear.getValue(),aimKindValue,deptValue);
    },
    reBuildGrid:function(aimYear,aimKind,deptValue){
        var me = this;
        var main = me.up('aimPerformanceMain');
        var chart = main.down('aimPerformanceChart');
        chart.loadData(null,aimYear);
        var columns = me.generateColumns(aimYear,aimKind);
        me.store.load({params:{aimYear:aimYear,aimKind:aimKind,dept:deptValue}});
        me.reconfigure(me.store, columns);
    }
});