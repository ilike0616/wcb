/**
 * Created by like on 2015-04-08.
 */
Ext.define("user.view.saleChance.LocusMap",{
    extend: 'Ext.window.Window',
    alias: 'widget.saleChanceLocusMap',
    modal: true,
    width: 900,
    height: 500,
    resizable:true,
    title: '地图',
    initComponent: function() {
        Ext.applyIf(this, {
            html:'加载地图'
        });
        this.callParent(arguments);
    },
    initPathMap : function(div, dataObj) {
        var me = this;
        var map = new BMap.Map(div); // 创建Map实例
        map.enableScrollWheelZoom(); // 启用滚轮放大缩小，默认禁用
        map.addControl(new BMap.OverviewMapControl());
        map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
        map.enableContinuousZoom();
        map.clearOverlays();
        var point = new BMap.Point(109.414446,33.719004);
        map.centerAndZoom(point,5);
        Ext.Array.each(dataObj, function(data, index) {
            me.addMark(map,data.longtitude,data.latitude,data.subject,data.location);
        });
    },
    // 添加标记和文本
    addMark : function(map, x, y, name, address) {
        var content = "<div style='margin:0;line-height:20px;padding:2px;'>" + name + "<br/>地址：" + address + "</div>";
        var poi = new BMap.Point(x, y);
        var marke;
        marke = new BMap.Marker(poi);
        map.addOverlay(marke);
        var infoWindow = new BMapLib.SearchInfoWindow(map, content, {
            title : "位置信息", // 标题
            width : 250, // 宽度
            height : 80, // 高度
            panel : "panel", // 检索结果面板
            enableAutoPan : true, // 自动平移
            enableSendToPhone : false,
            searchTypes : []
        }); // 创建信息窗口对象
        marke.addEventListener("onmouseover", function() {
            infoWindow.open(this);
        });
    }
})