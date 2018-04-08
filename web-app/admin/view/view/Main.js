/**
 * Created by shqv on 2014-8-27.
 */
Ext.define('admin.view.view.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewMain',
    layout: 'border',
    items: [
        {
            region:'west',
            title:'模块',
            xtype: 'moduleList',
            width: 300,
            collapsible: true,
            layout: 'fit'
        },
        {
            region:'center',
            xtype: 'viewList'
        }
    ]
});