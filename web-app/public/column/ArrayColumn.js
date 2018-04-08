/**
 * Created by like on 2015/8/19.
 */
Ext.define('public.column.ArrayColumn', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.arraycolumn',
    initComponent: function() {
        var me = this,
            dataIndex=me.dataIndex;
        me.renderer = function(value, metadata, record, rowIndex, columnIndex, store) {
            if(dataIndex.indexOf('.') == -1){
                return record.get(dataIndex);
            }
            var relate = dataIndex.split('.')[0],
                fieldName = dataIndex.split('.')[1],
                data = record.get(relate),
                result='';
            if(data == ""){
                return "";
            }
            Ext.Array.each(data,function(obj, index, itSelf){
                if(result != '') {
                    result += ',';
                }
                result += obj[fieldName];
            });
            return result;
        }
        me.callParent(arguments);
    }
});