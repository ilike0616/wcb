/**
 * Created by like on 2015-04-14.
 */
Ext.define('user.view.notifyModel.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.notifyModelMain',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'moduleList',
        title:'',
        flex: 1
    },{
        xtype: 'notifyModelList',
        flex: 4
    }]
});