Ext.define("user.view.bulletin.View",{
    extend: 'public.BaseWin',
    alias: 'widget.bulletinView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
            	readOnly:true
            }, 
            viewId:'BulletinView',
            buttons:[{
  	             text:'关闭',iconCls:'cancel',
  	             handler:function(btn){
  	            	btn.up('window').close();
  	        	 }
	         }]
        }
    ]
})