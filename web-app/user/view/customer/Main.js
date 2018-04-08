Ext.define('user.view.customer.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.customerMain',
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
            xtype: 'customerList',
            store:Ext.create('user.store.CustomerStore'),
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
    				xtype: 'customerFollowList',
    				store:Ext.create('user.store.CustomerFollowStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    },{
    				xtype: 'contactList',
    				store:Ext.create('user.store.ContactStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    },{ 
    				xtype: 'contractOrderList',
    				store:Ext.create('user.store.ContractOrderStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    },
                //{
    				//xtype: 'serviceTaskList',
    				//store:Ext.create('user.store.ServiceTaskStore'),
    			 //   enableSearchField:false,//false 关闭搜索框
    			 //   enableComplexQuery:false//false 关闭查询功能
        	    //},
				{
					xtype: 'sfaExecuteMain'
				},{
    				xtype: 'noteList',
    				store:Ext.create('user.store.NoteStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    }
        	]
        } 
    ]
});