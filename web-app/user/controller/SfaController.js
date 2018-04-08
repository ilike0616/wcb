/**
 * Created by like on 2015/8/27.
 */
Ext.define('user.controller.SfaController', {
    extend : 'Ext.app.Controller',
    views : ['sfa.List', 'sfa.event.List','sfa.event.Add','sfa.event.Edit','sfa.ModuleCombo','sfa.BindingSfa',
            'sfa.execute.Main','sfa.execute.SfaList','sfa.execute.EventList','sfa.execute.EventExecuteList','sfa.execute.EventExecuteView'],
    stores : [ 'SfaStore','UserModuleStore','SfaEventStore','SfaExecuteStore','SfaEventExecuteStore'],
    models:['UserModuleModel','SfaEventModel','SfaEventModel','SfaEventExecuteModel'],
    GridDoActionUtil : Ext.create("admin.util.GridDoActionUtil"),
    refs : [{
        ref: 'sfaList',
        selector: 'sfaList'
    }],
    init : function() {
        this.control({
            'sfaList button[operationId=sfa_set]':{
                click:function(btn){
                    var vw = btn.vw,baseList=btn.up('baseList');
                    var view = Ext.widget('sfaEventList',{listDom:baseList}).show();
                }
            },
            'sfaList button[operationId=sfa_enabled]':{
                click:function(btn) {
                    var grid = btn.up('baseList');
                    var store = grid.getStore();
                    var records = grid.getSelectionModel().getSelection();
                    var data = [];
                    Ext.Array.each(records, function (model) {
                        if (model.get('id')) {
                            data.push(Ext.JSON.encode(model.get('id')));
                        }
                    });
                    Ext.Ajax.request({
                        url: store.getProxy().api['enable'],
                        params: {ids: "[" + data.join(",") + "]"},
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            if (d.success) {
                                if (d.success) {
                                    Ext.example.msg('提示', '已启用');
                                } else {
                                    console.info(d.errors);
                                    Ext.example.msg('提示', '启用失败！');
                                }
                            }
                        }, failure: function (response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                        }
                    });
                }
            },
            'sfaList button[operationId=sfa_disable]':{
                click:function(btn) {
                    var grid = btn.up('baseList');
                    var store = grid.getStore();
                    var records = grid.getSelectionModel().getSelection();
                    var data = [];
                    Ext.Array.each(records, function (model) {
                        if (model.get('id')) {
                            data.push(Ext.JSON.encode(model.get('id')));
                        }
                    });
                    Ext.Ajax.request({
                        url: store.getProxy().api['disable'],
                        params: {ids: "[" + data.join(",") + "]"},
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            if (d.success) {
                                if (d.success) {
                                    Ext.example.msg('提示', '已停用');
                                } else {
                                    console.info(d.errors);
                                    Ext.example.msg('提示', '停用失败！');
                                }
                            }
                        }, failure: function (response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                        }
                    });
                }
            },
            'sfaEventList grid':{
                beforerender:function(grid,eOpts){
                    var store = grid.store;
                    Ext.apply(store.proxy.extraParams,{sfa:this.getSfaId()});
                },
                select:function(selectModel, record, index, eOpts){
                    if(index==0){
                        eOpts.up('sfaEventList').down("button#up").setDisabled(true);
                    }
                    if(eOpts.up('grid').getStore().getCount()-1==index){
                        eOpts.up('sfaEventList').down("button#down").setDisabled(true);
                    }
                }
            },
            'sfaEventList button#up':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index - 1, rec);
                    store.each(function(record,index){
                        record.set('index',index+1);
                    });
                    grid.getSelectionModel().select(rec);

                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'sfaEventList button#down':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index + 1, rec);
                    store.each(function(record,index){
                        record.set('index',index+1);
                    });
                    grid.getSelectionModel().select(rec);
                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'sfaEventList button#save':{
                click:function(btn){
                    this.GridDoActionUtil.doSave(btn.up('grid'),btn);
                }
            },
            'sfaEventList button#addButton':{
                click:function(btn){
                    var baseList=btn.up('baseList');
                    var view = Ext.widget('sfaEventAdd',{listDom:baseList}).show();
                    view.down('field[name=sfa]').setValue(this.getSfaId());
                }
            },
            'sfaEventAdd':{
                beforerender:function(win,eOpts){
                    var dateFieldStore = win.down('field[name=dateField]').store;
                    Ext.apply(dateFieldStore.proxy.extraParams,{moduleId:this.getSfaModuleId(),dbType:'java.util.Date'});
                    var dateFieldStore1 = win.down('field[name=dateFieldCycle]').store;
                    Ext.apply(dateFieldStore1.proxy.extraParams,{moduleId:this.getSfaModuleId(),dbType:'java.util.Date'});
                    Ext.apply(win.down('field[name=notifyContentTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=smsContentTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=emailSubjectTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=emailContentTemplate]'),{moduleId: this.getSfaModuleId()});
                },
                afterrender:function(win,eOppts){
                    var acceptors = win.down('baseMultiSelectTextareaField');
                    acceptors.hide();
                }
            },
            'sfaEventAdd field[name=receiverType][checked=true]':{
                change:function(field, newValue, oldValue, eOpts){
                    this.changeReceiver(field);
                }
            },
            'sfaEventAdd button#save':{
                click:function(btn){
                    var form = btn.up('form');
                    if(this.validForm(form) == false){
                        return;
                    }
                    this.GridDoActionUtil.doInsert(btn.up('window').listDom,btn.up('form'),btn.up('window'),{});
                }
            },
            'sfaEventList button#updateButton':{
                click:function(btn){
                    var baseList=btn.up('baseList'),
                        record = baseList.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('sfaEventEdit',{listDom:baseList}).show();
                    if(view.down('form'))view.down('form').loadRecord(record);
                    var acceptorsId = [],acceptorsName = [];
                    Ext.each(record.get('acceptors'),function(item){
                        acceptorsId.push(item.id);
                        acceptorsName.push(item.name);
                    })
                    if(acceptorsId.length > 0){
                        acceptorsId.join(",");
                        acceptorsName.join(",");
                    }
                    view.down('hiddenfield[name=acceptors]').setValue(acceptorsId);
                    view.down('baseMultiSelectTextareaField textareafield[name=acceptors.name]').setValue(acceptorsName);
                }
            },
            'sfaEventEdit':{
                beforerender:function(win,eOpts){
                    var dateFieldStore = win.down('field[name=dateField]').store;
                    Ext.apply(dateFieldStore.proxy.extraParams,{moduleId:this.getSfaModuleId(),dbType:'java.util.Date'});
                    dateFieldStore.load();
                    var dateFieldStore1 = win.down('field[name=dateFieldCycle]').store;
                    Ext.apply(dateFieldStore1.proxy.extraParams,{moduleId:this.getSfaModuleId(),dbType:'java.util.Date'});
                    dateFieldStore1.load();
                    Ext.apply(win.down('field[name=notifyContentTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=smsContentTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=emailSubjectTemplate]'),{moduleId: this.getSfaModuleId()});
                    Ext.apply(win.down('field[name=emailContentTemplate]'),{moduleId: this.getSfaModuleId()});
                },
                afterrender:function(win,eOppts){
                    var receiverType = win.down('field[name=receiverType][checked=true]').inputValue;
                    var acceptors = win.down('baseMultiSelectTextareaField');
                    if(receiverType != 3){
                        acceptors.hide();
                    }
                }
            },
            'sfaEventEdit field[name=receiverType][checked=true]':{
                change:function(field, newValue, oldValue, eOpts){
                    this.changeReceiver(field);
                }
            },
            'sfaEventEdit button#save':{
                click:function(btn){
                    var form = btn.up('form');
                    if(this.validForm(form) == false){
                        return;
                    }
                    this.GridDoActionUtil.doUpdate(btn.up('window').listDom,btn.up('form'),btn.up('window'),{});
                }
            },
            //手动绑定SFA
            'button[operationId$=_binding_sfa]':{
                click:function(btn,e,eOpts,rightClickRec){
                    var baseList = btn.up('baseList'),
                        moduleId = baseList.moduleId,
                        record = baseList.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('sfaBindingSfa',{moduleId:moduleId,linkId:record.get('id'),linkName:record.get(baseList.alertName)});
                    view.show();
                }
            },
            //初始化页面可以绑定的SFA方案
            'sfaBindingSfa':{
                afterrender:function(win){
                    var moduleId = win.moduleId,
                        linkId = win.linkId;
                    Ext.Ajax.request({
                        params : {moduleId : moduleId,linkId : linkId},
                        url:'sfa/getHandBingList',
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var result = Ext.JSON.decode(response.responseText);
                            if(result != ""){
                                var data = result.data;
                                var items = [];
                                Ext.Array.each(data,function(it){
                                    items.push(it);
                                })
                                win.down("checkboxgroup[name=chooseSfa]").removeAll();
                                win.down("checkboxgroup[name=chooseSfa]").add(items);
                            }
                        },
                        failure:function(response,opts){
                        }
                    })
                }
            },
            'sfaBindingSfa button#save':{
                click:function(btn){
                    var win = btn.up('baseWin');
                    // 必选条件下拉框的值
                    var moduleId = win.moduleId;
                    var linkId = win.linkId;
                    // checkbox组
                    var checkboxGroup = win.down("fieldset checkboxgroup[name=chooseSfa]");
                    // 选中的checkbox的值
                    var checkedValue = checkboxGroup.getValue();
                    console.info(checkedValue.field);
                    if(checkedValue != null && checkedValue != 'undefined'){
                        checkedValue = {moduleId:moduleId,linkId:linkId,enableSfas: checkedValue};
                        this.GridDoActionUtil.doAjax('sfa/handBingSfa',checkedValue,null);
                        win.close();
                    }else{
                        Ext.example.msg('提示', '请选择要启用的SFA！');
                    }
                }
            },
            //360度视图SFA中关联方案列表
            'sfaExecuteMain sfaExecuteSfaList':{
                select:function(selectModel, record, index, eOpts){
                    var sfa = selectModel.view.up('baseList');
                    var store = sfa.up('sfaExecuteMain').down('sfaExecuteEventList').getStore();
                    Ext.apply(store.proxy.extraParams,{sfa:record.get('sfa.id')});
                    store.load();
                    var store1 = sfa.up('sfaExecuteMain').down('sfaExecuteEventExecuteList').getStore();
                    Ext.apply(store1.proxy.extraParams,{sfaExecute:record.get('id')});
                }
            },
            //360度视图SFA中事件列表页面点击加载执行明细
            'sfaExecuteMain sfaExecuteEventList':{
                select:function(selectModel, record, index, eOpts){
                    var event = selectModel.view.up('baseList');
                    var store = event.up('sfaExecuteMain').down('sfaExecuteEventExecuteList').getStore();
                    Ext.apply(store.proxy.extraParams,{sfaEvent:record.get('id')});
                    store.load();
                }
            },
            //360度视图SFA中执行明细列表双击选中时打开查看页面
            'sfaExecuteMain sfaExecuteEventExecuteList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    var view = Ext.widget('sfaExecuteEventExecuteView');
                    view.down('form').loadRecord(record);
                    var acceptorsName = [];
                    Ext.each(record.get('employees'),function(item){
                        acceptorsName.push(item.name);
                    });
                    view.down('form').down('field[name=employees.name]').setValue(acceptorsName.join(","));
                    view.show()
                }
            },
            //360度视图SFA中查看事件执行明细页面
            'sfaExecuteEventExecuteView':{
                afterrender:function(win,eOppts){
                    var receiverType = win.down('field[name=receiverType][checked=true]').inputValue;
                    if(receiverType == 2){
                        win.down('field[name=employees.name]').hide();
                    }else{
                        win.down('field[name=customer.name]').hide();
                    }
                }
            }
        });
    },
    validForm:function(form){
        var valid = true;
        var dateType = form.down('field[name=dateType]').getGroupValue();
        if(dateType==1){
            var startDate = form.down('field[name=startDate]'),
                endDate = form.down('field[name=endDate]');
            if(!startDate.value){
                startDate.setActiveError("不能为空");
                valid = false;
            }
            if(!endDate.value){
                endDate.setActiveError("不能为空");
                valid = false;
            }
        } else if(dateType == 2){
            var dateField = form.down('field[name=dateField]');
            if(!dateField.getValue()){
                dateField.setActiveError('请选择');
                valid = false;
            }
            var lastingDays = form.down('field[name=lastingDays]');
            if(!lastingDays.getValue()){
                lastingDays.setActiveError("不能为空");
                valid = false;
            }
        } else if(dateType == 3){
            var dateField = form.down('field[name=dateFieldCycle]');
            if(!dateField.getValue()){
                dateField.setActiveError('请选择');
                valid = false;
            }
            var interval = form.down('field[name=interval]');
            if(!interval.getValue()){
                interval.setActiveError("不能为空");
                valid = false;
            }
            var lastingDays = form.down('field[name=lastingDaysCycle]');
            if(!lastingDays.getValue()){
                lastingDays.setActiveError("不能为空");
                valid = false;
            }
            var cycleTimes = form.down('field[name=cycleTimes]');
            if(!cycleTimes.getValue()){
                cycleTimes.setActiveError("不能为空");
                valid = false;
            }
        }
        if(form.down('field[name=isNotify]').getValue()){
            var content = form.down('field[name=notifyContentTemplate]');
            if(!content.getValue()){
                content.setActiveError("不能为空");
                valid = false;
            }
        }
        if(form.down('field[name=isSms]').getValue()){
            var content = form.down('field[name=smsContentTemplate]');
            if(!content.getValue()){
                content.setActiveError("不能为空");
                valid = false;
            }
        }
        if(form.down('field[name=isEmail]').getValue()){
            var subject = form.down('field[name=emailSubjectTemplate]');
            if(!subject.getValue()){
                subject.setActiveError("不能为空");
                valid = false;
            }
            var content = form.down('field[name=emailContentTemplate]');
            if(!content.getValue()){
                content.setActiveError("不能为空");
                valid = false;
            }
        }
        return valid;
    },
    changeReceiver:function(field){
        var value = field.inputValue,
            form = field.up('form'),
            isNotify = form.down('field[name=isNotify]'),
            notify = form.down('field[name=notifyContentTemplate]'),
            acceptors = form.down('baseMultiSelectTextareaField');
        if(value ==2){
            isNotify.setValue(false);
            isNotify.hide();
            notify.setValue('');
            notify.hide();
        }else{
            isNotify.show();
            notify.show();
        }
        if(value ==3){
            acceptors.show();
        }else{
            acceptors.hide()
        }
    },
    getSfaId:function(){
        var record = this.getSfaList().getSelectionModel().getSelection()[0];
        return record.get('id');
    },
    getSfaModuleId:function(){
        var record = this.getSfaList().getSelectionModel().getSelection()[0];
        return record.get('module').moduleId;
    }
})