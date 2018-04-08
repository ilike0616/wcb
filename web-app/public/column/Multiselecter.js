Ext.define('public.column.Multiselecter', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.multiselecter',
    initComponent: function() {
        var me = this,
        arry=me.arry;
        me.renderer = function(value, metadata, record, rowIndex, columnIndex, store) {
        	 var v = "";
        	 var vs = value.split(',');
        	 for(var i=0;i<arry.length;i++){
        	 	if(Ext.Array.contains(vs,arry[i][0])){
        	 		if(v=="") v += arry[i][1];
        	 		else v += ","+arry[i][1];
        	 	}
        	 }
        	return v;
        }
        me.callParent(arguments);
    }
});