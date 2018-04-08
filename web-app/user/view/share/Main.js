Ext.define('user.view.share.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.shareMain',
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
            xtype: 'shareList',
            store:Ext.create('user.store.ShareStore'),
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
    				xtype: 'shareDetailList',
    				store:Ext.create('user.store.ShareDetailStore')
        	    }
        	]
        }
    ]
});