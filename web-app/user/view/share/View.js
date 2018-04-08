Ext.define("user.view.share.View",{
    extend: 'public.BaseWin',
    alias: 'widget.shareView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
            	readOnly:true
            }, 
            viewId:'ShareView',
            buttons:[{
  	             text:'关闭',iconCls:'cancel',
  	             handler:function(btn){
  	            	btn.up('window').close();
  	        	 }
	         }]
        }
    ]
})