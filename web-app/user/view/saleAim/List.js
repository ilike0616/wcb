/**
 * Created by guozhen on 2015/07/23
 */
Ext.define('user.view.saleAim.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.saleAimList',
    autoScroll: true,
    title: '销售目标',
    split:true,
    forceFit:true,
    store : Ext.create('user.store.SaleAimStore'),
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
                        {xtype:'button',text:'设置',itemId:'aimSetButton',iconCls:'table_add'},
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
                                select:function(){
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
                            listeners:{
                                change:function(){
                                    me.search(me);
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
                    text: '销售额',
                    sortable: true,
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aimOrderMoney_'+num
                }, {
                    text: '客户数',
                    sortable: true,
                    align:'center',
                    width:70,
                    menuDisabled:true,
                    dataIndex: 'aimCustomer_'+num
                }, {
                    text: '回款额',
                    sortable: true,
                    align:'center',
                    menuDisabled:true,
                    dataIndex: 'aimPayMoney_'+num
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
        var deptValue = me.down('baseComboBoxTree').getValue();
        me.reBuildGrid(aimYear.getValue(),deptValue);
    },
    reBuildGrid:function(aimYear,deptValue){
        var me = this;
        var columns = me.generateColumns(aimYear);
        me.store.load({params:{aimYear:aimYear,dept:deptValue}});
        me.reconfigure(me.store, columns);
    }
});