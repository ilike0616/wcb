Ext.define("user.view.taskAssigned.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.taskAssignedEdit',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
    	 var me = this;
         Ext.applyIf(me, {
             items: [
				{
				    xtype: 'baseForm',
				    viewId:'TaskAssignedEdit',
				    buttons: [{
				        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'taskAssignedMain taskAssignedList[taskType=1]'
			        }]	
			    }
			]
        });
        me.callParent(arguments);
    }
});
