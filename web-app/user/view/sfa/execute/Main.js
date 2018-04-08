/**
 * Created by like on 2015/9/17.
 */
Ext.define('user.view.sfa.execute.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.sfaExecuteMain',
    layout: 'border',
    title: 'SFA日志',
    title1: 'SFA日志',
    initValues: [],
    items: [{
        region: 'west',
        width: '20%',
        collapsible: true,
        layout: 'fit',
        title: '方案',
        xtype: 'sfaExecuteSfaList'
    }, {
        region: 'center',
        layout: 'fit',
        title: '事件',
        xtype: 'sfaExecuteEventList'
    }, {
        region: 'east',
        width: '40%',
        title: '执行明细',
        collapsible: true,
        xtype: 'sfaExecuteEventExecuteList',
        layout: 'fit'
    }
    ]
});