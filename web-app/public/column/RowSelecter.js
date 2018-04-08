Ext.define('public.column.RowSelecter', {
    extend: 'Ext.grid.column.Column',

    alias: 'widget.rowselecter',
    initComponent: function() {
        var me = this,
        arry=me.arry;
        me.renderer = function(value, metadata, record, rowIndex, columnIndex, store) {
        	 for(var i=0;i<arry.length;i++){
        	 	if(arry[i][0]==value){
        	 		return arry[i][1];
        	 	}
        	 }
        	return "";
        }
        me.callParent(arguments);
    }
});