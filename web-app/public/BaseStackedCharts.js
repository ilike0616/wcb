Ext.define("public.BaseStackedCharts", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.baseStackedCharts',
    layout: 'border',
    initComponent: function() {
        var me = this,myChart,option;
        var drawChart = function() {
            require.config({
                paths: {
                    echarts: 'http://static.xiaoshouwuyou.com/echarts'
                }
            });
            require(
                [
                    'echarts',
                    'echarts/chart/bar',
                ],
                function(ec) {
                    var stackedBarPanel = me.down('panel[name=stackedBarChart]');
                    Ext.defer(function() {
                        me.myChart = ec.init(stackedBarPanel.el.dom,'infographic');
                        me.myChart.setOption(me.option);
                    }, 100);
                }
            );
            me.option = {
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: function (params,ticket,callback) {
                        var res = "";
                        for(var i=0;i<params.length;i=i+2){
                            if(i==0) res += params[i].name + "</br>";
                            res += params[i][0]+"   目标：" + params[i].data + "   完成：" + params[i+1].data + "</br>";
                        }
                        return res
                    }
                },
                legend : {
                    data:[]
                },
                toolbox : {
                    show : true
                },
                calculable : true,
                grid: {y: 70, y2:30, x2:20},
                xAxis : [
                    {
                        type : 'category',
                        data : me.generateYearMonth() // 年月
                    },
                    {
                        type : 'category',
                        axisLine: {show:false},
                        axisTick: {show:false},
                        axisLabel: {show:false},
                        splitArea: {show:false},
                        splitLine: {show:false},
                        data : me.generateYearMonth()
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : []
            };
            me.loadStackedBarData(me.option);
        };
        Ext.apply(me, {
            items: [{
                xtype: 'tabpanel',
                region: 'center',
                tabBar: {
                    padding: '3 5 3 0',
                    items: [
                        {xtype: 'tbfill'},
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
                                    me.refreshStackedBar(me.myChart, me.option);
                                }
                            }
                        },
                        {
                            xtype: 'numberfield',
                            margin: '3 0 3 5',
                            fieldLabel: '请输入年份',
                            labelWidth: 90,
                            width: 200,
                            value: new Date().getFullYear(),
                            minValue: 2000,
                            maxValue: 9999,
                            listeners: {
                                change: function (o, newValue, oldValue, eOpts) {
                                    if (o.validate()) {
                                        me.refreshStackedBar(me.myChart, me.option);
                                    }
                                }
                            }
                        }
                    ]
                },
                items: [{
                    title:'栈图',
                    xtype:'panel',
                    name: 'stackedBarChart',
                    region: 'center'
                }]
            }],
            listeners:{
                render:function(){
                    drawChart();
                }
            }
        });
        me.callParent(arguments);
    },
    // 刷新数据
    refreshStackedBar:function(myChart,option){
        var me = this;
        var aimYearObj = me.down('numberfield');
        if(!aimYearObj.validate()){
            return;
        }
        var xAxis = option.xAxis;
        xAxis[0].data = me.generateYearMonth(aimYearObj.getValue());
        xAxis[1].data = me.generateYearMonth(aimYearObj.getValue());
        me.loadStackedBarData(option);
        myChart.setOption(option);
    },
    loadStackedBarData:function(option){
        var me = this;
        var aimYear = me.down('numberfield').getValue();
        var aimKind = me.down('combo').getValue();
        Ext.Ajax.request({
            url: me.url,
            method: "POST",
            params: {
                aimKind:aimKind,
                aimYear:aimYear
            },
            async: false,
            success: function (response, opts) {
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    option.legend.data = [];
                    option.series = [];
                    var data = result.data;
                    Ext.Array.each(data, function (v,index) {
                        option.legend.data.push(v.ownerName);
                        var aimObj = {
                            name: v.ownerName,
                            type:'bar',
                            stack: '目标',
                            data: v.aimArr
                        }
                        var aimCompleteObj = {
                            name: v.ownerName,
                            type:'bar',
                            stack: '完成',
                            data: v.aimCompleteArr
                        }
                        option.series.push(aimObj);
                        option.series.push(aimCompleteObj);
                    });
                }
            },
            failure: function (response, opts) {
            }
        })
    },
    generateYearMonth : function(aimYear){
        var currentDate = new Date();
        if(Ext.typeOf(aimYear) != 'undefined') currentDate.setFullYear(aimYear);
        var yearMonths = [];
        for(var i=0;i<12;i++){
            currentDate.setMonth(i);
            yearMonths.push(Ext.Date.format(currentDate, 'y-m'));
        }
        return yearMonths;
    }
});