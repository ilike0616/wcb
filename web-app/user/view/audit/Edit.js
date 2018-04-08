Ext.define("user.view.audit.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.auditEdit',
    requires: [
    	//'public.BaseComboBoxTree',
        //'public.BaseForm'
    ],
    modal: true,
    width: 800,
    height:600,
    minHeight:500,
    layout: 'border',
    title: '审核申请',
    buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:false,target:'auditList'
            }],
    initComponent: function() {
    	  var me = this,items;
    	  var audit = {
            	region: 'center',
                title: '审核意见',
                xtype: 'auditOpinionEdit',
                changeWinTitle:false,
                itemId: 'auditOpinionEdit'
        	};
          var auditOpinionList = {
	            region: 'south',
	            title: '已审核意见',
	            xtype: 'auditOpinionList',
	            itemId: 'auditOpinionList',
	            height: 200,
	            split: true,
	            collapsible: true,
	            enableToolbar:false,
	            floatable: true
	       };
	       var applyView = {
	            region: 'east',
	            title: '申请单',
	            xtype: 'auditOpinionViewApply',
	            itemId: 'auditOpinionViewApply',
	            viewId: me.viewId,
	            changeWinTitle:false,
	            width: 400,
	            split: true,
	            collapsible: true,
	            floatable: false
	        };
    	  items =  [applyView,audit,auditOpinionList]
    	  Ext.applyIf(me, { 
           	 items : items
          });
          me.callParent(arguments);
    }
    
});
