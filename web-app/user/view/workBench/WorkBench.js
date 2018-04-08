/**
 * Created by guozhen on 2015/1/23.
 */
Ext.define('user.view.workBench.WorkBench', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workBench',
    border:0,
    autoScroll: true,
    lastLoadTime:new Date(),
    requires: ['Ext.app.PortalPanel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    getTools: function(){
        var me = this;
        return [{
            xtype: 'tool',
            type: 'refresh',
            handler: function(e, target, header, tool){
                var portlet = header.ownerCt;
                me.refreshPortalPanel(portlet);
            }
        }];
    },
    initComponent: function(){
        var me = this;
        var its = me.loadItems();
        Ext.apply(me, {
            items: [{
                xtype: 'portalpanel',
                border: 0,
                its: its,
                listeners:{
                    drop:function(node, data, overModel, dropPosition, eOpts){
                        var data = [];
                        var items = me.down('portalpanel').items.items;
                        Ext.Array.each(items,function(pItem,pIndex){
                            var offset = pIndex+1;
                            Ext.Array.each(pItem.items.items,function(item,index){
                                data.push(Ext.JSON.encode({id:item.dataId,userPortalId:item.userPortalId,idx:index+offset++}));
                            })
                        })
                        if(data != null && data.length > 0) data.join(",");
                        me.GridDoActionUtil.doAjax('userPortal/sortEmployeePortalIdx',{data:"["+data+"]"},null);
                    }
                }
            }]
        });
        me.callParent(arguments);
    },
    loadItems:function(){
        var me = this;
        var its = [];
        Ext.Ajax.request({
            url: 'userPortal/workBenchShowPortal',
            method: 'POST',
            timeout: 4000,
            async: false,
            success: function (response, opts) {
                var result = Ext.JSON.decode(response.responseText);
                its = result.portals;
                Ext.Array.each(its, function(it) {
                    it.tools = me.getTools();
                });
            },
            failure: function (e, op) {
                Ext.Msg.alert("发生错误");
            }
        });
        return its;
    },
    // 刷新工作台
    refreshWorkBench:function(workBench){
        var me = this;
        var panel = workBench.down('portalpanel');
        panel.removeAll();
        var its = me.loadItems();
        var items = [];
        var items1 = [];
        var items2 = [];
        Ext.each(its,function(val,idx){
            var i = idx%2;
            if(i==0){
                items1.push(val);
            }else if(i==1){
                items2.push(val);
            }
        });
        var items = [{
            items: items1
        },{
            items: items2
        }]
        panel.add(items);
    },
    refreshPortalPanel:function(portlet){
        // 因为有些刷新没有加载效果，所以为了看起来有加载的动作，所有增加这句代码
        portlet.setLoading('加载中...');
        if(portlet.down('baseStatisticCharts') != null){ // 基于baseStatisticCharts
            var baseStatisticCharts = portlet.down('baseStatisticCharts');
            baseStatisticCharts.loadData(baseStatisticCharts.store,[]);
        }else if(portlet.down('baseYearStatList') != null){ // 基于baseYearStatList
            // 列表
            var gridPanel = portlet.down('gridpanel');
            Ext.apply(gridPanel.getStore().proxy.extraParams,{})
            gridPanel.getStore().load();
            // 图形
            var baseYearStatList = portlet.down('baseYearStatList');
            baseYearStatList.loadMyStoreData(baseYearStatList.store,null);
        }else if(portlet.down('baseFunnelChart') != null){ // 基于baseFunnelChart
            var baseFunnelChart = portlet.down('baseFunnelChart');
            baseFunnelChart.refreshFunnel(baseFunnelChart.myChart,baseFunnelChart.option);
        }else if(portlet.down('baseStackedCharts') != null){ // 基于baseStackedCharts
            var baseStackedChart = portlet.down('baseStackedCharts');
            baseStackedChart.refreshStackedBar(baseStackedChart.myChart,baseStackedChart.option);
        }else if(portlet.down('baseList') != null){ // 列表
            var baseList = portlet.down('baseList');
            baseList.getStore().load();
        }
        // 消除加载层
        Ext.defer(function() {
            portlet.setLoading(false);
        }, 500);
    }
});
