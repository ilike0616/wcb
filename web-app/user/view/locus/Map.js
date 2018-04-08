Ext.define("user.view.locus.Map",{
    extend: 'Ext.window.Window',
    alias: 'widget.locusMap',
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