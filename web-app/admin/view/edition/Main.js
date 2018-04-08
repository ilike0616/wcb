/**
 * Created by guozhen on 2015/4/2.
 */
Ext.define('admin.view.edition.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.editionMain',
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
            xtype: 'editionList',
            store: Ext.create('admin.store.EditionStore'),
            region: 'center',
            flex : 1 ,
            split: true,
            floatable: true
        },{
            xtype:'tabpanel',
            hidden:true,
            activeIndex:0,
            region: 'south',
            flex : 1,
            split: true,
            floatable: true,
            items:[{
                    xtype: 'userList',
                    store:Ext.create('admin.store.UserStore')
                }
            ]
        }
    ]
});