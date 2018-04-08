Ext.define('user.view.locus.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.locusMain',
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
            xtype: 'locusList',
            store:Ext.create('user.store.LocusStore'),
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
    				xtype: 'locusDetailList',
    				store:Ext.create('user.store.LocusDetailStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    }
        	]
        } 
    ]
});