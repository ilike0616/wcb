Ext.define('user.view.Viewport', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.mainviewport',
    layout: 'border',
    defaults:{
        border: false
    },
    items:[
        {
            region: 'north',
            xtype: 'pageHeader'
        },
        {
            region: 'west',
            xtype: 'menuTree',
            collapsible: true,
            split:true,
            width: 200
        },
        {
            xtype: 'tabpanel',
            id: 'tabPanel',
            name:'navigationTabPanel',
            region: 'center',
            items: [],
            plugins:Ext.create('public.TabCloseMenu', {
                closeTabText: '关闭当前',
                closeOthersTabsText: '关闭其他',
                closeAllTabsText: '关闭所有'
            }),
            listeners: {
                afterrender:function(tab){
                    tab.add({
                        xtype:'workBench',
                        title:'工作台'
                    });
                }
            }
        },
        {
            region: 'south',
            xtype: 'pageFooter'
        }
    ]
});