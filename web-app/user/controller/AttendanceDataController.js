/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.controller.AttendanceDataController',{
    extend : 'Ext.app.Controller',
    views:['attendanceData.List','attendanceData.Map'] ,
    stores:['AttendanceDataStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
            'attendanceDataMap': {
                afterrender:function(component, eOpts){
                    var dataObj = component.listDom.getSelectionModel().getSelection()[0];
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
        var signLocation = dataObj.get('signLocation');
        var signOutLocation = dataObj.get('signOutLocation');
        if((signLocation != null && signLocation != "") || (signOutLocation != null && signOutLocation != "")){
            var con1 = "<div style='margin:0;line-height:20px;padding:2px;'>";
            var con2 = "所有者："+dataObj.get("employee.name")+"<br>" ;
            var con3 = "位置："+signLocation+"<br>" ;
            var con4 = "</div>";
            var content = con1+con2+con3+con4;
            var x = dataObj.get("signLongtitude");
            var y = dataObj.get("signLatitude");
            addMarker(map,x,y,content);

            var con1 = "<div style='margin:0;line-height:20px;padding:2px;'>";
            var con2 = "所有者："+dataObj.get("employee.name")+"<br>" ;
            var con3 = "位置："+signOutLocation+"<br>" ;
            var con4 = "</div>";
            var content = con1+con2+con3+con4;
            var x = dataObj.get("signOutLongtitude");
            var y = dataObj.get("signOutLatitude");
            addMarker(map,x,y,content);
        }
        if(signLocation == null || signLocation == "" || signOutLocation == null || signOutLocation == ""){
            // 获取当前位置
            var localCity = new BMap.LocalCity();
            localCity.get(function(result){
                var cityName = result.name;
                map.setCenter(cityName);
            });
        }
        function addMarker(map,x,y,content){
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
            }
        }
    }
})
