Ext.define("user.view.saleChance.View",{
    extend: 'public.BaseWin',
    alias: 'widget.saleChanceView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
            	readOnly:true
            }, 
            viewId:'SaleChanceView',
            buttons:[{
 	             text:'关闭',iconCls:'cancel',
 	             handler:function(btn){
 	            	btn.up('window').close();
 	        	 }
	         }]
        }
    ]
});
