/**
 * Created by shqv on 2014-9-13.
 */
Ext.define('user.view.dict.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.dictMain',
    layout: 'border',
    items: [
        {
            width:750,
            region:'east',
            xtype: 'dictItemList'
        },
        {
            region:'center',
            xtype: 'dictList'
        }
    ]
});