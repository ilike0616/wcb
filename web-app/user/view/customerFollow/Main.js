/**
 * Created by like on 2015/9/21.
 */
Ext.define('user.view.customerFollow.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.customerFollowMain',
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
            xtype: 'customerFollowList',
            store:Ext.create('user.store.CustomerFollowStore'),
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
                    xtype: 'sfaExecuteMain'
                }
            ]
        }
    ]
});