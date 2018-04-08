Ext.define("user.view.aimPerformance.Chart", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.aimPerformanceChart',
    layout: 'fit',
    initComponent: function () {
        var me=this,
            myStore = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data1' ,'data2'],
            data: []
        });
        myStore.removeAll();
        Ext.apply(this, {
            store:myStore,
            items: [{
                xtype: 'chart',
                itemId: 'column',
                animate: true,
                store: myStore,
                axes: [{
                    type: 'Numeric',
                    position: 'left',
                    fields: ['data1', 'data2'],
                    minimum: 0,
                    grid: true
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
                        width: 200,
                        height: 28,
                        renderer: function (storeItem, item) {
                            var title = "";
                            if(item.yField == 'data1'){
                                title = storeItem.get('name')+"   目标："+storeItem.get('data1');
                            }else{
                                title = storeItem.get('name')+"   完成："+storeItem.get('data2');
                            }
                            this.setTitle(title);
                        }
                    },
                    label: {
                        display: 'insideEnd',
                        'text-anchor': 'middle',
                        field: ['data1', 'data2'],
                        renderer: Ext.util.Format.numberRenderer('0'),
                        orientation: 'vertical',
                        color: '#333'
                    },
                    xField: 'name',
                    yField: ['data1', 'data2']
                }]
            }]
        });
        this.callParent(arguments);
    },
    loadData:function(record,aimYear){
        var me = this;
        var myStore = me.store;
        myStore.removeAll();
        var currentDate = new Date();
        if(aimYear != null && aimYear != "") currentDate.setFullYear(aimYear);
        var num = 0;
        var yearMonth = "";
        var data = [];
        for(var i=0;i<12;i++){
            num = i + 1;
            currentDate.setMonth(i);
            yearMonth = Ext.Date.format(currentDate, 'Y-m');
            data.push({
                name: yearMonth,
                data1: record?record.get('aim_'+num):0,
                data2: record?record.get('aimComplete_'+num):0
            })
        }
        myStore.loadData(data);
    }
});