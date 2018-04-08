Ext.define("user.view.planSummary.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.planSummaryAdd',
    requires: [
        'public.BaseForm'
    ],
    initComponent: function() {
    	 var me = this;
         Ext.applyIf(me, {
            items : [
		        {
		            xtype: 'baseForm',
		            viewId:me.viewId,
		            buttons: [{
		                text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'planSummaryMain planSummaryList[planSummaryType=1]'
		            }]
		        }
	    	]
        });
        me.callParent(arguments);
    }
});
