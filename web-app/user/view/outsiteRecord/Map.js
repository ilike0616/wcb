Ext.define("user.view.onsiteObject.Map",{
    extend: 'Ext.window.Window',
    alias: 'widget.onsiteObjectMap',
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