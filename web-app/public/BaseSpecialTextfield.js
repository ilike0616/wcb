/**
 * 弹出列表选择
 * Created by guozhen on 2014/10/15.
 * 可以传递的参数：
 * 1、storeObjName ：store对应的domain对象名称，如果XXXXStore去掉Store就是domain对象，可以不传,
 *    如：EmployeePagingStore->Employee需要传，EmployeeStore->Employee不需要传
 * 2、defaultFillVal ：默认是否填充该组件的值，取值：undefined,false,true
 *    如果defaultFillVal为空或没定义，且me.name=owner.name,Store是EmployeeXXStore,则赋值，否则不赋值
 *    如果defaultFillVal不为空，且Store是EmployeeXXStore,则赋值，否则，不赋值
 */
Ext.define('public.BaseSpecialTextfield', {
    extend: 'Ext.form.Panel',
    alias: 'widget.baseSpecialTextfield',
    requires : [
        'public.BasePopList'
    ],
    border:0,
    layout: {
        type: 'column',
        columns: 2
    },
    defaults: {
        msgTarget: 'side' //qtip title under side
    },
    customParamName: '',
    storeName: '',
    enableObjectListWin:true,
    initComponent: function() {
        var me = this,allowBlank=true,storeObjName = me.storeObjName;
        me.customParamName = me.paramName;
        this.storeName = me.store;
        var fromType = 'user';  // 管理员还是用户层调用,默认用户层
        if(me.fromType != '' && Ext.typeOf(me.fromType) != 'undefined'){
            fromType = me.fromType;
        }
        me.store = Ext.create(fromType+'.store.'+me.store+'');
        // 额外的参数
        if(Ext.typeOf(me.extraParams) != 'undefined'){
            Ext.apply(me.store.proxy.extraParams,me.extraParams);
        }
        var displayName = me.name;
        var index = displayName.indexOf(".");
        if(index > 0){
            displayName = displayName.substr(index+1);
        }
        if(me.allowBlank==false){
        	allowBlank = false;
        }
        var showWinBtn
        if(me.enableObjectListWin){
            showWinBtn = {
                style : {
                    marginLeft : '8px'
                },
                xtype: 'button',
                iconCls : 'dept_table',
                listeners : {
                    scope : me,
                    click : me.showObjectListWin
                }
            }
        }
        Ext.applyIf(me, {
            items : [
                {
                    xtype: 'combo',
                    name : me.name,
                    store : me.store,
                    beforeLabelTextTpl:me.beforeLabelTextTpl,
                    fieldLabel: me.fieldLabel,
                    displayField: displayName,
                    typeAhead: true,
                    hideLabel: false,
                    //hideTrigger: true, // 是否可以下拉
                    allowBlank:allowBlank,
                    columnWidth:0.98,
                    minChars: 1,
                    queryParam : 'searchValue',
                    matchFieldWidth: true,
                    submitValue:false,
                    listConfig: {
                        loadingText: '正在查找...',
                        emptyText: '没有找到匹配的数据'
                    },
                    listeners : {
                        scope : me,
                        focus :function(o, The, eOpts){
                            me.reloadStore(false);
                        },
                        blur : function(o){
                            var objectValue = o.getValue();
                            if(objectValue == null || objectValue == ""){
                                me.setRelatedObjectFieldValue(new Object());
                            }
                            var names = me.name.split(".");
                            var objectName = this.storeName.replace("Store","");
                            if(me.storeObjectName != null && me.storeObjectName != ""){
                                objectName = me.storeObjectName;
                            }
                            // 特殊处理
                            if(this.storeName == 'EmployeePagingStore'){
                                objectName = 'Employee';
                            }else if(Ext.typeOf(me.storeObjName) != 'undefined'){
                                objectName = me.storeObjName;
                            }
                            Ext.Ajax.request({
                                url:'customer/judgeObjectName',
                                params:{objectName : objectName,objectParamName:names[1],objectParamValue : objectValue},
                                method:'POST',
                                timeout:4000,
                                async:false,
                                success:function(response,opts){
                                    var d = Ext.JSON.decode(response.responseText);
                                    if(d.success){
                                        me.setRelatedObjectFieldValue(d.object);
                                    }else{
                                        o.setValue('');
                                        me.down("hiddenfield[name="+me.hiddenName+"]").setValue('');
                                    }
                                },
                                failure:function(response,opts){
                                }
                            });
                        }
                    }
                },
                showWinBtn
            ],
            listeners : {
                render : function(o,epts){
                    var hiddenFieldId = {
                        xtype : 'baseHiddenField',
                        name : me.hiddenName
                    };
                    var win = o.ownerCt.ownerCt;
                    if(Ext.typeOf(me.makeHiddenField) == 'undefined'){
                        // 根据页面上是否存在me.hiddenName对应的隐藏变量，如果存在，则不添加，防止重复。否则，则生成一个
                        var hiddenField = win.down("hiddenfield[name="+me.hiddenName+"]");
                        if(hiddenField == null || Ext.typeOf(hiddenField) == 'undefined'){
                            me.add(hiddenFieldId);
                        }
                    }
                    /********************************默认是否选中当前员工的值  开始*****************************************/
                    var defaultFillValNull = false;
                    if(me.defaultFillVal == null || me.defaultFillVal == '' || me.defaultFillVal == undefined || me.defaultFillVal == true){
                        defaultFillValNull = true;
                    }
                    var isEmployeeStore = false;
                    if(me.storeName == 'EmployeeStore' || me.storeName == 'EmployeePagingStore'){
                        isEmployeeStore = true;
                    }
                    if(defaultFillValNull == true && isEmployeeStore == true){
                        me.store.load();
                        win.down("hiddenfield[name="+me.hiddenName+"]").setValue(Util.Employee.employee.id);
                        me.down('combo').setValue(Util.Employee.employee.name);
                    }
                    /********************************默认是否选中当前员工的值  结束*****************************************/
                }
            }
        });

        me.callParent(arguments);
    },
    showObjectListWin : function(){
        var me = this;
        var operateBtn = false;  //关闭操作按钮
        var enableBasePaging = true;    //false 关闭翻页按钮
        var enableSearchField = true;   //false 关闭搜索框
        var enableComplexQuery = false;  //false 关闭查询功能
        var enableToolbar = true;   //关闭工具条
        if(Ext.typeOf(me.operateBtn) != 'undefined'){
            operateBtn = me.operateBtn;
        }
        if(Ext.typeOf(me.enableBasePaging) != 'undefined'){
            enableBasePaging = me.enableBasePaging;
        }
        if(Ext.typeOf(me.enableSearchField) != 'undefined'){
            enableSearchField = me.enableSearchField;
        }
        if(Ext.typeOf(me.enableSearchField) != 'undefined'){
            enableSearchField = me.enableSearchField;
        }
        if(Ext.typeOf(me.enableComplexQuery) != 'undefined'){
            enableComplexQuery = me.enableComplexQuery;
        }
        if(Ext.typeOf(me.enableToolbar) != 'undefined'){
            enableToolbar = me.enableToolbar;
        }
        me.reloadStore(true);
        Ext.create('Ext.window.Window', {
            title: me.fieldLabel+'列表',
            height: 480,
            width: 730,
            layout: 'fit',
            modal : true,
            items: {
                xtype:'basePopList',
                store: me.store,
                viewId: me.viewId,
                columns: me.columns,
                operateBtn : operateBtn,
                enableBasePaging : enableBasePaging,
                enableSearchField:enableSearchField,
                enableComplexQuery:enableComplexQuery,
                enableToolbar:enableToolbar,
                border: false
            },
            listeners : {
                scope : me,
                beforeclose : me.clearSearchCondition
            },
            buttons: [
                {text:'确定',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
            ]
        }).show();
    },
    confirm : function(o, e, eOpts){
        var me = this;
        var winForm = o.ownerCt.ownerCt
        var grid = winForm.down("basePopList");
        var selData = grid.getSelectionModel().getSelection()[0];
        if(selData == null || Ext.typeOf(selData) == 'undefined'){
            Ext.Msg.alert("提示信息", "请选择记录！");
            return;
        }
        me.setRelatedObjectFieldValue(selData.data);

        // 必须清空原有的条件，不然下次打开还会带上上次查询的条件
        Ext.apply(grid.getStore().proxy.extraParams,{searchCondition:""});
        grid.getStore().clearFilter();

        winForm.close();
    },
    closeWin : function(o, e, eOpts){
        var me = this;
        var winForm = o.ownerCt.ownerCt;
        me.clearSearchCondition(winForm);
        winForm.close();
    },
    clearSearchCondition : function(winForm){
        var grid = winForm.down("basePopList");
        // 必须清空原有的条件，不然下次打开还会带上上次查询的条件
        Ext.apply(grid.getStore().proxy.extraParams,{searchCondition:""});
        grid.getStore().clearFilter();
    },
    reloadStore : function(isPaging){
        var me = this;
        //6.23以下两行代码、会导致数据重新load，在某些情况下，会导致过滤的数据，没有经过筛选！
        //Ext.apply(me.store.proxy.extraParams, {specialParam:""});
        //me.store.clearFilter();
        var parentFormObj = me.ownerCt.ownerCt;
        if(Ext.typeOf(me.paramName) != 'undefined' && me.paramName != ''){
            var paramValue = parentFormObj.down("hiddenfield[name="+me.paramName+"]").getValue();
            Ext.apply(me.store.proxy.extraParams, {specialParam:paramValue});
        }
        Ext.apply(me.store.proxy.extraParams, {isPaging:isPaging});
        me.store.load();
    },
    setRelatedObjectFieldValue : function(selData){
        var me = this;
        var parentFormObj = me.ownerCt.ownerCt;
        // 找到所有的关联组件并赋值
        var components = parentFormObj.query('component[name^='+me.hiddenName+'.]');
        if(components != null && components.length > 0){
            // 先把me.hiddenName对应的baseHiddenField赋值
            var hiddenNameCom = parentFormObj.down('component[name='+me.hiddenName+']');
            if(Ext.typeOf(hiddenNameCom) != 'undefined' && hiddenNameCom != null) hiddenNameCom.setValue(selData['id']);
            Ext.Array.each(components, function(component, index) {
                var fieldName = component.name;
                var index = component.name.indexOf(".");
                if(index > 0){
                    fieldName = fieldName.substr(index+1);
                }
                if(component.xtype != 'baseSpecialTextfield'){
                    parentFormObj.down(component.xtype+"[name="+component.name+"]").setValue(selData[fieldName]);
                }
            });
        }
        // 所有的initName以me.hiddenName.开头的
        var initNames = parentFormObj.query("component[initName!=''][initName^="+me.hiddenName+".]");
        me.setInitNameValue(initNames,selData);

        // 所有的customParamName以me.hiddenName.开头的
        if(Ext.typeOf(me.paramName) == 'undefined'){
            var customParamNames = parentFormObj.query("baseSpecialTextfield[customParamName="+me.hiddenName+"]");
            if(customParamNames != null && customParamNames.length > 0){
                Ext.Array.each(customParamNames, function(component, index) {
                    component.down('combo').setValue(null);
                    component.down('baseHiddenField').setValue(null);
                })
            }
        }
    },
    // 循环赋值
    setInitNameValue:function(components,selData){
        var me = this;
        var parentFormObj = me.ownerCt.ownerCt;
        if(components != null && components.length > 0){
            Ext.Array.each(components, function(component, index) {
                var fieldNameObj = "";
                var fieldName = component.initName;
                var index = component.initName.indexOf(".");
                var fieldNameIndex = 0;
                if(index > 0){
                    fieldName = fieldName.substr(index+1);
                    // 如：saleChance.customer.name
                    fieldNameIndex = fieldName.indexOf(".");
                    if(fieldNameIndex > 0){
                        fieldNameObj = fieldName.substr(0,fieldNameIndex);
                        fieldName = fieldName.substr(fieldNameIndex + 1);
                    }
                }

                if(component.xtype != 'baseSpecialTextfield'){
                    if(fieldNameObj){
                        if(Ext.typeOf(selData[fieldNameObj]) != 'undefined'){
                            parentFormObj.down(component.xtype+"[name="+component.name+"]").setValue(selData[fieldNameObj][fieldName]);
                        }else{
                            parentFormObj.down(component.xtype+"[name="+component.name+"]").setValue(null);
                        }
                    }else{
                        parentFormObj.down(component.xtype+"[name="+component.name+"]").setValue(selData[fieldName]);
                    }
                }else{
                    if(component.name != me.name){
                        if(Ext.typeOf(selData[fieldNameObj]) != 'undefined'){
                            component.down('combo').setValue(selData[fieldNameObj][fieldName]);
                            component.down('baseHiddenField').setValue(selData[fieldNameObj].id);
                        }else{
                            component.down('combo').setValue(null);
                            component.down('baseHiddenField').setValue(null);
                        }
                    }
                }
            });
        }
    }

})
