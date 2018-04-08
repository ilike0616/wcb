Ext.define("user.view.planSummary.View",{
    extend: 'public.BaseWin',
    alias: 'widget.planSummaryView',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
    	 var me = this;
         Ext.applyIf(me, {
             items: [
				{
				    xtype: 'baseForm',
				    viewId:me.viewId,
				    buttons:[{
		 	             text:'关闭',iconCls:'cancel',
		 	             handler:function(btn){
		 	            	btn.up('window').close();
		 	        	 }
			         }]
			    }
			]
        });
        me.callParent(arguments);
    }
});
