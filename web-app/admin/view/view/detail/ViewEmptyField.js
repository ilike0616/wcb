/**
 * Created by guozhen on 2015-11-19.
 * 注意：该类没有实际意义
 *
 */
Ext.define("admin.view.view.detail.ViewEmptyField",{
    extend: 'Ext.panel.Panel',
    alias: 'widget.viewEmptyField',
    title: '空字段',
    layout : 'border',
    items: [{
        xtype: 'form',
        width:230,
        overflowY:'auto',
        items: []
    },{
        region: 'center',
        xtype : 'panel',
        html:'<div style="margin-top: 30%;margin-left: 30%;">请选择自定义明细记录！</div>'
    }]
})