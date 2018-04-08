Ext.define('user.view.saleChance.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.saleChanceMain',
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
            xtype: 'saleChanceList',
            store:Ext.create('user.store.SaleChanceStore'),
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
    				xtype: 'saleChanceFollowList',
    				store:Ext.create('user.store.SaleChanceFollowStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    },
                {
                    xtype: 'sfaExecuteMain'
                }
        	]
        }
    ]
});