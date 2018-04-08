/**
 * Created by guozhen on 2015-7-1.
 */
Ext.define('public.BaseYearStatList', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.baseYearStatList',
    layout:'border',
    initComponent: function() {
        var me = this,
        myStore = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data'],
            data: []
        });
        me.loadMyStoreData(myStore,null);
        var store = me.store;
        Ext.apply(store.proxy.extraParams,{statType:me.statType});
        store.load();
        /****************获取柱状图数据--结束*****************/
        Ext.applyIf(me, {
            store:myStore,
            items: [{
                xtype: 'tabpanel',
                region: 'center',
                tabBar: {
                    padding: '3 5 3 0',
                    items: [
                        {xtype: 'tbfill'},
                        {
                            xtype: 'numberfield',
                            margin: '3 0 3 5',
                            fieldLabel: '请输入年份',
                            labelWidth: 90,
                            width: 200,
                            minValue: 2000,
                            maxValue: 9999,
                            listeners: {
                                change: function (o, newValue, oldValue, eOpts) {
                                    if (o.validate()) {
                                        me.loadMyStoreData(myStore, newValue);
                                        Ext.apply(store.proxy.extraParams,{year: newValue});
                                        store.load();
                                    }
                                }
                            }
                        }
                    ]
                },
                items: [{
                    title: '列表',
                    xtype: 'panel',
                    layout: 'fit',
                    items: [
                        Ext.create('Ext.grid.Panel', {
                            autoScroll: true,
                            store: store,
                            features: [{
                                ftype: 'groupingsummary',
                                groupHeaderTpl: '{name}',
                                hideGroupedHeader: true,
                                enableGroupingMenu: false
                            }],
                            columns: {
                                defaults: {
                                    width: 70,
                                    sortable: true
                                },
                                items: [{
                                    header: me.firstHeader,
                                    width: 150,
                                    dataIndex: me.firstHeaderDataIndex,
                                    hideable: false,
                                    tdCls: 'task',
                                    summaryType: 'count',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return '(员工:' + value + ")";
                                    }
                                }, {
                                    header: '一月',
                                    dataIndex: 'JANUARY',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '二月',
                                    dataIndex: 'FEBRUARY',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '三月',
                                    dataIndex: 'MARCH',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '四月',
                                    dataIndex: 'APRIL',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '五月',
                                    dataIndex: 'MAY',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '六月',
                                    dataIndex: 'JUNE',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '七月',
                                    dataIndex: 'JULY',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '八月',
                                    dataIndex: 'AUGUST',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '九月',
                                    dataIndex: 'SEPTEMBER',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '十月',
                                    dataIndex: 'OCTOBER',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '十一月',
                                    dataIndex: 'NOVEMBER',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }, {
                                    header: '十二月',
                                    dataIndex: 'DECEMBER',
                                    summaryType: 'sum',
                                    summaryRenderer: function (value, summaryData, dataIndex) {
                                        return value;
                                    }
                                }]
                            }
                        })
                    ]
                },
                    {
                        title: '柱状图',
                        xtype: 'panel',
                        layout: {
                            type: 'hbox',       // Arrange child items vertically
                            align: 'stretch',    // Each takes up full width
                            padding: 5
                        },
                        items: [{
                            xtype: 'chart',
                            flex: 5,
                            itemId: 'column',
                            animate: true,
                            store: myStore,
                            axes: [{
                                type: 'Numeric',
                                position: 'left',
                                fields: ['data'],
                                label: {
                                    //renderer : Ext.util.Format.numberRenderer('0,0')
                                },
                                title: '记录数',
                                grid: true,
                                minimum: 0
                            }, {
                                type: 'Category',
                                position: 'bottom',
                                fields: ['name'],
                                label: {
                                    rotate: {
                                        degrees: 325
                                    }
                                }
                            }],
                            series: [{
                                type: 'column',
                                axis: 'left',
                                highlight: true,
                                tips: {
                                    trackMouse: true,
                                    width: 140,
                                    height: 28,
                                    renderer: function (storeItem, item) {
                                        this.setTitle(storeItem.get('name') + ': ' + storeItem.get('data'));
                                    }
                                },
                                label: {
                                    display: 'insideEnd',
                                    'text-anchor': 'middle',
                                    field: 'data',
                                    renderer: Ext.util.Format.numberRenderer('0'),
                                    orientation: 'vertical',
                                    color: '#333'
                                },
                                xField: 'name',
                                yField: 'data'
                            }]
                        }, {
                            xtype: 'grid',
                            store: myStore,
                            flex: 2,
                            columns: [
                                {
                                    text: '月份',
                                    dataIndex: 'name',
                                    flex: 3
                                },
                                {
                                    text: '记录数',
                                    dataIndex: 'data',
                                    flex: 2
                                }
                            ]
                        }]
                    }]
            }],
            listeners:{
                afterrender:function( o, eOpts ){
                    var dt = new Date();
                    var year = Ext.Date.format(dt, 'Y');
                    o.down('numberfield').setValue(year);
                }
            }
        });
        me.callParent(arguments);
    },
    loadMyStoreData:function(myStore,year){
        var me = this;
        myStore.removeAll();
        var url = 'statistics/statYearStatColumnChart';
        if(Ext.typeOf(me.columnChartUrl) != 'undefined' ){
            url = me.columnChartUrl;
        }
        Ext.Ajax.request({
            url: url,
            params: {
                year:year,
                statType:me.statType
            },
            method: 'POST',
            timeout: 4000,
            async: false,
            success: function (response, opts) {
                var d = Ext.JSON.decode(response.responseText);
                myStore.loadData(d.data);
            },
            failure: function (response, opts) {
                var errorCode = "";
                if (response.status) {
                    errorCode = 'error:' + response.status;
                }
                Ext.example.msg('提示', '操作失败！' + errorCode);
            }
        });
    }
})

