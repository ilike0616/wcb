/**
 * Created by shqv on 2014-6-11.
 */
Ext.define('public.BaseList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.baseList',
    operateBtn : true, //关闭操作按钮
    enableBasePaging : true,//false 关闭翻页按钮
    enableSearchField:true,//false 关闭搜索框
    enableComplexQuery:true,//false 关闭查询功能
    enableStatisticBtn:true,//false 关闭统计按钮
    enableToolbar:true, //关闭工具条
    enableSummary:false,
    selType: 'checkboxmodel',
    alertName:'name',
    initValues:[],
    extraBtn:[],
    requires: [
        'public.BasePaging',
        'public.BaseSearchField',
        'public.BaseStatisticWin',
        'public.BaseSummary'
    ],
    initComponent: function() {
        var me = this,
            viewId=me.viewId,
            columns=me.columns,
            tbar=[],config,store,moduleId,
            childAlias = me.alias,
            operateBtn = me.operateBtn,
            store = me.store,
            mohu = [],
            enableSummary = me.enableSummary;
        if(Ext.typeOf(store)=='string'){
        	store = Ext.create(store);
        }
        if(!columns&&viewId){
            if(!window.localStorage.getItem(viewId)) {
                var url = 'view/index?viewId='+viewId;
                if (me.url != "" && Ext.typeOf(me.url) != 'undefined') {
                    url = me.url;
                }
                Ext.Ajax.request({
                    url: url,
                    method: 'POST',
                    timeout: 4000,
                    async: false,
                    success: function (response, opts) {
                        var view = Ext.JSON.decode(response.responseText);
                        columns = view.columns;
                        moduleId = view.moduleId;
                        if (operateBtn == true) {
                            tbar = view.tbar;
                        }
                        me.forceFit = view.forceFit;
                        mohu = view.mohu;
                        enableSummary = view.enableSummary;
                        window.localStorage.setItem(viewId, response.responseText);
                    }
                });
            }else{
                var view = Ext.JSON.decode(window.localStorage.getItem(viewId));
                columns = view.columns;
                moduleId = view.moduleId;
                me.forceFit = view.forceFit;
                if (operateBtn == true) {
                    tbar = view.tbar;
                }
                mohu = view.mohu;
                enableSummary = view.enableSummary;
            }
        }
        var btn = [];
        if(operateBtn == true&&(tbar==undefined||tbar.length==0)){
        		btn = [
//        	       {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
//        	       {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
//        	       {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true}
        	    ];
        }
        if(me.extraBtn.length>0){
        	btn = me.extraBtn;
        }
        if(tbar.length==0){
        	tbar = btn;
        }
        var btnToolbar = Ext.create('Ext.toolbar.Toolbar', {
            border : 0,
            items : tbar
        });
        var contextMenu;
        if(tbar!=undefined&&tbar.length>0){
            var finalTbar = [];
            var menu = Ext.Array.every(tbar,function(o){ //单纯的遍历数组
                delete o.xtype;
                delete o.disabled;
                if(o.optRecords != 'no') finalTbar.push(o);
                return true;
            });
            contextMenu = Ext.create('Ext.menu.Menu', {
                auto:true,
                formGrid:me,
                items: finalTbar
            });
        }

        // 生成查询框
        var complexQuery;
        if(me.enableComplexQuery){
            complexQuery = me.createSearchFieldContainer();
        }

        var toolbar = [];
        if(me.enableToolbar){
            var searchField ;
            if(me.enableSearchField && mohu.length>0){
                var emptyText = '请输入'+mohu.join('、')
        	   searchField = {width:300,xtype : 'baseSearchField',store : store,emptyText:emptyText};
            }
            if(!me.enableComplexQuery && !me.operateBtn){
              toolbar = [{
                  margin:'5 0 5 20',
                  items:[{
                      xtype: 'fieldcontainer',
                      fieldLabel: '快速搜索',
                      labelWidth: 70,
                      items: [
                          searchField
                      ]
                  }]
              }];
          }else{
              toolbar = [{
                  items:[
                      searchField,
                      {xtype:'tbfill'},
                      complexQuery
                  ]
              },{
                  items:[
                      btnToolbar
                  ]
              }];
          }
        }
        // 是否显示分页工具条
        var basePaging;
        if(me.enableBasePaging == true){
            basePaging = Ext.widget('basePaging',{
                store : store
            });
        }
        var baseSummary
        if(enableSummary == true){
            baseSummary = Ext.widget('baseSummary',{
                gridDom : me
            });
        }
        Ext.applyIf(me, {
        	store : store,
        	title1 : me.title,
            dockedItems: [
                basePaging,
                baseSummary
            ],
            columns:columns,
            moduleId:moduleId,
            tbar:{
                xtype: 'container',
                itemId:'tbarContainer',
                layout: 'anchor',
                defaults: {anchor: '0',border:0},
                defaultType: 'toolbar',
                items:toolbar
            },listeners: {
                itemcontextmenu: function(view, rec, node, index, e) {
                	if(contextMenu){
                        contextMenu.removeAll();
                        Ext.Array.each(me.query('button[autodisabled=true][disabled=false][optRecords!=no]'),function(o){
                            var obj = {};
                            Ext.Object.each(o, function(key, value) {
                                if(key != 'xtype') obj[key] = value;
                            });
                            contextMenu.add(obj);
                        })
                        contextMenu.itemId = rec.get('id');
                        e.stopEvent();
                        contextMenu.showAt(e.getXY());
                        return false;
                	}
                },
                afterrender:function(grid,eOpts){
                    // 锁定视图
                    var locked = grid.lockedGrid;
                    if(Ext.typeOf(locked) != 'undefined'){
                        var lockedView = locked.getView();
                        var lockedPanel = lockedView.panel;
                        // 获取所有的toolbar对象(包括分页条)
                        var toolbar = lockedPanel.getDockedItems('toolbar');
                        // 移除所有的toolbar对象
                        Ext.Array.each(toolbar,function(tb){
                            lockedPanel.removeDocked(tb);
                        })
                        // 移除tbar
                        var tbarContainer = lockedPanel.getDockedItems('container[itemId="tbarContainer"]');
                        lockedPanel.removeDocked(tbarContainer[0]);
                    }
                    // 正常视图(暂时保留)
                    var normal = grid.normalGrid;
                    if(Ext.typeOf(normal) != 'undefined'){
                        var normalView = normal.getView();
                        //console.info(normalView.panel.getDockedItems('container[itemId="tbarContainer"]'))
                    }

                    // 鼠标移动到单元格上面悬浮显示
                    me.moveCellShowTip(grid);
                }
            }
       });
        me.callParent(arguments);
    },
    moveCellShowTip:function(grid){
        var view = grid.getView();
        Ext.create('Ext.tip.ToolTip', {
            target:view.el,
            delegate : '.x-grid-cell-inner',
            trackMouse: true,
            maxWidth:500,
            listeners: {
                beforeshow: function(tip, eOpts) {
                    var tipText = (tip.triggerElement.innerText || tip.triggerElement.textContent);
                    if (Ext.isEmpty(tipText) || Ext.isEmpty(tipText.trim()) ) {
                        return false;
                    }
                    tip.update(tipText);
                }
            }
        })
    },
    enterKey : function(o, e){
        var me = this;
        if (e.getKey() == e.ENTER) {
            me.search(o);
            return false;
        }
    },
    searchFieldCombo : function(o, newValue, oldValue, eOpts){
        var me = this;
        if(o.valueModels[0] == null || o.valueModels[0] == 'undefined') return;
        var pageType = o.valueModels[0].data.pageType;
        var textField = me.down("textfield[name=searchFieldTextValue]");
        var numberField = me.down("textfield[name=searchFieldNumberValue]");
        var comboField = me.down("combo[name=searchFieldComboValue]");
        var startDateField = me.down("datefield[name=searchFieldStartDateValue]");
        var endDateField = me.down("datefield[name=searchFieldEndDateValue]");
        if(pageType == "combo" || pageType == "checkbox"){
            textField.hide();
            if(numberField) numberField.hide();
            if(startDateField) startDateField.hide();
            if(endDateField) endDateField.hide();
            if(comboField)  comboField.reset();
            var storeArr = o.valueModels[0].data.store;
            if(comboField) comboField.getStore().removeAll();
            if(comboField) comboField.getStore().add(storeArr);
            if(comboField) comboField.show();
        }else if(pageType == "textfield"){
        	if(numberField) numberField.hide();
        	if(comboField)  comboField.hide();
        	if(startDateField) startDateField.hide();
        	if(endDateField) endDateField.hide();
        	if(textField) textField.reset();
        	if(textField) textField.show();
        }else if(pageType == 'datetimefield'){
            var dateStore = [['TODAY','今天'],['THIS_WEEK','本周'],['THIS_MONTH','本月'],['THIS_YEAR','今年'],['TOMORROW','明天'],
                ['NEXT_WEEK','下周'],['NEXT_MONTH','下月'] ,['YESTERDAY','昨天'],['LAST_WEEK','上周'],
                ['LAST_MONTH','上月'],['LAST_YEAR','去年'],['CUSTOM','自定义时间']];
            textField.hide();
            if(numberField) numberField.hide();
            if(startDateField) startDateField.hide();
            if(endDateField) endDateField.hide();
            if(comboField)comboField.reset();
            if(comboField)comboField.getStore().removeAll();
            if(comboField)comboField.getStore().add(dateStore);
            if(comboField)comboField.show();
        }else if(pageType == "numberfield"){
        	if(comboField)comboField.hide();
        	if(startDateField)startDateField.hide();
        	if(endDateField) endDateField.hide();
        	if(textField) textField.hide();
        	if(numberField)numberField.reset();
        	if(numberField)numberField.show();
        }
    },
    searchFieldValueCombo : function(o, newValue, oldValue, eOpts ){
        var me = this;
        var objList = o.up(me.alias[0].replace("widget.",""));
        var comboField = objList.down("combo[name=searchField]");
        var pageType = comboField.valueModels[0].data.pageType;
        if(pageType == 'datetimefield'){
            var startDateField = objList.down("datefield[name=searchFieldStartDateValue]");
            var endDateField = objList.down("datefield[name=searchFieldEndDateValue]");
            if(newValue == 'CUSTOM'){
                startDateField.reset();
                endDateField.reset();
                startDateField.show();
                endDateField.show();
            }else{
                startDateField.reset();
                endDateField.reset();
                startDateField.hide();
                endDateField.hide();
            }
        }

        if((pageType != 'datetimefield' || (pageType == 'datetimefield' && newValue != 'CUSTOM')) && newValue != null){
            me.search(o);
        }
    },
    search: function(btn, e, eOpts){
        var me = this;
        var objList = btn.up(me.alias[0].replace("widget.",""));
        var searchFieldObj = objList.down("combo[name=searchField]");
        var searchFieldName = searchFieldObj.getValue();
        var pageType = searchFieldObj.valueModels[0].data.pageType;
        var dbType = searchFieldObj.valueModels[0].data.dbType;

        var searchFieldOperation = objList.down("combo[name=searchFieldOperation]").getValue();

        var searchFieldValue = "";
        var startDateFieldValue = "";
        var endDateFieldValue = "";
        if(pageType == 'textfield') {
            searchFieldValue = objList.down("textfield[name=searchFieldTextValue]").getValue();
        }else if(pageType == 'numberfield'){
            searchFieldValue = objList.down("numberfield[name=searchFieldNumberValue]").getValue();
        }else if(pageType == 'combo' || pageType == 'checkbox' || pageType == 'datetimefield'){
            searchFieldValue = objList.down("combo[name=searchFieldComboValue]").getValue();
            if(searchFieldValue == 'CUSTOM'){
                startDateFieldValue = objList.down("datefield[name=searchFieldStartDateValue]").getValue();
                endDateFieldValue = objList.down("datefield[name=searchFieldEndDateValue]").getValue();
                if(startDateFieldValue != null && startDateFieldValue != "") {
                    startDateFieldValue = Ext.util.Format.date(startDateFieldValue, 'Y-m-d H:m:s');
                }else{
                    alert("请选择开始时间！");
                    return;
                }
                if(endDateFieldValue != null && endDateFieldValue != "") {
                    endDateFieldValue = Ext.util.Format.date(endDateFieldValue, 'Y-m-d H:m:s');
                }else{
                    alert("请选择结束时间！");
                    return;
                }
            }
        }

        var searchCondition = [];
        if(objList.down("checkboxfield[name=selectAdd]").getValue() == true){
            var tempSearchCondition = me.store.proxy.extraParams.searchCondition;
            if(tempSearchCondition){
                tempSearchCondition = Ext.JSON.decode(tempSearchCondition);
                Ext.Array.each(tempSearchCondition, function(condition) {
                    var fieldName = condition.searchFieldName;
                    // 避免同一个字段重复放入
                    if(fieldName != searchFieldName){
                        searchCondition.push(Ext.JSON.encode(condition));
                    }
                });
            }
        }
        if(searchFieldValue != ""){
            var conditionObj = new Object();
            conditionObj.searchFieldName = searchFieldName;
            conditionObj.searchFieldOperation = searchFieldOperation;
            conditionObj.searchFieldValue = searchFieldValue;
            conditionObj.dbType = dbType;
            if(startDateFieldValue){
                conditionObj.startDateFieldValue = startDateFieldValue;
            }
            if(endDateFieldValue){
                conditionObj.endDateFieldValue = endDateFieldValue;
            }
            //"#@#"
            searchCondition.push(Ext.JSON.encode(conditionObj));
            searchCondition.join(",");
        }else{
            searchCondition = [];
        }
        Ext.apply(me.store.proxy.extraParams, {searchCondition :"["+searchCondition+"]"});
        me.store.load();
    },
    createSearchFieldContainer : function(){
        var me = this;
        if(Ext.typeOf(me.viewId) == 'undefined')return;
        var searchFieldCombo =  Ext.create('Ext.form.ComboBox',{
            xtype:'combo',name:'searchField',displayField : 'fieldLabel',valueField : 'name',width : 150,itemId:'searchField'+me.viewId,
            listeners : {
                scope : me,
                change : me.searchFieldCombo
            },
            tpl: Ext.create('Ext.XTemplate',
                '<tpl for=".">',
                '<div class="x-boundlist-item" pageType="{pageType}" store={store} dbType={dbType}>{fieldLabel}</div>',
                '</tpl>'
            ),
            store: Ext.create('Ext.data.Store',{
                listeners : {
                    load : function(o, records, successful, eOpts){
                        if(records != null && records[0] != null && records[0] != 'undefined'){
                            searchFieldCombo.setValue(records[0].data.name);
                        }
                    }
                },
                proxy: {
                    type: 'ajax',
                    url : 'view/searchField?viewId='+me.viewId,
                    reader: {
                        type: 'json',
                        root: 'fields'
                    }
                },
                fields:['fieldLabel', 'name','pageType','store','dbType'],
                autoLoad:true
            })
        });
        var statisticBtn
        if(me.enableStatisticBtn){
            statisticBtn = {xtype:'button',text:'统计',itemId:'statisticButton',listeners:{scope : me,click: me.statistic}};
        }
        var complexQuery = Ext.create('Ext.container.Container', {
            layout:'hbox',
            defaults: {
                anchor: '0',
                margin:'0 5 0 5'
            },
            items: [
                {xtype:'tbfill'},
                searchFieldCombo,
                {xtype:'combo',name:'searchFieldOperation',value:'ilike',width : 50, store:[['ilike','='],['eq','=='],['ne','!='],['gt','>'],['ge','>='],['lt','<'],['le','<=']]},
                {xtype:'textfield',name:'searchFieldTextValue',listeners:{scope: me,specialkey:me.enterKey}},
                {xtype:'numberfield',name:'searchFieldNumberValue',invalidText:'只允许输入数值型数据',hidden:true,listeners:{scope: me,specialkey:me.enterKey}},
                {xtype:'combo',name:'searchFieldComboValue',width : 120,store:[[1,'aa']],autoSelect:true,forceSelection:true,typeAhead : true,emptyText:'-- 请选择 --',hidden:true,listeners:{scope:me,change: me.searchFieldValueCombo}},
                {xtype:'datefield',name:'searchFieldStartDateValue',fieldLabel:'从',hidden:true,labelWidth:20,width:130,format:'Y-m-d',submitFormat:'y-m-d'},
                {xtype:'datefield',name:'searchFieldEndDateValue',fieldLabel:'到',hidden:true,labelWidth:20,width:130,format:'Y-m-d',submitFormat:'y-m-d'},
                {xtype:'checkboxfield',name:'selectAdd',boxLabel:'在结果中查询'},
                {xtype:'button',text:'查询',itemId:'searchButton',iconCls:'',listeners:{scope : me,click: me.search}},
                statisticBtn
            ]
        });
        return complexQuery;
    },
    statistic : function(btn, e, eOpts){
    	var me = this;
    	var objList = btn.up(me.alias[0].replace("widget.",""));
    	var view =  Ext.widget('baseStatisticWin',{listDom:objList}).show();
    }
})

