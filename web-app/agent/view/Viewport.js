Ext.define('agent.view.Viewport', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.agentviewport',
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
        margin: '2 0 0 3',
        width: 200
    },
    {
        xtype: 'tabpanel',
        id: 'tabPanel',
        stateEvents: ['tabchange'],
        margins:'5 5 5 0',
        region: 'center',
        items: [{
            xtype:'panel',
            title:'工作台',
            height: 390,
            width : 300
        }]
    },{
        region: 'south',
        xtype: 'pageFooter'
    }]
});