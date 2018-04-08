window.myStore = Ext.create('Ext.data.JsonStore', {
    fields: ['name', 'data'],
    data: []
});

Ext.define("public.BaseStatisticWin", {
    extend: 'public.BaseWin',
    alias: 'widget.baseStatisticWin',
    height: 600,
    width: 750,
    title: '查询统计',
    layout: 'border',
    initComponent: function () {
        var me = this;
        var listStore = me.listDom.store;
        var createGroupField = me.createGroupField();
        myStore.removeAll();
        Ext.apply(this, {
            items: [
                {
                    xtype: 'panel',
                    itemId: 'optPanel',
                    region: 'north',
                    height: 60,
                    items: [{
                        layout: 'hbox',
                        border: 0,
                        margin: '15 0 0 0',
                        defaults: {
                            anchor: '0',
                            margin: '0 5 0 5'
                        },
                        items: [
                            {
                                xtype: 'label',
                                text: '统计类型：',
                                margin: '3 10 0 10'
                            },
                            createGroupField
                            //{
                            //    xtype: 'button',
                            //    text: '确定',
                            //    listeners: {
                            //        click: me.statisticSubmit,
                            //        scope: me
                            //    }
                            //}
                        ]
                    }]
                },
                {
                    xtype: 'tabpanel',
                    region: 'center',
                    items: [
                        {
                            xtype: 'panel',
                            title: '饼图',
                            layout: {
                                type: 'hbox',       // Arrange child items vertically
                                align: 'stretch',    // Each takes up full width
                                padding: 5
                            },
                            items: [{
                                xtype: 'chart',
                                flex: 3,
                                animate: true,
                                store: myStore,
                                shadow: true,
                                itemId: 'pie',
                                legend: {
                                    position: 'right'
                                },
                                insetPadding: 20,
                                theme: 'Base:gradients',
                                series: [{
                                    type: 'pie',
                                    field: 'data',
                                    showInLegend: true, // 是否显示图例
                                    donut: 20,
                                    tips: {
                                        trackMouse: true,
                                        width: 200,
                                        height: 23,
                                        renderer: function (storeItem, item) {
                                            var total = 0;
                                            myStore.each(function (rec) {
                                                total += rec.get('data');
                                            });
                                            this.setTitle(storeItem.get('name') + ': ' + (storeItem.get('data') / total * 100).toFixed(2) + '%('
                                            + storeItem.get('data') + ')');
                                        }
                                    },
                                    highlight: {
                                        segment: {
                                            margin: 20
                                        }
                                    },
                                    label: {
                                        field: 'name',
                                        display: 'rotate', // outside
                                        contrast: true,
                                        font: '12px Arial',
                                        renderer: function (value, label, storeItem, item, i, display, animate, index) {
                                            if (storeItem.get('data') == 0) {
                                                value = '';
                                            }
                                            return value;
                                        }
                                    }
                                }]
                            }, {
                                xtype: 'grid',
                                store: myStore,
                                flex: 1,
                                columns: [
                                    {
                                        text: '类别',
                                        dataIndex: 'name',
                                        flex: 3
                                    },
                                    {
                                        text: '记录数',
                                        dataIndex: 'data',
                                        flex: 2
                                    }
                                ]
                            }
                            ]
                        },
                        {
                            xtype: 'panel',
                            title: '柱状图',
                            layout: {
                                type: 'hbox',       // Arrange child items vertically
                                align: 'stretch',    // Each takes up full width
                                padding: 5
                            },
                            items: [
                                {
                                    xtype: 'chart',
                                    flex: 3,
                                    itemId: 'column',
                                    animate: true,
                                    store: myStore,
                                    axes: [{
                                        type: 'Numeric',
                                        position: 'left',
                                        fields: ['data'],
                                        label: {
                                            renderer: Ext.util.Format.numberRenderer('0,0')
                                        },
                                        title: '记录数',
                                        grid: true,
                                        minimum: 0
                                    }, {
                                        type: 'Category',
                                        position: 'bottom',
                                        fields: ['name'],
                                        title: '统计类型'
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
                                    flex: 1,
                                    columns: [
                                        {
                                            text: '类别',
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
                }]
        });
        this.callParent(arguments);
    },
    createGroupField: function () {
        var me = this;
        var searchFieldCombo = Ext.create('Ext.form.ComboBox', {
            xtype: 'combo',
            name: 'searchField',
            displayField: 'fieldLabel',
            valueField: 'name',
            width: 150,
            itemId: 'searchField' + me.viewId,
            tpl: Ext.create('Ext.XTemplate', '<tpl for=".">', '<div class="x-boundlist-item" pageType="{pageType}" dbType={dbType}>{fieldLabel}</div>',
                '</tpl>'),
            store: Ext.create('Ext.data.Store', {
                listeners: {
                    load: function (o, records, successful, eOpts) {
                        if (records[0] != null && records[0] != 'undefined') {
                            searchFieldCombo.setValue(records[0].data.name);
                            //var btn = me.down('panel').down('button');
                            //btn.fireEvent('click', btn);
                            me.statisticSubmit();
                        }
                    }
                },
                proxy: {
                    type: 'ajax',
                    url: 'view/statisticGroupField?viewId=' + me.listDom.viewId,
                    reader: {
                        type: 'json',
                        root: 'fields'
                    }
                },
                fields: ['fieldLabel', 'name', 'pageType', 'dbType'],
                autoLoad: true
            }),
            listeners: {
                select: me.statisticSubmit,
                scope: me
            }
        });
        return searchFieldCombo;
    },
    statisticSubmit: function () {
        var me = this;
        var combo = me.down('panel').down('combo');
        if (combo.value == '' || combo.value == null) {
            return;
        }
        me.down('tabpanel').items.each(function (tab) {
            if (tab.itemId == 'column' || tab.itemId == 'line') {
                tab.axes.items[1].title = combo.rawValue;
            }
        });
        var listStore = me.listDom.store;
        Ext.Ajax.request({
            url: listStore.getProxy().api['read'],
            params: {
                isStatistic: 1,
                groupField: combo.value,
                searchCondition: listStore.proxy.extraParams.searchCondition
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
                success = false;
            }
        });
    }
});