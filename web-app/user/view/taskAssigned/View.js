Ext.define("user.view.taskAssigned.View",{
    extend: 'public.BaseWin',
    alias: 'widget.taskAssignedView',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
    	 var me = this;
         Ext.applyIf(me, {
             items: [
				{
				    xtype: 'baseForm',
				    viewId:'TaskAssignedView',
					defaults:{
						readOnly:true
					},
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
