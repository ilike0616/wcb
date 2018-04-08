/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.view.employeeNotifyModel.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeNotifyModelMain',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'moduleList',
        title: '',
        flex: 1
    }, {
        flex: 4,
        region:'center',
        xtype: 'tabpanel',
        tabPosition: 'top',
        items:[{
            title: '员工',
            xtype:'employeeNotifyModelList'
        },{
            title: '系统',
            xtype:'employeeNotifyModelForbidList'
        }]
    }]
});