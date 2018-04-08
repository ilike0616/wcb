Ext.define('user.controller.LocusController', {
	extend : 'Ext.app.Controller',
	views : [ 'locus.List', 'locus.Map', 'locus.Main', 'locus.DetailList', 'locus.PathMap' ],
	stores : [ 'LocusStore', 'LocusDetailStore' ],
	models : [ 'LocusDetailModel' ],
	GridDoActionUtil : Ext.create("admin.util.GridDoActionUtil"),
	refs : [ {
		ref : 'locusList',
		selector : 'locusList'
	} ],
	init : function() {
		this.control({
			'locusMain locusList' : {
				itemdblclick : function(grid, record, item, index, e, eOpts) {
					var tabpanel = grid.up('locusMain').down('tabpanel');
					if (tabpanel.hidden) {
						tabpanel.show();
						grid.fireEvent('select', grid.getSelectionModel(), record, index, grid);
					} else {
						tabpanel.hide();
					}
				},
				select : function(selectModel, record, index, eOpts) {
					var tabpanel = eOpts.up('locusMain').down('tabpanel');
					eOpts.up('baseList').initValues = [ {
						id : 'locus.id',
						value : record.get('id')
					} ];
					if (tabpanel.hidden == false) {
						var locusDetail = tabpanel.down('locusDetailList');
						if (locusDetail) {
							var store = locusDetail.getStore();
							Ext.apply(store.proxy.extraParams, {
								locus : record.get('id')
							});
							locusDetail.initValues = [ {
								id : 'locus.id',
								value : record.get('id')
							} ];
							store.load(function(records) {
								locusDetail.setTitle(locusDetail.title1 + "(" + records.length + ")");
							});
						}
					}
				}
			},
			'locusMap' : {
				afterrender : function(component, eOpts) {
					var dataObj = component.listDom.getSelectionModel().getSelection()[0];
					this.initMap(component.el.dom, dataObj); // 初始化地图
				}
			},
			'locusPathMap' : {
				afterrender : function(component, eOpts) {
					var record = this.getLocusList().getSelectionModel().getSelection()[0];
					var store = this.getLocusList().getStore();
					var dataObj = [];
					Ext.Ajax.request({
						url : store.getProxy().api['detail'],
						params : {
							locus : record.get('id')
						},
						method : 'POST',
						timeout : 4000,
						async : false,
						success : function(response, opts) {
							var rt = Ext.JSON.decode(response.responseText);
							dataObj = rt.data;
						},
						failure : function(response, opts) {
							var errorCode = "";
							if (response.status) {
								errorCode = 'error:' + response.status;
							}
							Ext.example.msg('提示', '操作失败！' + errorCode);
							success = false;
						}
					});
					this.initPathMap(component.el.dom, dataObj);
				}
			}
		});
	},
	initPathMap : function(div, dataObj) {
		var me = this;
		var map = new BMap.Map(div); // 创建Map实例
		map.enableScrollWheelZoom(); // 启用滚轮放大缩小，默认禁用
		map.addControl(new BMap.OverviewMapControl());
		map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
		map.enableContinuousZoom();
		map.clearOverlays();
		if(dataObj.length-1 <= 0){
 			var point = new BMap.Point(116.331398,39.897445);
 			map.centerAndZoom(point,12);
	 		function myFun(result){
 			    var cityName = result.name;
 			    map.setCenter(cityName);
 			}
 			var myCity = new BMap.LocalCity();
 			myCity.get(myFun);
 		}else{
	 		var point = new BMap.Point(dataObj[0].longtitude,dataObj[0].latitude);
	 		map.centerAndZoom(point,12);
 		}
		var lastpoint = null;
		Ext.Array.each(dataObj, function(data, index) {
			var firstOrLast = 0;
			if (index == dataObj.length - 1) {
				firstOrLast = 1;// 起点
			} else if (index == 0) {
				firstOrLast = 2;// 终点
			}
			var name = data.employee.name+":"+Ext.util.Format.date(data.locusDate,'Y-m-d H:i:s');
			me.addMark(map,data.longtitude,data.latitude,name,data.location,"0",firstOrLast);
			var nextpoint = new BMap.Point(data.longtitude,data.latitude);
		 	if(index!=0 && index!=dataObj.length ){
		 		polyline = new BMap.Polyline([lastpoint,nextpoint], {strokeColor:"blue", strokeWeight:6, strokeOpacity:0.5});
				map.addOverlay(polyline);
		 	}
		 	lastpoint=nextpoint;
		});
	},
	// 添加标记和文本
	addMark : function(map, x, y, name, address, type, firstOrLast) {
		var content = "<div style='margin:0;line-height:20px;padding:2px;'>" + name + "<br/>地址：" + address + "</div>";
		var poi = new BMap.Point(x, y);
		var marke;
		if (firstOrLast == 0) {
			if (type == "1") {
				var icon = new BMap.Icon('http://static.xiaoshouwuyou.com/ext/static/img/locus.png', new BMap.Size(20, 32), {
					anchor : new BMap.Size(10, 30)
				});
				marke = new BMap.Marker(poi, {
					icon : icon
				});
			} else {
				marke = new BMap.Marker(poi);
			}
		} else if (firstOrLast == 1) {
			var icon = new BMap.Icon('http://static.xiaoshouwuyou.com/ext/static/img/locus_start_pos.png', new BMap.Size(41, 37), {// 是引用图标的名字以及大小，注意大小要一样
				anchor : new BMap.Size(10, 30)
			// 这句表示图片相对于所加的点的位置
			});
			marke = new BMap.Marker(poi, {
				icon : icon
			});
		} else if (firstOrLast == 2) {
			var icon = new BMap.Icon('http://static.xiaoshouwuyou.com/ext/static/img/locus_end_pos.png', new BMap.Size(41, 37), {// 是引用图标的名字以及大小，注意大小要一样
				anchor : new BMap.Size(10, 30)
			// 这句表示图片相对于所加的点的位置
			});
			marke = new BMap.Marker(poi, {
				icon : icon
			});
		}
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
	},
	initMap : function(div, dataObj) {
		var map = new BMap.Map(div);
		var point = new BMap.Point(116.3972, 39.9096);
		map.centerAndZoom(point, 12);
		map.enableScrollWheelZoom(); // 启用滚轮放大缩小，默认禁用
		map.addControl(new BMap.OverviewMapControl());
		map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
		map.enableContinuousZoom();

		var content = "<div style='margin:0;line-height:20px;padding:2px;'>" + dataObj.get("employee.name") + ":" + Ext.util.Format.date(dataObj.get("latestDate"),'Y-m-d H:i:s') + "<br/>位置："
				+ dataObj.get("location") + "</div>";
		var x = dataObj.get("longtitude");
		var y = dataObj.get("latitude");
		if (x != "" && y != "") {
			var poi = new BMap.Point(x, y);
			map.centerAndZoom(poi, 12);
			var marke = new BMap.Marker(poi);
			map.addOverlay(marke);
			var infoWindow = null;
			marke.addEventListener("onmouseover", function() {
				infoWindow = new BMapLib.SearchInfoWindow(map, content, {
					title : "员工位置", // 标题
					width : 250, // 宽度
					height : 80, // 高度
					panel : "panel", // 检索结果面板
					enableAutoPan : true, // 自动平移
					enableSendToPhone : false,
					searchTypes : []
				}); // 创建信息窗口对象
				infoWindow.open(this);
			});
		} else {
			// 获取当前位置
			var localCity = new BMap.LocalCity();
			localCity.get(function(result) {
				var cityName = result.name;
				map.setCenter(cityName);
			});
		}
	}
})