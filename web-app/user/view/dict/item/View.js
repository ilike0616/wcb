Ext.define("user.view.dict.item.View",{
    extend: 'public.BaseWin',
    alias: 'widget.dictItemView',
    requires: [
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
            	readOnly:true
            }, 
            viewId:'DictItemView',
            buttons:[{
 	             text:'关闭',iconCls:'cancel',
 	             handler:function(btn){
 	            	btn.up('window').close();
 	        	 }
	         }]
        }
    ]
});
