/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.OutsiteRecordController',{
    extend : 'Ext.app.Controller',
    views:['outsiteRecord.List','outsiteRecord.Map','outsiteRecord.View'] ,
    stores:['OutsiteRecordStore'],
    models:['OutsiteRecordModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'outsiteRecordList',
            selector : 'outsiteRecordList'
        }
    ],
    init:function() {
        this.control({
            'onsiteObjectMap': {
                afterrender:function(component, eOpts){
                    var dataObj = this.getOutsiteRecordList().getSelectionModel().getSelection()[0];
                    this.initMap(component.el.dom,dataObj); //初始化地图
                }
            }
        });
    },
    initMap:function(div,dataObj){
        var map = new BMap.Map(div);
        var point = new BMap.Point(116.3972,39.9096);
        map.centerAndZoom(point, 12);
        map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
        map.addControl(new BMap.OverviewMapControl());
        map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
        map.enableContinuousZoom();

        var content = "";
        var finalObj = dataObj.get("customerFollow.subject");
        if(finalObj == ""){
            finalObj = dataObj.get("objectFollow.subject");
        }
        var con1 = "<div style='margin:0;line-height:20px;padding:2px;'>";
        var con2 = "对象："+finalObj+"<br>" ;
        var con3 = "</div>";
        content = con1+con2+con3;
        var x = dataObj.get("longtitude");
        var y = dataObj.get("latitude");
        if(x != "" && y != ""){
            var poi=new BMap.Point(x, y);
            map.centerAndZoom(poi, 12);
            var marke = new BMap.Marker(poi);
            map.addOverlay(marke);
            var infoWindow = null;
            marke.addEventListener("onmouseover", function(){
                infoWindow = new BMapLib.SearchInfoWindow(map,content,{
                        title: "信息", //标题
                        width: 250, //宽度
                        height: 80, //高度
                        panel : "panel", //检索结果面板
                        enableAutoPan : true, //自动平移
                        enableSendToPhone : false,
                        searchTypes :[]
                    }
                );  // 创建信息窗口对象
                infoWindow.open(this);
            });
        }else{
            // 获取当前位置
            var localCity = new BMap.LocalCity();
            localCity.get(function(result){
                var cityName = result.name;
                map.setCenter(cityName);
            });
        }
    }

})
