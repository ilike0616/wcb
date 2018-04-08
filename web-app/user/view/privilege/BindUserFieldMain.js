/**
 * Created by guozhen on 2014/12/19.
 */
Ext.define('user.view.privilege.BindUserFieldMain', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.bindUserFieldMain',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'moduleList',
        title:'',
        flex: 1
    },{
        xtype: 'moduleViewList',
        flex: 1
    },{
        xtype: 'bindUserFieldList',
        flex: 2
    }]
});