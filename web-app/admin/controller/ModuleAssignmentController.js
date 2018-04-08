/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define("admin.controller.ModuleAssignmentController",{
    extend:'Ext.app.Controller',
    views:['moduleAssignment.Main','moduleAssignment.List','dept.DeptUserList','moduleAssignment.EnableOperationList'
    ,'moduleAssignment.EnableUserPortalList','moduleAssignment.condition.Main','moduleAssignment.condition.List'
    ,'moduleAssignment.condition.DetailList','moduleAssignment.condition.Add','moduleAssignment.condition.Edit'
    ,'moduleAssignment.condition.DetailAdd','moduleAssignment.condition.DetailEdit'] ,
    stores:['UserStore'],
    models:['UserModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref:'moduleAssignmentList',
            selector:'moduleAssignmentList'
        },
        {
            ref:'deptUserList',
            selector:'moduleAssignmentMain deptUserList'
        },
        {
            ref:'enableOperationList',
            selector:'moduleAssignmentMain enableOperationList'
        },{
            ref:'enableUserPortalList',
            selector:'moduleAssignmentMain enableUserPortalList'
        }
    ],
    init:function(){
        this.control({
            'moduleAssignmentMain > moduleAssignmentList':{
                checkchange : function(record, checked, eOpts ){
                     if(record.get('leaf')) { // 叶子节点
                        if(!checked) {
                            record.set('checked', false);
                        }
                    }else{ // 非叶子节点，把该节点下面的子节点选中或取消选中
                        record.cascadeBy(function(record) {
                            record.set('checked', checked);
                        });
                    }
                },
                selectionchange: function(o, record, eOpts){
                    var userRecord = this.getDeptUserList().getSelectionModel().getSelection()[0];
                    if(userRecord == null){
                        alert("请选择用户!");
                        return;
                    }
                    // 操作列表
                    var store = this.getEnableOperationList().getStore();
                    Ext.apply(store.proxy.extraParams,{});
                    if(Ext.typeOf(record[0]) != 'undefined'){
                        Ext.apply(store.proxy.extraParams,{moduleId:record[0].get('id'),userId:userRecord.get('id')});
                        store.load();
                    }
                    // 用户Portal列表
                    var userPortalStore = this.getEnableUserPortalList().getStore();
                    Ext.apply(userPortalStore.proxy.extraParams,{});
                    if(Ext.typeOf(record[0]) != 'undefined'){
                        Ext.apply(userPortalStore.proxy.extraParams,{moduleId:record[0].get('id'),userId:userRecord.get('id')});
                        userPortalStore.load();
                    }
                }
            },
            'moduleAssignmentMain > deptUserList':{
                beforerender:function(grid,eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{limit:9999999});
                    store.load();
                },
                render : function(grid,eOpts){
                     var userList = grid;
                     var record = userList.getStore().getAt(0);
                     userList.getSelectionModel().selectRange(0,0);
                     userList.fireEvent('itemclick',grid,record);
                 },
                itemclick : function(o,record, item, index, e, eOpts){
                    var store = o.up("moduleAssignmentMain").down("moduleAssignmentList").getStore();
                    Ext.apply(store.proxy.extraParams,{userId:record.get("id")});
                    store.load();
                }
            },
            // 启用模块
            'moduleAssignmentMain moduleAssignmentList button#saveButton':{
                click:function(btn){
                    var tree = btn.up("moduleAssignmentMain").down("moduleAssignmentList");
                    var records = tree.getView().getChecked();
                    var moduleIds = [];
                    Ext.Array.each(records, function(rec){
                        moduleIds.push(rec.get('id'));
                    });
                    var userList = this.getDeptUserList();
                    var record = userList.getSelectionModel().getSelection()[0];
                    var store = tree.getStore();
                    var succ = this.GridDoActionUtil.doAjax('user/moduleAssignForUser',{id:record.get("id"),moduleIds:"["+moduleIds.join(",")+"]"},store,false);
                    if(succ){
                        var userPortalStore = this.getEnableUserPortalList().getStore();
                        userPortalStore.load();
                        this.getEnableOperationList().getStore().load();
                    }
                }
            },
            // 模块分配->用户操作 启用按钮
            'moduleAssignmentMain enableOperationList button#enableButton' : {
                click:function(btn){
                    var userList = this.getDeptUserList();
                    var record = userList.getSelectionModel().getSelection()[0];
                    if(record == null || Ext.typeOf(record) == 'undefined'){
                        alert("请选择用户！");
                        return;
                    }

                    var moduleList = this.getModuleAssignmentList();
                    var moduleRecord = moduleList.getSelectionModel().getSelection()[0];
                    if(moduleRecord == null || Ext.typeOf(moduleRecord) == 'undefined'){
                        alert("请选择模块！");
                        return;
                    }

                    this.GridDoActionUtil.doSave(this.getEnableOperationList(),Object,{userId:record.get('id'),moduleId:moduleRecord.get('id')});
                }
            },
            // 模块分配->用户操作 条件按钮
            'moduleAssignmentMain enableOperationList button#userOptConditionButton' : {
                click:function(btn){
                    var userList = this.getDeptUserList();
                    var userRec = userList.getSelectionModel().getSelection()[0];
                    if(userRec == null || Ext.typeOf(userRec) == 'undefined'){
                        alert("请选择用户！");
                        return;
                    }
                    var operationList = this.getEnableOperationList();
                    var record = operationList.getSelectionModel().getSelection()[0];
                    if(record == null || Ext.typeOf(record) == 'undefined'){
                        alert("请选择操作！");
                        return;
                    }
                    if(record.get('userOperationId') == 0){
                        Ext.Msg.alert("提示","请先启用该操作！");
                        return;
                    }
                    var userOperation = record.get('userOperationId');
                    var user = userRec.get('id');
                    Ext.widget('conditionMain',{
                        userOperation : userOperation,
                        user : user,
                        module : record.get('module')
                    }).show();
                }
            },
            // 模块分配->用户Portal 启用按钮
            'moduleAssignmentMain > tabpanel > enableUserPortalList button#enableUserPortalButton' : {
                click:function(btn){
                    var userList = this.getDeptUserList();
                    var record = userList.getSelectionModel().getSelection()[0];
                    if(record == null || Ext.typeOf(record) == 'undefined'){
                        alert("请选择用户！");
                        return;
                    }
                    var moduleList = this.getModuleAssignmentList();
                    var moduleRecord = moduleList.getSelectionModel().getSelection()[0];
                    if(moduleRecord == null || Ext.typeOf(moduleRecord) == 'undefined'){
                        alert("请选择模块！");
                        return;
                    }

                    this.GridDoActionUtil.doSave(this.getEnableUserPortalList(),Object,{userId:record.get('id'),moduleId:moduleRecord.get('id')});
                }
            },
            // 条件列表
            'conditionMain conditionList':{
                // 渲染前，先根据userOperation和user加载store
                beforerender:function(grid, eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{userOperation:grid.userOperation,user:grid.user});
                    store.load();
                },
                // 选择条件时，根据条件重新加载条件明细
                select:function(o, record, index, eOpts ){
                    var main = eOpts.up('conditionMain');
                    var conditionDetailList = main.down('conditionDetailList');
                    var store = conditionDetailList.getStore();
                    Ext.apply(store.proxy.extraParams,{
                        user: conditionDetailList.user,
                        module: conditionDetailList.module,
                        userOptCondition:record.get('id')
                    });
                    store.load();
                }
            },
            'conditionMain conditionDetailList':{
                // 渲染前，先根据userOperation和user加载store
                beforerender:function(grid, eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{user: 0,module: 0,userOptCondition:0});
                    store.load();
                }
            },
            // 条件新增页面初始化
            'conditionMain conditionList button#addButton':{
                click:function(btn){
                    var list = btn.up('baseList');
                    Ext.widget('conditionAdd',{
                        userOperation : list.userOperation,
                        user: list.user
                    }).show();
                }
            },
            // 条件明细新增页面初始化
            'conditionMain conditionDetailList button#addButton':{
                click:function(btn){
                    var main = btn.up('conditionMain');
                    var conditionList = main.down('conditionList');
                    var conditionRecord = conditionList.getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(conditionRecord) == 'undefined'){
                        Ext.Msg.alert('提示','请选择条件分组！');
                        return;
                    }
                    var conditionDetailList = main.down('conditionDetailList');
                    Ext.widget('conditionDetailAdd',{
                        userOptCondition : conditionRecord.get('id'),
                        module : conditionDetailList.module,
                        user : conditionDetailList.user
                    }).show();
                }
            },
            // 条件明细修改页面初始化
            'conditionMain conditionDetailList button#updateButton':{
                click:function(btn){
                    var me = this;
                    var main = btn.up('conditionMain');
                    var conditionDetailList = main.down('conditionDetailList');
                    var record = conditionDetailList.getSelectionModel().getSelection()[0];
                    var win = Ext.widget('conditionDetailEdit',{
                        module : conditionDetailList.module,
                        user : conditionDetailList.user,
                        record : record
                    }).show();
                    win.down('form').loadRecord(record);
                }
            },
            // 条件明细新增页面
            'conditionDetailAdd':{
                beforerender: function (form) {
                    // 渲染前，根据moduleId加载对应的字段名称
                    this.loadConditionDetailFieldNames(form);
                }
            },
            // 条件明细修改页面
            'conditionDetailEdit':{
                beforerender: function (form) {
                    // 渲染前，根据moduleId加载对应的字段名称
                    this.loadConditionDetailFieldNames(form);
                }
            },
            'conditionDetailAdd combo[name=fieldName]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.changeValueComponent(o,newValue);
                }
            },
            'conditionDetailEdit combo[name=fieldName]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.changeValueComponent(o,newValue);
                }
            },
            'conditionDetailAdd combo[name=operator]':{
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    var fieldCombo = form.down('combo[name=fieldName]');
                    this.changeValueComponent(fieldCombo,fieldCombo.getValue());
                }
            },
            'conditionDetailEdit combo[name=operator]':{
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    var fieldCombo = form.down('combo[name=fieldName]');
                    this.changeValueComponent(fieldCombo,fieldCombo.getValue());
                }
            }
        });
    },
    // 条件，根据moduleId获取可选字段
    loadConditionDetailFieldNames:function(form){
        Ext.apply(form.down("field[name='fieldName']"), {
            store: Ext.create('Ext.data.Store', {
                fields: ['fieldName', 'fieldText', 'dbType','dict','toMany'],
                proxy: {
                    type: 'ajax',
                    autoSync: true,
                    api: {
                        read: 'userOptConditionDetail/loadFieldList?user='+form.user+"&module="+form.module
                    },
                    reader: {
                        type: 'json',
                        root: 'data',
                        successProperty: 'success'
                    },
                    simpleSortMode: true
                },
                autoLoad: true,
                listeners:{
                    load:function(o, records, successful, eOpts){
                        if(successful == true){
                            if(form.record){
                                form.down("field[name='fieldName']").setValue(form.record.get('fieldName'));
                            }
                        }
                    }
                }
            })
        });
    },
    //条件明细新增、修改页面，选择字段后加改变值得组件类型
    changeValueComponent:function(o,newValue){
        if(newValue == null) return;
        var win = o.up('window');
        var form = o.up('form');
        var record = o.findRecordByValue(newValue); // 当前下拉选中的记录
        var dbType = record.get('dbType');
        var dict = record.get('dict');
        var operatorComponent = form.down('combo[name=operator]');
        var operator = form.down('combo[name=operator]').getValue();
        var allStore = [['==', '等于'],['!=', '不等于'],['>', '大于'],['>=', '大于等于'],['<', '小于'],['<', '小于等于'],['in', '包含'],['notin', '不包含']]
        var firstStore = [['==', '等于'],['!=', '不等于']];
        var middleStore = [['>', '大于'],['>=', '大于等于'],['<', '小于'],['<', '小于等于']];
        var endStore = [['in', '包含'],['notin', '不包含']];
        win.down('checkbox[name=valueMe]').setVisible(false);
        if(dbType == 'java.lang.String' || newValue == 'id'){ // 字符型
            var valueComponent = Ext.widget('textfield',{
                name: "value",
                itemId : 'value',
                fieldLabel: "值",
                allowBlank : false
            });
            operatorComponent.getStore().removeAll();
            operatorComponent.getStore().add(allStore);
        }else if(dbType == 'java.lang.Integer') { // 数值型
            if (dict) { // 下拉
                var multiSelect = false;
                if (operator == 'in' || operator == 'notin') multiSelect = true;
                var valueComponent = Ext.widget('combo', {
                    name: "value",
                    itemId: 'value',
                    fieldLabel: "值",
                    valueField: 'itemId',
                    displayField: 'text',
                    multiSelect: multiSelect,
                    store: Ext.create('Ext.data.Store', {
                        fields: ['itemId', 'text'],
                        proxy: {
                            type: 'ajax',
                            api: {
                                read: 'dataDictItem/list?dict=' + dict
                            },
                            reader: {
                                type: 'json',
                                root: 'data',
                                successProperty: 'success'
                            },
                            simpleSortMode: true
                        },
                        autoLoad: true
                    })
                });
                operatorComponent.getStore().removeAll();
                operatorComponent.getStore().add(allStore);
            } else { // 普通数值
                var valueComponent = Ext.widget('numberfield', {
                    name: "value",
                    itemId: 'value',
                    fieldLabel: "值",
                    allowBlank: false
                });
                operatorComponent.getStore().removeAll();
                operatorComponent.getStore().add(firstStore);
                operatorComponent.getStore().add(middleStore);
            }
        }else if(dbType == 'java.lang.Boolean'){
            var valueComponent = Ext.widget('checkbox',{
                fieldLabel : '值',
                name : 'value',
                itemId : 'value',
                allowBlank: false,
                boxLabelAlign: 'before',
                inputValue: 'true',
                uncheckedValue:'false'
            });
            operatorComponent.getStore().removeAll();
            operatorComponent.getStore().add(firstStore);
        }else if(dbType == 'java.util.Date'){ // 日期型
            var valueComponent = Ext.widget('datetimefield',{
                name: "value",
                itemId : 'value',
                fieldLabel: "值",
                format: 'Y-m-d H:m:s',
                allowBlank : false,
                value:new Date()
            });
            operatorComponent.getStore().removeAll();
            operatorComponent.getStore().add(firstStore);
            operatorComponent.getStore().add(middleStore);
        }else if(dbType == 'com.uniproud.wcb.Employee'){
            var valueComponent = Ext.widget('baseMultiSelectTextareaField',{
                name: "value.name",
                fromType:'admin',
                itemId : 'value',
                url:'employee/listForAdmin?user='+win.user,
                fieldLabel: "值",
                allowBlank : false,
                store:'EmployeeStore',
                hiddenName:'value',
                enableComplexQuery : false
            });
            operatorComponent.getStore().removeAll();
            if(record.get('toMany') == true){
                valueComponent.hidden = true;
                operatorComponent.getStore().add(endStore);
                win.down('checkbox[name=valueMe]').setVisible(true);
            }else if(record.get('toMany') == false){
                operatorComponent.getStore().add(firstStore);
                operatorComponent.getStore().add(endStore);
                if(operator == 'in' || operator == 'notin'){
                    valueComponent.hidden = false;
                    win.down('checkbox[name=valueMe]').setVisible(true);
                }else{
                    valueComponent.hidden = true;
                    win.down('checkbox[name=valueMe]').setVisible(true);
                }
            }
        }
        // 如果要是变换后找不到，就默认选中第一个
        if(operatorComponent.getStore().findRecord('field1',operator) == null){
            operatorComponent.setValue(operatorComponent.getStore().getAt(0).get('field1'));
        }
        form.remove(form.down('[itemId=value]'));
        form.insert(2,valueComponent);
        this.initEditForm(form,valueComponent);
    },
    initEditForm:function(form,valueComponent){
        // 改变之后要把record的值附上去
        var win = form.up('window');
        var record = win.record;
        if(record) {
            if (valueComponent.xtype == 'baseMultiSelectTextareaField') {
                var recVal = record.get('value');
                var recText = record.get('valueText');
                var index = recVal.indexOf('me');
                if(index > -1){
                    if(recVal.indexOf(",me")){
                        recVal = recVal.replace(",me","");
                        recText = recText.replace(",me","");
                    }else if(recVal.indexOf("me,")){
                        recVal = recVal.replace("me,","");
                        recText = recText.replace("me,","");
                    }else{
                        recVal = recVal.replace("me","");
                        recText = recText.replace("me","");
                    }
                }
                valueComponent.down('hiddenfield[name=value]').setValue(recVal);
                valueComponent.down('textareafield[name=value.name]').setValue(recText);
                if(index > -1){
                    win.down('checkbox[name=valueMe]').setValue("me");
                }
            }else {
                valueComponent.setValue(record.get('value'));
            }
            delete win.record;
        }
    }
});