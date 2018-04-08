/**
 * Created by like on 2015-04-13.
 */
Ext.define('user.controller.NotifyModelController', {
    extend: 'Ext.app.Controller',
    views: ['notifyModel.Main', 'notifyModel.List', 'module.List',
        'notifyModel.FilterMain', 'notifyModel.FilterTree', 'notifyModel.FilterDetail',
        'notifyModel.FilterAdd', 'notifyModel.FilterEdit',
        'notifyModel.FilterDetailAdd','notifyModel.FilterDetailEdit'
        ],
    stores: ['NotifyModelStore', 'NotifyModelFilterStore', 'NotifyModelFilterDetailStore','UserFieldStore'],
    models: ['NotifyModelFilterModel', 'NotifyModelFilterDetailModel','UserFieldModel'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'moduleList',
            selector: 'notifyModelMain moduleList'
        },
        {
            ref: 'notifyModelList',
            selector: 'notifyModelMain notifyModelList'
        },
        {
            ref: 'notifyModelFilterTree',
            selector: 'notifyModelFilterMain notifyModelFilterTree'
        },{
            ref: 'notifyModelFilterDetail',
            selector: 'notifyModelFilterMain notifyModelFilterDetail'
        }
    ],
    init: function () {
        this.control({
            'notifyModelMain moduleList': {                     //模板树形结构
                select: function (o, record, index, eOpts) {
                    var store = this.getNotifyModelList().getStore();
                    Ext.apply(store.proxy.extraParams, {'module.moduleId': record.get('moduleId')});        //根据moduleId查询触发模型
                    //module传值
                    Ext.apply(this.getNotifyModelList(), {notifyModule: record.get('moduleId')});
                    store.load();
                },
                deselect:function (o, record, index, eOpts) {
                    Ext.Array.forEach(this.getNotifyModelList().query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            },
            'baseWinForm[moduleId=notify_model][optType=add]': {
                beforerender: function (form) {
                    this.loadNotifyFieldData(form);
                },
                afterrender: function (form) {
                    //moduleId传递给模型新增页面
                    form.down("field[name='module.moduleId']").setValue(this.getModuleId());
                }
            },
            'baseWinForm[moduleId=notify_model][optType=update]': {
                beforerender: function (form) {
                    this.loadNotifyFieldData(form);
                }
            },
            //点击“设置”按钮，模型ID传递给条件组树
            'notifyModelMain notifyModelList button[operationId=notify_model_filter]': {
                click: function (btn) {
                    var vw = btn.vw, view, baseList;
                    baseList = btn.up('baseList');
                    view = Ext.widget(vw, {listDom: baseList, viewId: vw, optType: btn.optType}).show();
                    var store = view.down('notifyModelFilterTree').getStore();
                    Ext.apply(store.proxy.extraParams, {'notifyModel': this.getNotifyModel()});
                    store.load();
                }
            },
            //点击打开新增条件组
            'notifyModelFilterMain notifyModelFilterTree button[operationId=filter_add]': {
                click: function (btn) {
                    //如果已有条件组，则必须选择一条作为新增条件组的父级
                    var win = Ext.widget('notifyModelFilterAdd').show();
                    win.down('form').down("field[name='moduleId']").setValue(this.getModuleId());     //赋值moduleId
                    win.down('form').down("field[name='notifyModel']").setValue(this.getNotifyModel());   //传递模型ID
                }
            },
            //点击打开修改条件组
            'notifyModelFilterMain notifyModelFilterTree button[operationId=filter_update]': {
                click: function (btn) {
                    var win = Ext.widget('notifyModelFilterEdit').show();
                    var record = this.getNotifyModelFilterTree().getSelectionModel().getSelection()[0];
                    if (win.down('form'))win.down('form').loadRecord(record);
                    win.down('form').down("field[name='moduleId']").setValue(this.getModuleId());     //赋值moduleId
                    win.down('form').down("field[name='notifyModel']").setValue(this.getNotifyModel());    //传递模型ID
                }
            },
            //新增页面选父级条件组时增加条件notifyModel
            'notifyModelFilterAdd': {
                beforerender: function (form) {
                    this.notifyModelToFilterAdd(form);
                }
            },
            //修改页面选父级条件组时增加条件notifyModel
            'notifyModelFilterEdit': {
                beforerender: function (form) {
                    this.notifyModelToFilterAdd(form);
                }
            },
            //选中条件组时，加载条件明细
            'notifyModelFilterTree':{
                select:function(selectModel, record, index, eOpts){
                    Ext.Array.forEach(eOpts.up('notifyModelFilterMain').query('button[operationId=filter_detail_add]'),function(o,index){
                        o.setDisabled(false);           //新增条件明细按钮设置为可点击
                    });
                    var record = this.getNotifyModelFilterTree().getSelectionModel().getSelection()[0];             //选中的条件组
                    var store = this.getNotifyModelFilterDetail().getStore();
                    Ext.apply(store.proxy.extraParams,{notifyModelFilter:record.get('id')});         //根据条件组加载条件明细
                    store.load();
                }, deselect:function(selectModel, record, index, eOpts){
                    Ext.Array.forEach(eOpts.up('notifyModelFilterMain').query('button[operationId=filter_detail_add]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            },
            //条件明细列表
            'notifyModelFilterMain notifyModelFilterDetail':{
                beforerender: function (form) {                           //防止打开时显示上次打开的条件明细
                    var store = this.getNotifyModelFilterDetail().getStore();
                    Ext.apply(store.proxy.extraParams, {notifyModelFilter: '0'});
                }
            },
            'notifyModelFilterDetailAdd':{
                beforerender: function (form) {
                    this.loadFilterDetailFieldNames(form);
                }
            },
            'notifyModelFilterDetailEdit':{
                beforerender: function (form) {
                    this.loadFilterDetailFieldNames(form);
                }
            },
            //打开新增条件窗口
            'notifyModelFilterMain notifyModelFilterDetail button[operationId=filter_detail_add]': {
                click: function (btn) {
                    var view = Ext.widget('notifyModelFilterDetailAdd').show();
                    view.down('form').down('field[name=notifyModelFilter]').setValue(this.getNotifyModelFilter());
                }
            },
            //条件明细，选择字段
            'notifyModelFilterDetailAdd field[name=fieldName]':{
                change:function( me, newValue, oldValue, eOpts ){
                    this.changeFilterDetailFieldNames(me, newValue);
                }
            },
            'notifyModelFilterDetailEdit field[name=fieldName]':{
                change:function( me, newValue, oldValue, eOpts ){
                    this.changeFilterDetailFieldNames(me, newValue);
                }
            },
            //修改条件明细初始化
            'notifyModelFilterMain notifyModelFilterDetail button[operationId=filter_detail_update]': {
                click: function (btn) {
                    var record = this.getNotifyModelFilterDetail().getSelectionModel().getSelection()[0];
                    var view = Ext.widget('notifyModelFilterDetailEdit',{
                        record : record
                    }).show();
                    view.down('form').loadRecord(record);
                }
            },
            //仅需修改，选择时处理页面变化
            'notifyModelFilterDetailAdd field[name=justEdit]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.changeJustEdit(o.up('form'),newValue);
                }
            },
            'notifyModelFilterDetailEdit field[name=justEdit]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.changeJustEdit(o.up('form'),newValue);
                }
            }
        });
    },
    /**
     * 控制是否仅需修改时页面变化
     * @param form   表单
     * @param justEdit  是否仅需修改
     */
    'changeJustEdit':function(form,justEdit){
        form.down('field[name=justEdit]').setValue(justEdit);
        var operate = form.down('field[name=operator]'),
            expectValue = form.down('field[name=expectValue]');
        if(justEdit){
            operate.setDisabled(true);
            expectValue.setDisabled(true);
        }else{
            operate.setDisabled(false);
            expectValue.setDisabled(false);
        }
    },
    //条件明细新增、修改页面，选择字段后加载初始化期望值
    'changeFilterDetailFieldNames':function(o, newValue){
        if(newValue == null) return;
        var win = o.up('window');
        var form = o.up('form');
        var record = o.findRecordByValue(newValue); // 当前下拉选中的记录
        var dbType = record.get('dbType');
        var dict = record.get('dict');
        var operator = form.down('combo[name=operator]').getValue();
        if(dict){
            var multiSelect = false;
            if(operator == 'in' || operator == 'notin') multiSelect = true;
            var dictArray = []
            Ext.Ajax.request({
                url:'dataDictItem/list',
                params:{dict:dict},
                method:'POST',
                timeout:20000,
                async:false,
                success:function(response,opts){
                    var d = Ext.JSON.decode(response.responseText);
                    if(d.success){
                        //dictArray = Ext.Array.toArray(d.data);
                        Ext.Array.each(d.data,function(item,index,data){
                            dictArray.push([item.itemId,item.text])
                        });
                    }
                }
            });
            var valueComponent = Ext.widget('combo',{
                name: "expectValue",
                itemId : 'expectValue',
                fieldLabel: "预期值",
                valueField:'itemId',
                displayField:'text',
                multiSelect: multiSelect,
                store: dictArray,
                allowBlank : false
            });
            var operateList = [['==', '等于'],['!=', '不等于'],['in','包含'],['notin','不包含']]
        }else if(dbType == 'java.lang.String' || newValue == 'id'){
            var valueComponent = Ext.widget('textfield',{
                name: "expectValue",
                itemId : 'expectValue',
                fieldLabel: "预期值",
                allowBlank : false
            });
            var operateList = [['==', '等于'],['!=', '不等于'],['in','包含'],['notin','不包含']]
        }else if(dbType == 'java.util.Date'){
            var valueComponent = Ext.widget('datetimefield',{
                name: "expectValue",
                itemId : 'expectValue',
                fieldLabel: "预期值",
                format: 'Y-m-d H:m:s',
                allowBlank : false,
                value:new Date()
            });
            var operateList = [['==', '等于'],['!=', '不等于'],['>', '大于'],['>=', '大于等于'],['<', '小于'],['<', '小于等于']]
        }else if(dbType == 'java.lang.Integer' || dbType == 'java.lang.Long' || dbType == 'java.lang.Double' || dbType == 'java.math.BigDecimal'){
            var valueComponent = Ext.widget('numberfield',{
                name: "expectValue",
                itemId : 'expectValue',
                fieldLabel: "预期值",
                allowBlank : false
            });
            var operateList = [['==', '等于'],['!=', '不等于'],['>', '大于'],['>=', '大于等于'],['<', '小于'],['<', '小于等于']]
        }else if(dbType == 'java.lang.Boolean'){
            var valueComponent = Ext.widget('checkbox',{
                fieldLabel:"预期值",
                name:"expectValue",
                itemId : 'expectValue',
                uncheckedValue:false,
                inputValue:true
            });
            var operateList = [['==', '等于'],['!=', '不等于']]
        }
        if(dict){//赋值是否关联数据字典
            form.down('[name=isDict]').setValue(true);
        }else{
            form.down('[name=isDict]').setValue(false);
        }
        if(operateList){//重置运算符
            form.down('combo[name=operator]').getStore().loadData(operateList);
        }
        form.down('field[name=dbType]').setValue(dbType);//赋值dbType
        if(valueComponent){//基本数据类型修改期望值组件
            form.remove(form.down('[itemId=expectValue]'));
            form.insert(7,valueComponent);
            // 改变之后要把record的值附上去
            var record = form.up('window').record;//修改前的数据
            if(record && record.get('fieldName')==form.down('[name=fieldName]').getValue()){//如果当前选中的字段是原始字段，复原数据
                if(dict){
                    form.down('[itemId=expectValue]').setValue(Number(record.get('expectValue')));
                }else{
                    form.down('[itemId=expectValue]').setValue(record.get('expectValue'));
                }
                form.down('combo[name=operator]').setValue(record.get('operator'));
            }else{
                form.down('combo[name=operator]').setValue('==');
            }
            this.changeJustEdit(form,false);
        }else{
            this.changeJustEdit(form,true);//非基本类型字段，仅支持“仅需修改”的情况
        }
    },
    //条件，根据moduleId获取触发的可选字段
    'loadFilterDetailFieldNames':function(form){
        Ext.apply(form.down("field[name='fieldName']"), {
            store: Ext.create('Ext.data.Store', {
                fields: ['fieldName', 'fieldText', 'dbType','dict'],
                proxy: {
                    type: 'ajax',
                    autoSync: true,
                    api: {
                        read: 'notifyModelFilterDetail/loadFieldList?moduleId='+this.getModuleId()
                    },
                    reader: {
                        type: 'json',
                        root: 'data',
                        successProperty: 'success',
                        totalProperty: 'total'
                    },
                    simpleSortMode: true
                },
                autoLoad: false,
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
        form.down("field[name='fieldName']").getStore().load();
    },
    //配置加载消息接受者可选列表
    'loadNotifyFieldData': function (form) {
        Ext.apply(form.down("field[name='notifyField']"), {
            store: Ext.create('Ext.data.Store', {
                fields: ['fieldName', 'fieldText', 'isNotifyMany'],
                proxy: {
                    type: 'ajax',
                    api: {
                        read: 'notifyModel/getNotifyFieldList'
                    },
                    reader: {
                        type: 'json',
                        root: 'data',
                        successProperty: 'success',
                        totalProperty: 'total'
                    },
                    simpleSortMode: true
                },
                autoLoad: false
            })
        });
        Ext.apply(form.down("field[name='notifyField']").getStore().proxy.extraParams, {moduleId: this.getModuleId()});
        Ext.apply(form.down("field[name='subjectTemplate']"),{moduleId: this.getModuleId()});
        Ext.apply(form.down("field[name='contentTemplate']"),{moduleId: this.getModuleId()});
    },
    //新增、修改页面加载上级条件组时携带模型的ID
    'notifyModelToFilterAdd': function (form) {
        var store = form.down('baseComboBoxTree').getStore();
        Ext.apply(store.proxy.extraParams, {'notifyModel': this.getNotifyModel()});
        store.load();
    },
    //获取moduleId
    'getModuleId':function (){
        var moduleId =this.getNotifyModelList().notifyModule;
        return moduleId;
    },
    //获取操作模型的ID
    'getNotifyModel': function () {
        var record = this.getNotifyModelList().getSelectionModel().getSelection()[0];
        return record.get('id');
    },
    //获取当前选中的条件组
    'getNotifyModelFilter':function(){
        var record = this.getNotifyModelFilterTree().getSelectionModel().getSelection()[0];
        return record.get('id');
    }
})