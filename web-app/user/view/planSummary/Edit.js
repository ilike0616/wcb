Ext.define("user.view.planSummary.Edit",{
    extend: 'public.BaseWin',
    alias: 'widget.planSummaryEdit',
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
				    buttons: [{
				        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'planSummaryMain planSummaryList[planSummaryType=1]'
			        }]	
			    }
			]
        });
        me.callParent(arguments);
    }
});
