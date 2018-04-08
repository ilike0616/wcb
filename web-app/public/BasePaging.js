/**
 * Created by shqv on 2014-6-11.
 */
Ext.define('public.BasePaging', {
    extend: 'Ext.toolbar.Paging',
    alias: 'widget.basePaging',
    dock: 'bottom',
    displayInfo: true, 
    emptyMsg: "没有记录",
    items : [ {
        xtype : 'combo',
        name : 'pageSize',
        displayField : 'id',
        typeAhead : true,
        forceSelection : true,
        editable : true,
        width : 80,
        listeners: {
            afterrender:function(combo) {
                combo.setValue(combo.up().getStore().pageSize);
            },
            select:function(combo, records) {
                var combovalue = Number(combo.getValue());
                var store =  combo.up().getStore();
                store.pageSize = combovalue;
                store.load();
            }
        },
        store : Ext.create('Ext.data.ArrayStore', {
            fields : ['id'],//必须设置为id ，不可更改
            data : [[10],[15],[25],[30],[40],[50]]
        })
    }]
})
