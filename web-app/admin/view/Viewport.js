Ext.define('admin.view.Viewport', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.adminviewport',
    layout: 'border',
    defaults:{
        split:true,
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
        width: 200
    },
        {
            xtype: 'tabpanel',
            id: 'tabPanel',
            stateEvents: ['tabchange'],
            region: 'center',
            items: [{
                xtype:'panel',
                title:'工作台',
                height: 390,
                width : 300
            }]
        }
     ,{
            region: 'south',
            xtype: 'pageFooter'
        }
    ]
});