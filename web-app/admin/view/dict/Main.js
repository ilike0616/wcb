/**
 * Created by shqv on 2014-9-13.
 */
Ext.define('admin.view.dict.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.dictMain',
    layout: 'border',
    items: [
        {
            region:'center',
            store : Ext.create('admin.store.DataDictStore'),
            xtype: 'dictList'
        },
        {
            width:450,
            region:'east',
            xtype: 'dictItemList'
        }
    ]
});