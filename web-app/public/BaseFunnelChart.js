/**
 * Created by guozhen on 2015-6-27.
 */
Ext.define('public.BaseFunnelChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.baseFunnelChart',
    layout: 'border',
    initComponent: function() {
        var me = this,myChart,option;
        var title = me.title1;
        if(Ext.typeOf(title) == 'undefined') title = "";
        var drawChart = function() {
            require.config({
                paths: {
                    echarts: 'http://static.xiaoshouwuyou.com/echarts'
                }
            });
            require(
                [
                    'echarts',
                    'echarts/chart/funnel',
                ],
                function(ec) {
                    var funnelPanel = me.down('panel[name=funnelChart]');
                    Ext.defer(function() {
                        me.myChart = ec.init(funnelPanel.el.dom,'infographic');
                        me.myChart.setOption(me.option);
                    }, 100);
                }
            );
            me.option = {
                title : {
                    text : title,
                    x : 'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function (params,ticket,callback) {
                        var data = params.data;
                        var res = data.name + ": " + data.value + "%(" + data.realValue + ")"
                        return res
                    }
                },
                legend : {
                    orient:'vertical',
                    x:'left',
                    y:'center',
                    data:[]
                },
                toolbox : {
                    show : true
                },
                series : [{
                    type:'funnel',
                    width:'70%',
                    funnelAlign:'center',
                    sort : 'descending',
                    data:[]
                }]
            };
            me.loadFunnelData(me.option);
        };
        Ext.apply(me, {
            items:[{
                xtype: 'panel',
                region: 'center',
                layout:'border',
                items:[{
                    xtype:'radiogroup',
                    layout: 'hbox',
                    columns: 2,
                    region: 'north',
                    items: [
                        {
                            boxLabel: '组内（自己和下属）',
                            name: me.id+'meAndLower',
                            inputValue: '0',
                            checked:true,
                            margin:'0 20 0 10'
                        }, {
                            boxLabel: '自己',
                            name: me.id+'meAndLower',
                            inputValue: '1'
                        }
                    ],
                    listeners:{
                        change:function(o, newValue, oldValue, eOpts ){
                            me.refreshFunnel(me.myChart,me.option);
                        }
                    }
                },{
                    xtype:'panel',
                    name: 'funnelChart',
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
    refreshFunnel:function(myChart,option){
        var me = this;
        me.loadFunnelData(option);
        myChart.setOption(option);
    },
    loadFunnelData:function(option){
        var me = this;
        var url = 'base/statisticFunnelRanking';
        if(Ext.typeOf(me.url) != 'undefined'){
            url = me.url;
        }
        var meAndLower = me.down("radiofield[name="+me.id+"meAndLower]").getValue();
        Ext.Ajax.request({
            url: url,
            method: "POST",
            params: {
                moduleId:me.moduleId,
                statType : me.statType,
                statField : me.statField,
                groupField:me.groupField,
                meAndLower:meAndLower
            },
            async: false,
            success: function (response, opts) {
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    option.legend.data = [];
                    option.series = [];
                    var data = result.data;
                    var legend = data.legend;
                    var arr = data.seriesObj;
                    Ext.Array.each(arr, function (v) {
                        option.legend.data.push(v.name);
                        option.series.push({
                            name: v.name,
                            type:'funnel',
                            width:'70%',
                            height:'70%',
                            funnelAlign:'center',
                            sort : 'descending',
                            data: arr
                        });
                    });
                }
            },
            failure: function (response, opts) {
            }
        })
    }

})

