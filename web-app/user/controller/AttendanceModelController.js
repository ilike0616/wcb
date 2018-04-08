/**
 * Created by guozhen on 2015/04/10.
 */
Ext.define('user.controller.AttendanceModelController',{
    extend : 'Ext.app.Controller',
    views:['attendanceModel.Main','attendanceModel.List','attendanceModel.Add','attendanceModel.Edit','attendanceModel.View'
        ,'attendanceModel.Map','attendanceModel.BindEmployee'] ,
    stores:['AttendanceModelStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.application.getController("EmployeeController");
        this.control({
            'attendanceModelMain attendanceModelList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    var tabpanel = grid.up('attendanceModelMain').down('tabpanel');
                    if(tabpanel.hidden){
                        var employee = tabpanel.down('employeeList');
                        if(employee){
                            var store = employee.getStore();
                            Ext.apply(store.proxy.extraParams,{attendanceModel:record.get('id')});
                            employee.initValues = [{id:'attendanceModel.id',value:record.get('id')},{id:'attendanceModel.name',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                employee.setTitle(employee.title1+"("+this.getTotalCount()+")");
                            });
                        }
                        tabpanel.show();
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = eOpts.up('attendanceModelMain').down('tabpanel');
                    eOpts.up('baseList').initValues = [{id:'attendanceModel.id',value:record.get('id')},{id:'attendanceModel.name',value:record.get('name')}];
                    if(tabpanel.hidden==false){
                        var employee = tabpanel.down('employeeList');
                        if(employee){
                            var store = employee.getStore();
                            Ext.apply(store.proxy.extraParams,{attendanceModel:record.get('id')});
                            employee.initValues = [{id:'attendanceModel.id',value:record.get('id')},{id:'attendanceModel.name',value:record.get('name')}];
                            store.load(function(records){
                                employee.setTitle(employee.title1+"("+records.length+")");
                            });
                        }
                    }
                }
            },
            'attendanceModelMain attendanceModelList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    if(e.getKey()==e.Q||e.getKey()==e.TAB){
                        var tab = view.up('attendanceModelMain').down('tabpanel');
                        if(tab.hidden==false){
                            tab.setActiveTab(1);
                            var activeIndex = tab.activeIndex+1;
                            if(tab.items.getCount()==activeIndex){
                                activeIndex = 0;
                            }
                            tab.activeIndex = activeIndex;
                            tab.setActiveTab(activeIndex);
                        }else{
                            tab.activeIndex = 0;
                            tab.setActiveTab(0);
                            tab.show();
                        }
                    }else if(e.getKey()==e.ESC){
                        view.up('attendanceModelMain').down('tabpanel').hide();
                    }
                }
            },
            'fieldcontainer#locationContainer button#locationButton':{
                click:function(o, e, eOpts ){
                    var view = o.up('window');
                    var mapView = Ext.widget('attendanceModelMap',{
                        parentView : view
                    });
                    mapView.show();
                }
            },
            'attendanceModelMap': {
                afterrender:function(component, eOpts){
                    var address = null;
                    var addressObj = component.parentView.down('fieldcontainer textfield[name=location]');
                    if(addressObj != null && addressObj != 'undefined') address = addressObj.getValue();
                    var mapPanel = component.down('form panel');
                    this.initMap(component,mapPanel.el.dom,address,1); //初始化地图
                }
            },
            'attendanceModelMap form fieldcontainer button#mapLocationButton':{
                click:function(o,e,eOpts){
                    var view = o.up('attendanceModelMap');
                    var address = view.down('textfield[name=location]').getValue();
                    if(address == ""){
                        alert("请输入要定位的地点！");
                        return;
                    }
                    var mapPanel = view.down('form panel');
                    this.initMap(view,mapPanel.el.dom,address,2);
                }
            }
        });
    },
    initMap:function(view,div,address,type){
        var map = new BMap.Map(div);
        var point = new BMap.Point(116.3972,39.9096);
        map.centerAndZoom(point, 12);
        map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
        map.addControl(new BMap.OverviewMapControl());
        map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
        map.enableContinuousZoom();
        // 获取当前位置
        var localCity = new BMap.LocalCity();
        localCity.get(function(result){
            var cityName = result.name;
            map.setCenter(cityName);
        });
        var parentView = view.parentView;
        // 搜当前查询的地址
        if(address != null && address != ""){
            if(type == 1){
                this.addMark(map,parentView);
            }else{
                this.muhuLocation(map,address);
            }
        }

        var geoc = new BMap.Geocoder();//地址解析类
        map.addEventListener("click", function(e) {
            var pt = e.point;
            parentView.down('hiddenfield[name=longtitude]').setValue(pt.lng);
            parentView.down('hiddenfield[name=latitude]').setValue(pt.lat);
            geoc.getLocation(pt, function(rs) {
                var addComp = rs.addressComponents;
                var opts = {
                    width: 250,     //信息窗口宽度
                    height: 100,     //信息窗口高度
                    title: '当前位置：'
                }
                var addr = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                map.clearOverlays();
                map.addOverlay(new BMap.Marker(pt)); //如果地址解析成功，则添加红色marker
                var infoWindow = new BMap.InfoWindow(addr, opts);  //创建信息窗口对象
                map.openInfoWindow(infoWindow, pt);
                parentView.down('textfield[name=location]').setValue(addr);
            });
            Ext.example.msg('提示', '您已成功定位到当前位置！');
        });
    },
    // 添加标记和文本
    addMark : function(map,parentView) {
        var address = parentView.down('textfield[name=location]').getValue();
        var longtitude = parentView.down('hiddenfield[name=longtitude]').getValue();
        var latitude = parentView.down('hiddenfield[name=latitude]').getValue();
        if(longtitude != null && longtitude != "" && latitude != null && latitude != ""){
            var point = new BMap.Point(longtitude,latitude);
            map.clearOverlays();
            map.addOverlay(new BMap.Marker(point)); //如果地址解析成功，则添加红色marker
            map.centerAndZoom(point,14);
            var opts = {
                width: 250,     //信息窗口宽度
                height: 100,     //信息窗口高度
                title: '当前位置：'
            }
            var infoWindow = new BMap.InfoWindow(address, opts);  //创建信息窗口对象
            map.openInfoWindow(infoWindow, point);
        }else{
            this.muhuLocation(map,address);
        }
    },
    muhuLocation:function(map,address){
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search(address);
    }
})
