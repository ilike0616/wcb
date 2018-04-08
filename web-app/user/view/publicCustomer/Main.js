Ext.define('user.view.publicCustomer.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.publicCustomerMain',
    layout : {
        type: 'border',
        pack : 'start ',
        align: 'stretch'
    },
    defaults:{
        split:true,
        border: false
    },
    items: [
        {
            xtype: 'publicCustomerList',
            store:Ext.create('user.store.PublicCustomerStore'),
            region: 'center',
            flex : 1 ,
            split: true,
            floatable: true
        },
        {
        	xtype:'tabpanel',
        	hidden:true,
        	activeIndex:0,
        	region: 'south',
        	flex : 1,
        	split: true,
            floatable: true,
        	items:[
        	   {
    				xtype: 'contactList',
    				store:Ext.create('user.store.ContactStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	   },{
                    xtype: 'sfaExecuteMain'
                }
        	]
        } 
    ]
});