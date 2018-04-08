Ext.define('admin.view.model.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.modelMain',
    layout: 'border',
    items: [
        {
            region:'west',
            width: 400,
            title:'模块',
            xtype: 'modelList',
            dockedItems: [],
            collapsible: true
        },
        {
            region:'center',
            xtype: 'fieldList'
        }
    ]
});