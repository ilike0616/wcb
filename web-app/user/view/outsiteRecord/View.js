Ext.define("user.view.outsiteRecord.View",{
    extend: 'public.BaseWin',
    alias: 'widget.outsiteRecordView',
    requires: [
        'public.BaseForm'
    ],
    title: '查看现场记录',
    items: [
        {
            xtype: 'baseForm',
            defaults:{
            	readOnly:true
            }, 
            viewId:'OutsiteRecordView',
            buttons:[{
	             text:'关闭',iconCls:'cancel',
	             handler:function(btn){
	            	btn.up('window').close();
	        	 }
	         }]
        }
    ]
})