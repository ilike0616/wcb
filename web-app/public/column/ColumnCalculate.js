Ext.define('public.column.ColumnCalculate', {
    extend: 'Ext.grid.column.Column',

    alias: 'widget.columnCalculate',
    initComponent: function() {
        var me = this,
        calType=me.calType;
        me.renderer = function(value, metadata, record, rowIndex, columnIndex, store) {
        	if(calType == 1){ // 当前日期-value日期值
                if(value != null && value != ""){
                    var valueDate = Ext.Date.format(new Date(value),'Y-m-d');
                    var currentDate = Ext.Date.format(new Date(),'Y-m-d');
                    var mill = new Date(currentDate) - new Date(valueDate);
                    var diffDay = mill / 1000 / 3600 / 24;
                    return diffDay;
                }
            }
        	return "";
        }
        me.callParent(arguments);
    }
});