/**
 * Created by shqv on 2014-9-11.
 */
Ext.define('user.controller.BaseController', {
    extend: 'Ext.app.Controller',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    requires:[
        'Util.Employee'
    ],
    isStartTask : false,
    init:function() {
        this.application.getController("ReviewController");
        this.application.getController("SfaController");
        this.control({
        	'field':{
	       		 render: function (field, p) {
	       			 if(field.note){
		                     Ext.create('Ext.tip.ToolTip', {
		                    	 target: field.el,
		                    	 html: field.note
		                	 }); 
	       			 }
                 }
	       	},
            'grid[renderLoad=true]':{
                afterrender:function(grid){
                    grid.getStore().load();
                }
            },
            'tabpanel[name=navigationTabPanel]':{
                tabchange:function(tabPanel, newCard, oldCard, eOpts ){
                    var me = this;
                    var viewAlias = newCard.viewAlias;
                    if(!newCard.lastLoadTime) newCard.lastLoadTime = new Date();
                    var lastLoadTime = newCard.lastLoadTime.getTime();
                    var currentTime = new Date().getTime();
                    var diff = (currentTime - lastLoadTime) / 1000;
                    // 上次访问距离当前时间大于10秒才刷新
                    if(diff > 10){
                        var items = [];
                        if(viewAlias == 'scheduleMain'){ // 日程安排
                            newCard.down('calendarpanel').eventStore.load();
                        }else if(viewAlias == 'aimPerformanceMain') { // 目标业绩
                            var grid = newCard.down('aimPerformanceList');
                            grid.search(grid);
                        }else if(viewAlias == 'saleAimList') { // 销售目标
                            newCard.search(newCard);
                        }else if(newCard.xtype == 'workBench'){
                            var portalPanel = newCard.down('portalpanel');
                            var items = portalPanel.its;
                            Ext.Array.each(items,function(o){
                                var portlet = portalPanel.down(o.xtype)
                                newCard.refreshPortalPanel(portlet);
                            })
                        }else{
                            if(newCard.store != null){ // 直接就是baseList
                                items.push(newCard);
                            }else{ // 框架
                                items = newCard.items.items;
                            }
                            Ext.Array.each(items,function(grid){
                                if(grid.lockable == true){ // 锁定列
                                    grid = grid.view.normalGrid;
                                }
                                var view = grid.view;
                                if(view != null){
                                    if(view.xtype == 'gridview'){
                                        grid.store.load();
                                    }else if(view.xtype == 'treeview'){
                                        me.GridDoActionUtil.loadStore(grid);
                                    }
                                }
                            })
                        }
                        newCard.lastLoadTime = new Date();
                    }
                }
            },
            'baseTreeGrid':{
                select:function(selectModel, record, index, eOpts){
                    var grid = eOpts.up('treepanel');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(false);
                    });
                }, deselect:function(selectModel, record, index, eOpts){
                    var grid = eOpts.up('treepanel');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            },
            'baseList':{
                select:function(selectModel, record, index, eOpts){
                    var grid = selectModel.view.up('grid');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var selectedCount = selectModel.getSelection().length;
                    // 判断button条件
                    this.judgeButtonCondition(grid,record);

                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        if(selectedCount > 1){ // 选中条数大于1
                            if(o.optRecords == 'many'){ // 按钮没有附加条件的或者可用条数大于0的，按钮可用
                                if(o.hasCondition == false || (Ext.typeOf(o.recordMap) != 'undefined' && o.recordMap.getCount() > 0)){
                                    o.setDisabled(false);
                                }
                            }else if(o.optRecords == 'one'){
                                o.setDisabled(true);
                            }
                        }else{
                            // 选中条数等于1且没有条件过滤的，则所有的都可用
                            if(Ext.typeOf(o.hasCondition) == 'undefined' || (Ext.typeOf(o.recordMap) != 'undefined' && o.recordMap.getCount() > 0)){
                                o.setDisabled(false);
                            }
                        }
                    });
                }, deselect:function(selectModel, record, index, eOpts){
                    var me = this;
                    var selectedCount = 0;
                    var grid = selectModel.view.up('baseList');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var selectedCount = selectModel.getSelection().length;
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        if(Ext.typeOf(o.recordMap) != 'undefined'){
                            if(o.recordMap.containsKey(record.get('id'))){
                                o.recordMap.removeAtKey(record.get('id'));
                            }
                        }
                        if(selectedCount == 0){ // 最后一条选择的被取消，则全部不可用
                            o.setDisabled(true);
                        }else {
                            if(o.optRecords == 'many'){
                                // 按钮没有附加条件的或者可用条数大于0的，按钮可用
                                if(Ext.typeOf(o.hasCondition) == 'undefined' || o.hasCondition == false || (Ext.typeOf(o.recordMap) != 'undefined' && o.recordMap.getCount() > 0)){
                                    o.setDisabled(false);
                                }else{
                                    o.setDisabled(true);
                                }
                            }else if(o.optRecords == 'one'){
                                // optRecords=one的情况下:
                                // 1、一条选中，按钮可用；2、多条选中,按钮禁用
                                if(selectedCount == 1){
                                    o.setDisabled(false);
                                }else{
                                    o.setDisabled(true);
                                }
                            }
                        }
                        if(me.isStartTask == false){
                            me.setButtonText(grid);
                        }
                    });
                }
            },
            'baseForm':{
            	afterrender:function(form){
            		if(form.up("window")){
            			if(form.changeWinTitle)	form.up("window").setTitle(form.winTitle);
            			if(form.up("window").listDom&&form.up("window").optType=='add'){
                			form.form.setValues(form.up("window").listDom.initValues);
                		}
            		}
            		//form宽度加20个像素，为Y滚动条预留位置
            		form.setWidth(form.getWidth()+20);
            	}
            },
            'menuTree treeview':{
            	itemkeydown:function( view, record, item, index, e, eOpts){
            		var viewport = view.up('mainviewport');
            		var tap = viewport.down('tabpanel');
            		if(e.getKey()==e.Q){
            			if(tap.getActiveTab().nextSibling())tap.setActiveTab(tap.getActiveTab().nextSibling());
            			else tap.setActiveTab(0);
            		}
            	}
            },
            'baseList dataview':{
            	itemkeydown:function( view, record, item, index, e, eOpts){
            		var baseList = view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
            		if(baseList){
	        			if(e.getKey()==e.ESC){
	        				baseList.getSelectionModel().deselectAll();
	        			}else if(e.getKey()==e.ENTER){
	        				baseList.fireEvent('itemdblclick',baseList,record);
	        			}else if(e.getKey()==e.R){
	        				view.up('grid').getStore().load();
	        			}else if(e.getKey()==e.RIGHT){
	        				if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').moveNext();
	        			}else if(e.getKey()==e.LEFT){
	        				if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').movePrevious();
	        			}
	        			view.focus(false,1000);
            		}
        		}
            },
            'baseTreeGrid treeview':{
            	itemkeydown:function( view, record, item, index, e, eOpts){
            		var baseList = view.up('baseTreeGrid');
            		if(baseList){
	        			if(e.getKey()==e.ESC){
	        				baseList.getSelectionModel().deselectAll();
	        			}else if(e.getKey()==e.ENTER){
	        				baseList.fireEvent('itemdblclick',view);
	        			}else if(e.getKey()==e.R){
	        				baseList.getStore().load();
	        			}else if(e.getKey()==e.RIGHT){
	        				if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').moveNext();
	        			}else if(e.getKey()==e.LEFT){
	        				if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').movePrevious();
	        			}
	        			view.focus(false,1000);
            		}
            	}
            },'menu[auto=true]':{
            	click:function( menu, item, e, eOpts){
                    var store = menu.formGrid.getStore();
                    var record;
                    if(Ext.typeOf(store) == 'undefined') {
                        store = menu.formGrid.getTreeStore();
                        record = store.getNodeById(menu.itemId);
                    }else{
                        record = store.getById(menu.itemId); // 当前鼠标右击的记录
                    }
            		if(item.operationId){
	            		var btn = menu.formGrid.down('button[operationId='+item.operationId+']');
	            		if(btn)btn.fireEvent('click',btn,e, eOpts,record);
            		}else if(item.itemId){
            			var btn = menu.formGrid.down('button[itemId='+item.itemId+']');
	            		if(btn)btn.fireEvent('click',btn,e, eOpts,record);
            		}
            	}
            },
            'button[auto=true]':{
                click:function(btn,e,eOpts,rightClickRec){
                	var vw = btn.vw,view,showWin=btn.showWin,baseList,targetEl=btn.targetEl,isCustom=btn.isCustom,
                    baseList = btn.up('baseList');// 取baseList里面的数据应该放到下面，不要直接写到这个地方
                    if(Ext.typeOf(btn.up('baseList')) == "undefined"){
                        baseList = btn.up('baseTreeGrid');
                    }
                    var moduleId = baseList.moduleId;
                    var targetDom;
                    if(targetEl){
                        targetDom = Ext.ComponentQuery.query(targetEl)[0]
                    }
                	if(showWin){
                        if(isCustom && vw != 'baseImport' && vw != 'baseExport'){
                            view =  Ext.create('public.BaseWinForm',{listDom:baseList,viewId:vw,optType:btn.optType,targetDom:targetDom,recordMap:btn.recordMap,targetEl:targetEl,moduleId:moduleId}).show();
                        }else{
                            view =  Ext.widget(vw,{listDom:baseList,viewId:vw,optType:btn.optType,targetDom:targetDom,recordMap:btn.recordMap}).show();
                        }
                	}
                	if(btn.optType=='update'||btn.optType=='view'){
                		var record = baseList.getSelectionModel().getSelection()[0];
                        // 鼠标右击的记录不为空
                        if(Ext.typeOf(rightClickRec) != 'undefined') record = rightClickRec;
                		if(view.down('form'))view.down('form').loadRecord(record);
                	}else if(btn.optType=='del'){
                        this.GridDoActionUtil.doDelete(baseList,baseList.alertName,btn);
                	}
                	if(btn.optType=='add'||btn.optType=='update'||btn.optType=='view'){
                		//加载明细
                		if(view.down('grid')){
	                		var store = view.down('grid').getStore();
		        			var id = "0";
		        			if(view.down('form').down("field[name=id]")){
		        				id = view.down('form').down("field[name=id]").value;
		        			}
		        			Ext.apply(store.proxy.extraParams,{object_id:id});
		        			store.load();
                		}
                	}
                    if(btn.operationId == moduleId+'_review'){
                        var record = baseList.getSelectionModel().getSelection()[0],
                            form = view.down('form');
                        if(form.down("field[name='module.moduleId']")) {
                            form.down("field[name='module.moduleId']").setValue(moduleId);
                        }else{
                            form.add({
                                name:'module.moduleId',
                                hidden:true,
                                value:moduleId
                            });
                        }
                        if(form.down("field[name='linkId']")) {
                            form.down("field[name='linkId']").setValue(record.get('id'));
                        }else{
                            form.add({
                                name:'linkId',
                                hidden:true,
                                value:record.get('id')
                            });
                        }
                        var owner = record.get('owner')?record.get('owner').id:record.get('employee').id;
                        var ownerName = record.get('owner')?record.get('owner').name:record.get('employee').name;
                        if(form.down("field[name='owner']")){
                            form.down("field[name='owner']").setValue(owner);
                        }else{
                            form.add({
                                name:'owner',
                                hidden:true,
                                value:owner
                            });
                        }
                        if(form.down("field[name='owner.name']")){
                            form.down("field[name='owner.name']").setValue(ownerName);
                        }
                    }
                }
            },
            'button[autoInsert=true]':{
                click:function(btn){
                    var window = btn.up('window');
                    var baseEditLists = window.query('baseEditList');
                    var correct = true;
                    var params = {};
                    if(baseEditLists && baseEditLists.length > 0){
                        correct = this.judgeFormDetailIsCorrect(baseEditLists,params);
                    }
                    if(correct == true){
                        if(window.targetDom){
                            this.GridDoActionUtil.doInsert(btn.up('window').targetDom,btn.up('form'),btn.up('window'),params);
                        }else if(window.listDom){
                            this.GridDoActionUtil.doInsert(btn.up('window').listDom,btn.up('form'),btn.up('window'),params);
                        }else{
                            this.GridDoActionUtil.doInsert(Ext.ComponentQuery.query(btn.target)[0],btn.up('form'),btn.up('window'),params);
                        }
                    }
                }
            },
            'button[autoUpdate=true]':{
                click:function(btn){
                    var window = btn.up('window');
                    var baseEditLists = window.query('baseEditList');
                    var correct = true;
                    var params = {};
                    if(baseEditLists && baseEditLists.length > 0){
                        correct = this.judgeFormDetailIsCorrect(baseEditLists,params);
                    }
                    if(correct == true){
                        if(window.targetDom){
                            this.GridDoActionUtil.doUpdate(btn.up('window').targetDom,btn.up('form'),btn.up('window'),params);
                        }else if(window.listDom){
                            this.GridDoActionUtil.doUpdate(btn.up('window').listDom,btn.up('form'),btn.up('window'),params);
                        }else{
                            this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('form'),btn.up('window'),params);
                        }
                    }
                }
            },
            'button[autoDelete=true]':{
                click:function(btn){
                    this.GridDoActionUtil.doDelete(btn.up('gridpanel'),btn.subject,btn);
                }
            },
            'button[autoShowAdd=true]':{
                click:function(btn){
                    if(btn.target) {
                        var view = Ext.widget(btn.target);
                        view.show();
                    }else{
                        Ext.Msg.alert('提示', '按钮的target属性未指定！');
                    }
                }
            },
            'button[autoShowEdit=true]':{
                click:function(btn){
                    var record = btn.up('gridpanel').getSelectionModel().getSelection()[0]
                    if(btn.target) {
                        var view = Ext.widget(btn.target);
                        view.show();
                        view.down('form').loadRecord(record);
                    }else{
                        Ext.Msg.alert('提示', '按钮的target属性未指定！');
                    }
                }
            },
            'button[autoClose=true]':{
                click:function(btn){
                    btn.up('window').close();
                }
            },
            'baseEditList':{
                select: function (selectModel, record, index, eOpts) {
                    var grid = selectModel.view.headerCt.grid;
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(false);
                    });

                },
                deselect: function( selectModel,record, index, eOpts){
                    var grid = selectModel.view.headerCt.grid;
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            }
        });
    },
    judgeButtonCondition:function(grid,record){
        var me = this;
        Ext.Array.forEach(grid.query('button[hasCondition=true]'),function(o,index){
            var userOptConditions = o.userOptConditions;
            // 是否匹配条件
            var isMatchCond = true;
            var isError = false;
            Ext.Array.each(userOptConditions, function(condition) {
                isMatchCond = true;
                Ext.Array.each(condition.userOptConditionDetail, function(detail) {
                    var operator = detail.operator;
                    var value = detail.value;
                    var valueFlag = detail.valueFlag;
                    var dbType = detail.dbType;
                    var toMany = detail.toMany; // true：表示record里面的值对应多个，否则，value里面的值对应多个
                    var fieldNameRecordVal = record.get(detail.fieldName);
                    if(fieldNameRecordVal == null || fieldNameRecordVal == ''){
                        isError = true;
                        return false;
                    }
                    if(operator == 'in' || operator == 'notin'){ // 包含或不包含处理方式
                        if(dbType == 'java.lang.String'){ // 字符串型
                            var valIndex = fieldNameRecordVal.indexOf(value);
                            if(valIndex > -1 && operator == 'in'){ // 包含
                                isMatchCond = isMatchCond && true
                            }else if(valIndex < 0 && operator == 'notin'){ // 不包含
                                isMatchCond = isMatchCond && true
                            }else{
                                isMatchCond = isMatchCond && false
                            }
                        }else {
                            // 字面值并且值等于me，则应该取实际值
                            if(value == 'me'){
                                value = Util.Employee.employee.id;
                            }
                            var containsValue = false;
                            if(toMany == true){ // toMany==true:表示不带点的employee对象且record里面对应多个值
                                var empObjs = record.get(detail.fieldName);
                                Ext.Array.each(empObjs,function(empObj){
                                    if(empObj.id == value){
                                        containsValue = true;
                                        return false;
                                    }
                                });
                            }else{
                                if(toMany == false){ // toMany==false:表示不带点的employee对象且record里面对应一个值
                                    fieldNameRecordVal = fieldNameRecordVal.id;
                                }
                                var valueArr = []
                                if(value.toString().indexOf(",") > -1){
                                    valueArr = value.split(',');
                                }else{
                                    valueArr.push(value);
                                }
                                Ext.Array.each(valueArr,function(v){
                                    // 值有可能包含me,此时需要转换成实际值
                                    if(v == 'me'){
                                        v = Util.Employee.employee.id;
                                    }
                                    if(v == fieldNameRecordVal){
                                        containsValue = true;
                                        return false;
                                    }
                                });
                            }
                            if(operator == 'in'){ // 包含
                                isMatchCond = isMatchCond && containsValue;
                            }else if(operator == 'notin'){ // 不包含
                                if(containsValue == false){ // 说明
                                    isMatchCond = isMatchCond && true;
                                }else{
                                    isMatchCond = isMatchCond && false;
                                }
                            }
                        }
                    }else{ // 普通操作符处理方式
                        if(value == 'me'){
                            value = Util.Employee.employee.id;
                            if(toMany == false){ // toMany==false:表示不带点的employee对象且record里面对应一个值
                                fieldNameRecordVal = fieldNameRecordVal.id;
                            }
                        }else{
                            if(dbType == 'java.util.Date') { // 日期型,需要转换一下
                                fieldNameRecordVal = Date.parse(new Date(fieldNameRecordVal));
                                value = Date.parse(new Date(value));
                            }
                        }
                        var bds = fieldNameRecordVal + operator + value;
                        isMatchCond = isMatchCond && eval(bds);
                    }
                    // 因为明细里面各条目是且地关系，所以只要有一个为假，则整个这个条件组为假,直接跳出
                    if(isMatchCond == false) return false;
                })
                // 因为条件组里面各条目是或的关系，所以只要有一个为真，则整个这个条件组为真,直接跳出
                if(isMatchCond == true){
                    return false;
                }
            })
            if(isMatchCond == true && isError == false){
                o.setDisabled(true);
            }else{
                o.setDisabled(false);
                if(o.optRecords == 'many'){
                    if(Ext.typeOf(o.recordMap) == 'undefined'){
                        var map = new Ext.util.HashMap();
                        o.recordMap = map;
                    }
                    o.recordMap.add(record.get('id'),record);
                    if(me.isStartTask == false){
                        me.setButtonText(grid);
                    }
                    //o.setText(o.btnText + "(" + o.recordMap.getCount() + ")");
                }
            }
        });
    },
    setButtonText:function(grid){
        var me = this;
        var selectModel = grid.getSelectionModel();
        me.isStartTask = true;
        Ext.defer(function(){
            Ext.Array.forEach(grid.query('button[hasCondition=true]'),function(o,index){
                if(Ext.typeOf(o.recordMap) != 'undefined'){
                    if(selectModel.getSelection().length == 0){ // 没有选中的记录
                        o.setText(o.btnText);
                        delete o.recordMap;
                    }else{
                        if(o.recordMap.getCount() > 0){
                            o.setText(o.btnText + "(" + o.recordMap.getCount() + ")");
                        }else{
                            o.setText(o.btnText);
                            delete o.recordMap;
                        }
                    }
                }
            })
            me.isStartTask = false;
        },100)
    },
    /**
     * 判断明细是否有错误数据
     * @param baseEditLists
     * @returns {boolean}
     */
    judgeFormDetailIsCorrect:function(baseEditLists,params){
        var me = this;
        var success = true;
        var detail = "detail";
        var dels = "dels";
        for(var i=0;i<baseEditLists.length;i++){
            var grid = baseEditLists[i];
            var records = grid.getStore().data.items; // 获取所有记录。如果getModifiedRecords,则修改时会有问题
            var data = [];
            Ext.Array.each(records, function (model) {
                data.push(Ext.JSON.encode(model.data));
            });
            var removeRecords = grid.getStore().getRemovedRecords();
            var dels = []
            Ext.Array.each(removeRecords, function (model) {
                dels.push(Ext.JSON.encode(model.get('id')));
            });
            if(i > 0) {
                detail = detail+i;
                dels = dels+i;
            }
            params[detail] = "[" + data.join(",") + "]";
            params[dels] = "[" + dels.join(",") + "]";
            if(data.length > 0){
                Ext.Ajax.request({
                    url:'base/validateDetailRecord',
                    params:{viewId:grid.viewId,detail:"[" + data.join(",") + "]"},
                    method:'POST',
                    async:false,
                    success:function(response,opts){
                        var d = Ext.JSON.decode(response.responseText);
                        if(d.success){
                            var errorList = d.errorList;
                            if(errorList.length > 0){
                                success = false;
                                me.showErrorTips(grid,errorList);
                            }
                        }else{
                            if(d.msg) Ext.example.msg('提示', d.msg);
                            success = false;
                        }
                    },
                    failure:function(response,opts){
                        var errorCode = "";
                        if(response.status){
                            errorCode = 'error:'+response.status;
                        }
                        Ext.example.msg('提示', '操作失败！'+errorCode);
                        success = false;
                    }
                });
            }
        }
        return success;
    },
    showErrorTips:function(grid,errorList){
        Ext.Array.each(errorList,function(o){
            var tip = new Ext.tip.ToolTip({
                cls: Ext.baseCSSPrefix + 'grid-row-editor-errors',
                anchor: 'left',
                anchorToTarget: false
            });
            tip.showAt([0, 0]);
            tip.update(o.elList);
            var record = grid.getStore().getAt(o.index);
            var rowId = grid.view.getRowId(record);
            var row = grid.getEl().down("#" + rowId),
                viewEl = grid.view.editingPlugin.view.el,
                viewHeight = viewEl.dom.clientHeight,
                viewTop = 10,
                viewBottom = viewTop + viewHeight,
                rowHeight = row.getHeight(),
                rowTop = row.getOffsetsTo(grid.view.editingPlugin.view.body)[1],
                rowBottom = rowTop + rowHeight;
            if (rowBottom > viewTop && rowTop < viewBottom) {
                tip.showAt(tip.getAlignToXY(viewEl, 'tl-tr', [15, row.getOffsetsTo(viewEl)[1]]));
            } else {
                tip.hide();
            }
            tip.enable();
        })
    }
});