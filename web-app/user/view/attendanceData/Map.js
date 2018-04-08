/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define("user.view.attendanceData.Map",{
    extend: 'Ext.window.Window',
    alias: 'widget.attendanceDataMap',
    modal: true,
    width: 700,
    height: 500,
    title: '地图',
    initComponent: function() {
        Ext.applyIf(this, {
            html:'加载地图'
        });
        this.callParent(arguments);
    }
})