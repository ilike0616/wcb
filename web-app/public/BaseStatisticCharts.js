Ext.define("public.BaseStatisticCharts", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.baseStatisticCharts',
    layout: 'border',
    initComponent: function () {
        var me=this,
            myStore = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data'],
            data: []
        });
        me.loadData(myStore,[]);
        Ext.apply(this, {
            store:myStore,
            items: [{
                xtype: 'tabpanel',
                region: 'center',
                tabBar: {
                    padding: '3 5 0 0',
                    items: [
                        {xtype: 'tbfill'},
                        {
                            xtype: 'combo',
                            name: 'searchFieldComboValue',
                            width: 120,
                            store: [['TODAY', '今天'], ['THIS_WEEK', '本周'], ['THIS_MONTH', '本月'], ['THIS_YEAR', '今年'],
                                ['YESTERDAY', '昨天'], ['LAST_WEEK', '上周'],
                                ['LAST_MONTH', '上月'], ['LAST_YEAR', '去年'], ['CUSTOM', '自定义时间']],
                            autoSelect: true,
                            forceSelection: true,
                            typeAhead: true,
                            emptyText: '-- 请选择 --',
                            listeners: {scope: me, change: me.statisticByDate}
                        },
                        {
                            xtype: 'datefield',
                            name: 'searchFieldStartDateValue',
                            fieldLabel: '从',
                            hidden: true,
                            labelWidth: 20,
                            width: 130,
                            format: 'Y-m-d',
                            submitFormat: 'y-m-d',
                            listeners:{scope:me,select:me.statisticByCustomDate}
                        },
                        {
                            xtype: 'datefield',
                            name: 'searchFieldEndDateValue',
                            fieldLabel: '到',
                            hidden: true,
                            labelWidth: 20,
                            width: 130,
                            format: 'Y-m-d',
                            submitFormat: 'y-m-d',
                            listeners:{scope:me,select:me.statisticByCustomDate}
                        }
                    ]
                },
                items: [
                    {
                        title: '饼图',
                        xtype: 'panel',
                        layout: {
                            type: 'hbox',       // Arrange child items vertically
                            align: 'stretch',    // Each takes up full width
                            padding: 5
                        },
                        items: [{
                            xtype: 'chart',
                            flex: 5,
                            animate: true,
                            store: myStore,
                            shadow: true,
                            itemId: 'pie',
                            /*legend: {
                                position: 'right'
                            },*/
                            insetPadding: 20,
                            series: [{
                                type: 'pie',
                                field: 'data',
                                //lengthField: 'data',
                                showInLegend: true, // 是否显示图例
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
                            flex: 2,
                            columns: [
                                {
                                    text: '员工',
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
                    }, {
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
                                fields: ['name']
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
                                    text: '员工',
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
    loadData:function(myStore,searchCondition){
        var me = this;
        myStore.removeAll();
        var url = 'base/statisticEmployeeSomeObjectRanking'
        if (Ext.typeOf(me.url) != 'undefined' && me.url != null) {
            url = me.url;
        }
        Ext.Ajax.request({
            url: url,
            params: {
                moduleId: me.moduleId,
                statField: me.statField,
                statType: me.statType,
                searchCondition: "[" + searchCondition + "]"
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
    },
    statisticByDate: function (o, newValue, oldValue, eOpts) {
        var me = this;
        var objList = o.up(me.alias[0].replace("widget.", ""));
        var comboField = objList.down("combo[name=searchField]");
        var startDateField = objList.down("datefield[name=searchFieldStartDateValue]");
        var endDateField = objList.down("datefield[name=searchFieldEndDateValue]");
        if (newValue == 'CUSTOM') {
            startDateField.reset();
            endDateField.reset();
            startDateField.show();
            endDateField.show();
        } else {
            startDateField.reset();
            endDateField.reset();
            startDateField.hide();
            endDateField.hide();
        }

        if (newValue != 'CUSTOM' && newValue != null) {
            me.search(o);
        }
    },
    statisticByCustomDate:function(o, newValue, oldValue, eOpts ){
        var me = this,
            objList = o.up(me.alias[0].replace("widget.", "")),
            startDateField = objList.down("datefield[name=searchFieldStartDateValue]").getValue(),
            endDateField = objList.down("datefield[name=searchFieldEndDateValue]").getValue();
        if(startDateField != null && endDateField != null){
            me.search(o);
        }
    },
    search: function (btn) {
        var me = this;
        var objList = btn.up(me.alias[0].replace("widget.", ""));
        var searchFieldValueValue = objList.down("combo[name=searchFieldComboValue]").getValue();
        var startDateFieldValue = "";
        var endDateFieldValue = "";
        if (searchFieldValueValue == 'CUSTOM') {
            startDateFieldValue = objList.down("datefield[name=searchFieldStartDateValue]").getValue();
            endDateFieldValue = objList.down("datefield[name=searchFieldEndDateValue]").getValue();
            if (startDateFieldValue != null && startDateFieldValue != "") {
                startDateFieldValue = Ext.util.Format.date(startDateFieldValue, 'Y-m-d H:m:s');
            } else {
                alert("请选择开始时间！");
                return;
            }
            if (endDateFieldValue != null && endDateFieldValue != "") {
                endDateFieldValue = Ext.util.Format.date(endDateFieldValue, 'Y-m-d H:m:s');
            } else {
                alert("请选择结束时间！");
                return;
            }
        }

        var searchCondition = [];
        if (searchFieldValueValue != "") {
            searchCondition.push(Ext.JSON.encode(searchFieldValueValue + "#@#" +  startDateFieldValue + "#@#" + endDateFieldValue));
            searchCondition.join(",");
        } else {
            searchCondition = [];
        }
        me.loadData(objList.store,searchCondition);
    }
});