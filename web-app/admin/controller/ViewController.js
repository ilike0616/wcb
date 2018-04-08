/**
 * Created by shqv on 2014-8-27.
 */
Ext.define('admin.controller.ViewController',{
    extend : 'Ext.app.Controller',
    views:['view.Main','view.List','view.Add','view.Edit','view.detail.List','view.detail.Add','view.detail.Edit'
        ,'view.detail.ViewStringListField','view.detail.ViewStringFormField','view.detail.ViewIntFormField'
        ,'view.detail.ViewIntListField','view.detail.ViewFloatFormField','view.detail.ViewFloatListField'
        ,'view.detail.ViewDateFormField','view.detail.ViewDateListField','view.detail.ViewFileFormField'
        ,'view.detail.ViewFileListField','view.detail.ViewBooleanFormField','view.detail.ViewBooleanListField'
        ,'view.detail.ViewGridField','view.detail.Main','view.detail.PreviewView','view.detail.PreviewList'
        ,'view.detail.PropertyList','view.detail.PropertyAdd','view.detail.PropertyEdit','view.detail.ViewList','view.detail.ViewEmptyField'
        ,'view.detail.ViewHiddenField','view.detail.ViewDetailDataDictItem','dict.item.List','dict.item.Add','dict.item.Edit'
        ,'view.detail.ViewSplitterField','view.detail.ViewDetailCondList','view.detail.ViewDetailCondAdd','view.detail.SplitterAdd'
    ] ,
    stores:['ViewStore','ViewDetailStore','ViewDetailExtendStore'],
    models:['ViewModel','ViewDetailModel','ViewDetailExtendModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'viewList',
            selector : 'viewMain viewList'
        },
        {
            ref : 'moduleList',
            selector : 'viewMain moduleList'
        },
        {
            ref : 'viewDetailList',
            selector : 'viewDetailList grid'
        },
        {
            ref:'viewDetailEditForm',
            selector:'viewDetailEdit form'
        },
        {
            ref:'viewDetailEditWin',
            selector:'viewDetailEdit'
        },
        {
            ref: 'viewDetailDeleteBtn',
            selector: 'viewDetailList button#deleteButton'
        }
    ],
    init:function(){
        this.application.getController("ModuleController");
        this.control({
            'viewMain moduleList ':{
                select:function(list, record, index, eOpts){
                    var viewList = eOpts.up('viewMain').down('viewList');
                    var store = viewList.getStore();
                    Ext.apply(store.proxy.extraParams, {module:record.get('id')});
                    store.load();

                    var detailBtn = viewList.down('button#detail');
                    if(record.get('leaf') == false){ // 非叶子节点
                        detailBtn.setDisabled(true);
                        return;
                    }else{
                        detailBtn.setDisabled(false);
                    }
                }
            },
            'viewDetailViewList grid':{
                select:function(list, record, index, eOpts){
                    // 展开第一个
                    var viewDetailMain = eOpts.up('viewDetailMain');
                    var relatedOperatePanel = viewDetailMain.down('panel#relatedOperatePanel');
                    relatedOperatePanel.down('viewOperationList').expand();

                    this.loadGridStore(record,eOpts);
                    this.setEmptyFieldShow(eOpts);
                }
            },
            'viewDetailViewList combo':{
                change:function(o, newValue, oldValue, eOpts){
                    if(oldValue){ // 上次为空值不用执行
                        this.loadGridStore(null,o);
                        this.setEmptyFieldShow(o);
                    }

                    var viewList = o.up('viewDetailViewList');
                    var grid = viewList.down('grid');
                    Ext.apply(grid.getStore().proxy.extraParams, {module:viewList.paramsObj.moduleId,user:newValue});
                    grid.getStore().load(function(records, operation, success){
                        if(!oldValue && success == true && viewList.paramsObj.view){
                            var rec = grid.getStore().query("id",viewList.paramsObj.view);
                            grid.getSelectionModel().select(rec.get(0));
                        }
                    });
                }
            },
            'viewMain viewList button#add':{
                click:function(btn){
                    var record = this.getModuleList().getSelectionModel().getSelection()[0];
                    if(record){
                        var view = Ext.widget('viewAdd');
                        view.down('form hiddenfield[name=module.id]').setValue(record.get("id"));
                        view.down('form hiddenfield[name=model.id]').setValue(record.get("model.id"));
                        view.down('form textfield[name=viewId]').setValue(record.get("model.modelName"));
                        var user = btn.up('baseList').down('field[name=user]').getValue();
                        if(user){
                            view.down('form field[name=user.id]').store.load();
                            view.down('form field[name=user.id]').setValue(user);
                        }
                        view.show();
                    }else{
                        Ext.Msg.alert('提示', '请先选择模块！');
                    }
                }
            },
            'viewMain viewList button#detail':{
                click:function(btn){
                    var viewMain = btn.up('viewMain');
                    var moduleId="",userId="",view="",viewId="";
                    var viewList = viewMain.down('viewList');
                    var userCombo = viewList.down('combo[name=user]');
                    var records = viewList.getSelectionModel().getSelection();
                    if(records.length > 0){
                        var record = records[0];
                        view = record.get('id');
                        viewId = record.get('viewId');
                        userId = record.get('user.id');
                        moduleId = record.get('module.id');
                    }else if(userCombo.getValue()){
                        userId = userCombo.getValue();
                        var viewModule = viewMain.down('moduleList');
                        var moduleRecord = viewModule.getSelectionModel().getSelection()[0];
                        moduleId = moduleRecord.get('id');
                    }else{
                        Ext.Msg.alert("提示","请选择记录或者用户！");
                        return;
                    }
                    var paramsObj = {view:view,viewId:viewId,userId:userId,moduleId:moduleId};
                    var win = Ext.widget('viewDetailMain',{
                        paramsObj:paramsObj
                    });
                    win.show();
                }
            },
            'viewMain viewList combo[name=user]':{
                change:function(combo, newValue, oldValue, eOpts ){
                    var o = combo.up('viewList');
                    var store = o.getStore();
                    Ext.apply(store.proxy.extraParams, {user:newValue});
                    store.load();
                }
            },
            // 自定义明细列表
            'viewDetailList grid':{
                select:function(selectModel, record, index, eOpts){
                    if(index==0){
                        eOpts.up('viewDetailList').down("button#up").setDisabled(true);
                    }
                    if(eOpts.up('grid').getStore().getCount()-1==index){
                        eOpts.up('viewDetailList').down("button#down").setDisabled(true);
                    }

                    // 选择明细，动态改变右边内容
                    var viewDetailMain = eOpts.up('viewDetailMain');
                    var viewDetailViewList = viewDetailMain.down('viewDetailViewList');
                    var moduleRecord = viewDetailViewList.down('grid').getSelectionModel().getSelection()[0];
                    var relatedOperatePanel = viewDetailMain.down('panel#relatedOperatePanel');
                    var propertySet = relatedOperatePanel.down('component[name=propertySet]');
                    // 展开第二个
                    propertySet.expand();
                    var panel = Ext.widget(record.get('win'));
                    propertySet.removeAll();
                    propertySet.add(panel.items.items);
                    if(record.get('view.editable') && moduleRecord.get('viewType') == 'list'){
                        var editableHid = propertySet.down('form').down('hiddenfield#viewEditable');
                        if(editableHid) editableHid.setValue(record.get('view.editable'));
                    }
                    // 加载数据字典数据
                    var panelForm = propertySet.down('form');
                    var fieldName = record.get('userField.fieldName');
                    if(fieldName && fieldName.indexOf(".") > -1){
                        var isSubmitValue = panelForm.down('checkboxfield[name=isSubmitValue]');
                        if(isSubmitValue) isSubmitValue.setVisible(true);
                    }
                    var user = record.get('user.id');
                    var dictCombo = panelForm.down('combo#dict');
                    if(dictCombo){
                        dictCombo.getStore().load({params:{user:user,isPaging:false}});
                    }
                    panelForm.loadRecord(record);
                    var userFieldMax = propertySet.down('numberfield[name=userField.max]');
                    if(userFieldMax){
                        userFieldMax.setMaxValue(record.get('userField.maxSize'));
                        if(userFieldMax.getValue() == 65535){
                            panelForm.down('combo[name=pageType]').getStore().removeAt(4);
                        }
                    }
                    var propertyGrid = propertySet.down("baseList");
                    var store = propertyGrid.getStore();
                    Ext.apply(store.proxy.extraParams,{viewDetail: record.get("id")});
                    store.load();
                },
                deselect:function(o, record, index, eOpts){
                    this.setEmptyFieldShow(eOpts);
                }
            },
            // 附加条件按钮
            'viewEmptyField form button[name=configExtraCondition]':{
                click:function(o){
                    var form = o.up('form');
                    var paramStoreCombo = form.down('combo[name=paramStore]');
                    var paramStoreComboVal = paramStoreCombo.getValue();
                    if(paramStoreComboVal){
                        var extraConditionTextarea = form.down("textarea[name=extraCondition]");
                        var paramStoreComboStore = paramStoreCombo.getStore();
                        var record = paramStoreComboStore.findRecord("store",paramStoreComboVal);
                        var moduleId = record.get('moduleId');
                        if(moduleId){
                            var viewDetailViewList = o.up('viewDetailMain').down('viewDetailViewList');
                            var userCombo = viewDetailViewList.down('combo[name=user]');
                            var conditionList = Ext.widget('viewDetailCondList',{
                                user : userCombo.getValue(),
                                moduleId:moduleId,
                                extraConditionTextarea:extraConditionTextarea
                            });
                            conditionList.show();
                        }
                    }else{
                        Ext.Msg.alert("提示","请先选择Store和ViewId");
                    }
                }
            },
            'viewDetailCondList grid button#addButton':{
                click:function(o){
                    var list = o.up('viewDetailCondList');
                    Ext.widget('viewDetailCondAdd',{
                        user:list.user,
                        moduleId:list.moduleId,
                        listDom:list.down('gridpanel')
                    }).show()
                }
            },
            'viewDetailCondList grid button#updateButton':{
                click:function(o){
                    var list = o.up('viewDetailCondList');
                    var records = list.down('baseList').getSelectionModel().getSelection();
                    if(!records || records.length==0){
                        Ext.Msg.alert("提示","请先选择记录！");
                        return;
                    }
                    var add = Ext.widget('viewDetailCondAdd',{
                        user:list.user,
                        moduleId:list.moduleId,
                        record:records[0],
                        listDom:list.down('gridpanel')
                    });
                    add.show();
                }
            },
            'viewDetailCondList grid button#deleteButton':{
                click:function(btn){
                    var list = btn.up('viewDetailCondList');
                    var records = list.down('baseList').getSelectionModel().getSelection();
                    if(records && records.length > 0){
                        Ext.MessageBox.confirm('提示','确定删除所选记录吗？', function(button) {
                            if (button == 'yes') {
                                var gridStore = list.down('grid').getStore();
                                Ext.Array.each(records,function(record){
                                    gridStore.remove(record);
                                })
                                Ext.example.msg('提示', "添加成功！");
                            }
                        })
                    }
                }
            },
            'viewDetailCondAdd combo[name=fieldName]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.searchFieldCombo(o,newValue);
                }
            },
            'viewDetailCondAdd combo[name=searchFieldComboValue]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.searchFieldValueCombo(o,newValue);
                }
            },
            'viewDetailCondAdd form button#viewDetailCondSave':{
                click:function(btn){
                    var listDom = btn.up('window').listDom;
                    var store = listDom.getStore();
                    var form = btn.up('form');
                    if (!form.getForm().isValid()) return;
                    var originFieldNameVal = form.down('hiddenfield[name=originFieldName]').getValue();
                    if(originFieldNameVal){
                        var record = store.findRecord("fieldName",originFieldNameVal);
                        store.remove(record);
                    }
                    // 得到新对象
                    var addObj = this.getAddObj(btn);
                    // 添加新对象
                    store.add(addObj);

                    Ext.example.msg('提示', "添加成功！");
                    form.getForm().reset();
                }
            },
            // 条件列表->保存，生成json字符串到属性字段里面
            'viewDetailCondList button#generateJSONCondition':{
                click:function(btn){
                    var viewDetailCondList = btn.up('viewDetailCondList');
                    var gridStore = viewDetailCondList.down('grid').getStore();
                    var objArr = [];
                    gridStore.each(function(record){
                        var obj = {fieldName:record.get('fieldName'),fieldNameText:record.get('fieldNameText'),
                            operator:record.get('operator'),operatorText:record.get('operatorText'),
                            userFieldValue:record.get('userFieldValue'),userFieldTextValue:record.get('userFieldTextValue'),
                            dbType:record.get('dbType'),startDate:record.get('startDate'),endDate:record.get('endDate')
                        }
                        objArr.push(Ext.JSON.encode(obj));
                    })
                    objArr.join(",");
                    if(objArr && objArr.length > 0){
                        viewDetailCondList.extraConditionTextarea.setValue("["+objArr+"]");
                    }else{
                        viewDetailCondList.extraConditionTextarea.setValue(null);
                    }
                    viewDetailCondList.close();
                }
            },
            // 属性form上面的默认值combo后面的按钮
            'viewEmptyField form button[name=editUserFieldDict]':{
                click:function(o){
                    var form = o.up('form');
                    var dict = form.down('combo#dict');
                    var dictValue = dict.getValue();
                    if(dictValue){
                        var dictItemView = Ext.widget('viewDetailDataDictItem',{
                            sourceForm:form,
                            dict:dictValue,
                            user:form.down('hiddenfield[name=user.id]').getValue(),
                            viewDetailMain: o.up('viewDetailMain')
                        });
                        var dictItemViewStore = dictItemView.down('dictItemList').getStore();
                        Ext.apply(dictItemViewStore.proxy.extraParams,{dict:dictValue});
                        dictItemViewStore.load();
                        dictItemView.show()
                    }
                }
            },
            'dictItemList button#addButton':{
                click:function(btn){
                    var viewDetailDataDictItem = btn.up('viewDetailDataDictItem');
                    var view = Ext.widget("dictItemAdd");
                    view.down('hiddenfield[name=user]').setValue(viewDetailDataDictItem.user);
                    view.down('hiddenfield[name=dict]').setValue(viewDetailDataDictItem.dict);
                    view.show();
                }
            },
            // 当新增、修改、删除时，动态改变form上面的默认值combo
            'viewDetailDataDictItem dictItemList':{
                afterrender: function (grid, eOpts) {
                    var me = this;
                    var viewDetailDictItem = grid.up('viewDetailDataDictItem');
                    grid.mon(grid.store, 'datachanged', function (store, record, operation, eOpts) {
                        var sourceForm = viewDetailDictItem.sourceForm;
                        var defValueCombo = sourceForm.down('combo[name=defValue]');
                        me.defValueComboStoreLoad(defValueCombo,viewDetailDictItem.dict,viewDetailDictItem.user,viewDetailDictItem.viewDetailMain)
                    });
                }
            },
            // 是否超链接
            'viewEmptyField form checkboxfield#isHyperLink':{
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    var isHyperLinkFS = form.down('fieldset#isHyperLinkFS');
                    var paramStore = form.down('combo[name=paramStore]');
                    var paramViewId = form.down('combo[name=paramViewId]');
                    var targetIdName = form.down('textfield[name=targetIdName]');
                    if(newValue == true){
                        isHyperLinkFS.setVisible(true);
                        paramStore.allowBlank = false;
                        //paramViewId.allowBlank = false;
                        targetIdName.allowBlank = false;
                    }else{
                        isHyperLinkFS.setVisible(false);
                        paramStore.allowBlank = true;
                        //paramViewId.allowBlank = true;
                        targetIdName.allowBlank = true;
                        paramStore.setValue(null);
                        paramViewId.setValue(null);
                        targetIdName.setValue(null);
                    }
                }
            },
            // 列表是否可编辑
            'viewEmptyField form hiddenfield#viewEditable':{
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    var pageType = form.down('combo[name=pageType]');
                    var editableFS = form.down('fieldset#editableFS');
                    var isHyperLink = form.down('checkboxfield#isHyperLink');
                    var isHyperLinkFS = form.down('fieldset#isHyperLinkFS');
                    if(newValue == 'true'){
                        pageType.setVisible(true);
                        editableFS.setVisible(true);
                        form.remove(isHyperLink);
                        form.remove(isHyperLinkFS);
                    }else{
                        if(pageType.fieldType != 'Date'){
                            pageType.setVisible(false);
                        }
                        editableFS.setVisible(false);
                        form.remove(editableFS);
                    }
                }
            },
            'viewEmptyField form combo[name=userField.dict]':{ // 数据字典改变时，动态加载子项
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    var record = o.getStore().findRecord("id", newValue);
                    var issys = false;
                    if(record){
                        issys = record.get('issys');
                    }
                    var btnEditUserFieldDict = form.down('button[name=editUserFieldDict]');
                    if(btnEditUserFieldDict){
                        if(issys == true){
                            btnEditUserFieldDict.setDisabled(true);
                        }else{
                            btnEditUserFieldDict.setDisabled(false);
                        }
                    }
                    var user = form.down('hiddenfield[name=user.id]').getValue();
                    var defValueCombo = form.down('combo[name=defValue]');
                    if(defValueCombo){
                        this.defValueComboStoreLoad(defValueCombo,newValue,user,o.up('viewDetailMain'));
                    }
                }
            },
            'viewEmptyField form combo[name=paramStore]':{ // paramStore改变时，动态加载子项
                change:function(o, newValue, oldValue, eOpts){
                    var form = o.up('form');
                    if(newValue == null){
                        form.down('combo[name=paramViewId]').getStore().removeAll();
                    }else{
                        var record = o.getStore().findRecord("store", newValue);
                        if(record){
                            var viewDetailMain = o.up('viewDetailMain');
                            var viewDetailViewList = viewDetailMain.down('viewDetailViewList');
                            var userCombo = viewDetailViewList.down('combo[name=user]');
                            if(userCombo){
                                var viewDetailList = viewDetailMain.down('viewDetailList').down('baseList');
                                var viewDetailRecs = viewDetailList.getSelectionModel().getSelection();
                                var isHyperLink = form.down('checkboxfield[name=isHyperLink]');
                                var user = userCombo.getValue();
                                var moduleId = record.get('moduleId');
                                var paramViewStore = form.down('combo[name=paramViewId]');
                                var viewStore = paramViewStore.getStore();
                                if(isHyperLink){
                                    viewStore.load({
                                        params:{module:moduleId,clientType:'pc',viewType:'form',specialParam:user,viewPage:true},
                                        callback: function(records, operation, success) {
                                            if(success == true && viewDetailRecs && viewDetailRecs.length > 0){
                                                paramViewStore.setValue(viewDetailRecs[0].get('paramViewId'));
                                            }
                                        }
                                    });
                                }else{
                                    viewStore.load({
                                        params:{module:moduleId,clientType:'pc',viewType:'list',specialParam:user},
                                        callback: function(records, operation, success) {
                                            if(success == true && viewDetailRecs && viewDetailRecs.length > 0){
                                                paramViewStore.setValue(viewDetailRecs[0].get('paramViewId'));
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            },
            'viewDetailList button#up':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index - 1, rec);
                    store.each(function(record,index){
                        record.set('orderIndex',index+1);
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
            'viewDetailList button#down':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index + 1, rec);
                    store.each(function(record,index){
                        record.set('orderIndex',index+1);
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
            'viewDetailList button#save':{
                click:function(btn){
                    this.GridDoActionUtil.doSave(btn.up('grid'),btn);
                }
            },
            'viewDetailList button#addButton':{
                click:function(btn){
                    var parentWin = btn.up('viewDetailList');
                    var win = Ext.create('admin.view.view.detail.Add',{
                        parentWin:parentWin
                    });
                    win.show();
                }
            },
            'viewDetailList button#previewButton':{
                click:function(btn){
                    var gridMain = btn.up('viewDetailMain');
                    var viewList = gridMain.down('viewDetailViewList').down('grid');
                    var viewRecords = viewList.getSelectionModel().getSelection();
                    // 预览form时显示多少列
                    var columns = 2;
                    if(viewRecords){
                        var clientType = viewRecords[0].get('clientType');
                        if(clientType == 'mobile'){
                            columns = 1;
                        }else{
                            columns = viewRecords[0].get('columns');
                        }
                    }
                    var grid = btn.up('viewDetailList');
                    var xtype = 'previewView';
                    if(grid.paramsObj.viewId){
                        if(grid.paramsObj.viewId.substring(grid.paramsObj.viewId.length-4) == 'List'){
                            xtype = 'previewList';
                        }
                    }else{
                        Ext.Msg.alert('提示', '出错了！');
                        return;
                    }
                    var storeName = grid.paramsObj.viewId.substring(0,grid.paramsObj.viewId.length-4)+'Store';
                    var win = Ext.widget(xtype,{
                        storeName:storeName,
                        title:gridMain.title,
                        viewId:grid.paramsObj.viewId,
                        userId:grid.paramsObj.userId,
                        paramColumns:columns
                    });
                    win.show();
                }
            },
            'viewDetailList button#addSplitter':{
                click:function(btn){
                    var viewDetailMain = btn.up('viewDetailMain');
                    var viewList = viewDetailMain.down('viewDetailViewList').down('grid');
                    var records = viewList.getSelectionModel().getSelection();
                    if(records && records.length > 0){
                        var win = Ext.widget('viewDetailSplitterAdd',{
                            parentGrid : btn.up('viewDetailList').down('baseList')
                        });
                        win.down('hiddenfield[name=view.id]').setValue(records[0].get('id'));
                        win.down('hiddenfield[name=user.id]').setValue(records[0].get('user.id'));
                        win.down('hiddenfield[name=module.id]').setValue(records[0].get('module.id'));
                        win.show();
                    }else{
                        Ext.Msg.alert('提示','请先选择视图！');
                    }
                }
            },
            'viewDetailSplitterAdd button#save':{
                click:function(btn){
                    var win = btn.up('window');
                    var form = win.down('form');
                    this.GridDoActionUtil.doInsert(win.parentGrid,form,win);
                }
            },
            'combo[name=pageType]':{
                change:function(field, newValue, oldValue, eOpts ){
                    if(field.fieldType == 'String') {
                        this.dealStringField(field, newValue, oldValue);
                    }else if(field.fieldType == 'Integer') {
                        this.dealIntegerField(field, newValue, oldValue);
                    }else if(field.fieldType == 'Float'){
                        this.dealFloatField(field, newValue, oldValue);
                    }else if(field.fieldType == 'Date'){
                        this.dealDateField(field, newValue, oldValue);
                    }else if(field.fieldType == 'Boolean'){
                        this.dealBooleanField(field, newValue, oldValue);
                    }else if(field.fieldType == 'File'){
                        this.dealFileField(field, newValue, oldValue);
                    }
                }
            },
            'combo[name=inputFormat][belongToName=ViewStringField]':{
                change:function(field, newValue, oldValue, eOpts ){
                    var form = field.up('form');
                    var inputFormatParamFS = form.down('fieldset#inputFormatParamFS');
                    if(oldValue){
                        inputFormatParamFS.down('combo[name=paramStore]').setValue(null);
                        inputFormatParamFS.down('combo[name=paramViewId]').setValue(null);
                    }
                    if(!newValue || newValue == 'email' || newValue == 'url' || newValue == 'phone'){
                        inputFormatParamFS.down('combo[name=paramStore]').allowBlank=true;
                        inputFormatParamFS.down('combo[name=paramViewId]').allowBlank=true;
                        inputFormatParamFS.setVisible(false);
                    }else{
                        inputFormatParamFS.down('combo[name=paramStore]').allowBlank=false;
                        //inputFormatParamFS.down('combo[name=paramViewId]').allowBlank=false;
                        inputFormatParamFS.setVisible(true);
                    }
                }
            },
            'viewDetailEdit button#save': {
                click: function (btn) {
                    this.GridDoActionUtil.doUpdate(this.getViewDetailList(), this.getViewDetailEditForm(), this.getViewDetailEditWin());
                }
            },
            'viewDetailList button#deleteButton':{
                click:function(btn){
                    var win = btn.up("viewDetailList");
                    var grid = win.down('grid');
                    this.GridDoActionUtil.doDelete(grid, 'label',this.getViewDetailDeleteBtn());
                }
            },
            'viewDetailPropertyList[name=propertyGridView] button#addLine':{
                click:function(btn){
                    var win = btn.up("panel[name=propertySet]");
                    var viewDetail = win.down("hiddenfield[name=id]").getValue();
                    var add = Ext.widget('viewDetailPropertyAdd');
                    add.down('hiddenfield[name=viewDetail]').setValue(viewDetail);
                    add.show();
                }
            },
            'viewDetailPropertyList[name=propertyGridView] button#editLine':{
                click:function(btn){
                    var grid = btn.up("baseList");
                    var record = grid.getSelectionModel().getSelection()[0];
                    var edit = Ext.widget('viewDetailPropertyEdit');
                    var form = edit.down('form');
                    if(!record.get('paramType')){
                        record.set('paramType','String')
                    }
                    form.loadRecord(record);
                    form.down('[name=paramValue]').setValue(record.get('paramValue'));
                    edit.show();
                }
            },
            'viewDetailPropertyAdd combo[name=paramType]':{
                change:function(o, newValue, oldValue, eOpts ){
                    this.changeComponent(o,newValue);
                }
            },
            'viewDetailPropertyEdit combo[name=paramType]':{
                change:function(o, newValue, oldValue, eOpts ){
                    this.changeComponent(o,newValue);
                }
            }
        });
    },
    setEmptyFieldShow:function(eOpts){
        var viewDetailMain = eOpts.up('viewDetailMain');
        var relatedOperatePanel = viewDetailMain.down('panel#relatedOperatePanel');
        var propertySet = relatedOperatePanel.down('component[name=propertySet]');
        var panel = Ext.widget('viewEmptyField');
        propertySet.removeAll();
        propertySet.add(panel.items.items);
    },
    loadGridStore:function(record,o){
        var viewDetailMain = o.up('viewDetailMain');
        // 视图明细
        var viewDetailList = viewDetailMain.down('viewDetailList');
        viewDetailList.paramsObj.view = record?record.get('id'):"0";
        viewDetailList.paramsObj.viewId = record?record.get('viewId'):"";
        viewDetailList.paramsObj.userId = record?record.get('user.id'):"";
        var viewListStore = viewDetailList.down('grid').getStore();
        Ext.apply(viewListStore.proxy.extraParams, {view:viewDetailList.paramsObj.view});
        viewListStore.load();
        // 操作
        var viewOptList = viewDetailMain.down('viewOperationList');
        viewDetailList.paramsObj.view = record?record.get('id'):"0";
        viewDetailList.paramsObj.viewId = record?record.get('viewId'):"";
        viewDetailList.paramsObj.userId = record?record.get('user.id'):"";
        var viewOptStore = viewOptList.down('grid').getStore();
        Ext.apply(viewOptStore.proxy.extraParams, {view:viewDetailList.paramsObj.view});
        viewOptStore.load();
        if(record && record.get('viewType') == 'list'){
            viewOptList.down('button#addButton').setDisabled(false);
        }else{
            viewOptList.down('button#addButton').setDisabled(true);
        }
    },
    changeComponent : function(o,value){
        var form = o.up('form');
        form.remove(form.down('[name=paramValue]'));
        var valueCom
        if(value == 'String'){
            valueCom = Ext.widget('textfield',{
                name: "paramValue",
                fieldLabel: "值",
                padding: '3 5 3 5',
                labelWidth : 120,
                allowBlank : false
            });
        }else if(value == 'Integer'){
            valueCom = Ext.widget('numberfield', {
                name: "paramValue",
                fieldLabel: "值",
                labelWidth : 120,
                padding: '3 5 3 5',
                allowBlank: false
            });
        }else if(value == 'Date'){
            valueCom = Ext.widget('datetimefield',{
                name: "paramValue",
                fieldLabel: "值",
                labelWidth : 120,
                padding: '3 5 3 5',
                format: 'Y-m-d H:m:s',
                allowBlank : false,
                value:new Date()
            });
        }else if(value == 'Boolean'){
            valueCom = Ext.widget('checkbox',{
                fieldLabel : '值',
                labelWidth : 120,
                padding: '3 5 3 5',
                name : 'paramValue',
                allowBlank: false,
                boxLabelAlign: 'before',
                inputValue: 'true',
                uncheckedValue:'false'
            });
        }
        form.insert(2,valueCom);
    },
    dealStringField:function(field,newValue,oldValue){
        var form = field.up('form');
        var mostFieldFS = form.down('fieldset#mostFieldFS');
        var colsAndRowsFS = form.down('fieldset#colsAndRowsFS');
        var minAndMaxFS = form.down('fieldset#minAndMaxFS');
        var inputFormatCombo = form.down('combo[name=inputFormat]');
        var isHyperLink = form.down('checkboxfield#isHyperLink');
        var dict = form.down('combo#dict');
        if(oldValue){
            form.down('numberfield[name=cols]').setValue(null);
            form.down('numberfield[name=rows]').setValue(null);
            if(form.down('textfield[name=defValue]'))form.down('textfield[name=defValue]').setValue(null);
            form.down('textfield[name=initName]').setValue(null);
            inputFormatCombo.setValue(null);
            form.down('combo[name=paramStore]').setValue(null);
            form.down('combo[name=paramViewId]').setValue(null);
        }
        if(newValue == 'hidden'){
            mostFieldFS.setVisible(false);
            if(isHyperLink) isHyperLink.setVisible(false);
            if(dict) dict.setVisible(false);
            this.setDefValueComponentTF(form,true);
        }else{
            mostFieldFS.setVisible(true);
            if(isHyperLink) isHyperLink.setVisible(true);
            if(newValue == 'textfield'){
                colsAndRowsFS.setVisible(false);
                minAndMaxFS.setVisible(true);
                inputFormatCombo.setVisible(true);
            }else if(newValue == 'textfield2'){
                colsAndRowsFS.down('numberfield[name=rows]').setVisible(false);
                colsAndRowsFS.setVisible(true);
                minAndMaxFS.setVisible(true);
                inputFormatCombo.setVisible(true);
            }else if(newValue == 'textarea'){
                colsAndRowsFS.down('numberfield[name=rows]').setVisible(true);
                colsAndRowsFS.setVisible(true);
                minAndMaxFS.setVisible(true);
                inputFormatCombo.setVisible(true);
            }else if(newValue == 'htmleditor'){
                colsAndRowsFS.down('numberfield[name=rows]').setVisible(true);
                colsAndRowsFS.setVisible(true);
                minAndMaxFS.setVisible(false);
                inputFormatCombo.setVisible(false);
                dict.setVisible(false);
            } else if(newValue == 'multichoice') {
                colsAndRowsFS.setVisible(false);
                minAndMaxFS.setVisible(false);
                inputFormatCombo.setVisible(false);
            }
            if(newValue == 'multichoice'){
                if(dict) dict.setVisible(true);
                this.setDefValueComponentTF(form,false);
            }else{
                if(dict) dict.setVisible(false);
                this.setDefValueComponentTF(form,true);
            }
        }
    },
    dealIntegerField:function(field,newValue,oldValue){
        var form = field.up('form');
        var mostFieldFS = form.down('fieldset#mostFieldFS');
        var minAndMaxFS = form.down('fieldset#minAndMaxFS');
        var isMoneyFS = form.down('fieldset#isMoneyFS');
        var dict = form.down('combo#dict');
        if(oldValue){
            form.down('checkboxfield[name=userField.isMoney]').setValue(null);
            if(form.down('textfield[name=defValue]'))form.down('textfield[name=defValue]').setValue(null);
            form.down('textfield[name=initName]').setValue(null);
        }
        if(newValue == 'hidden'){
            mostFieldFS.setVisible(false);
            isMoneyFS.setVisible(false);
            this.setDefValueComponentTF(form,true);
        }else{
            mostFieldFS.setVisible(true);
            if(newValue == 'numberfield') {
                if (dict) dict.setVisible(false);
                minAndMaxFS.setVisible(true);
                isMoneyFS.setVisible(true);
                this.setDefValueComponentTF(form, true);
            }else{
                if(dict) dict.setVisible(true);
                minAndMaxFS.setVisible(false);
                if(field.viewType == 'List'){
                    isMoneyFS.setVisible(true);
                }else{
                    isMoneyFS.setVisible(false);
                }
                this.setDefValueComponentTF(form,false);
            }
        }
    },
    dealFloatField:function(field,newValue,oldValue){
        var form = field.up('form');
        var mostFieldFS = form.down('fieldset#mostFieldFS');
        var isMoneyFS = form.down('fieldset#isMoneyFS');
        if(oldValue){
            if(form.down('textfield[name=defValue]'))form.down('textfield[name=defValue]').setValue(null);
            form.down('textfield[name=initName]').setValue(null);
        }
        if(newValue == 'hidden'){
            mostFieldFS.setVisible(false);
            isMoneyFS.setVisible(false);
        }else{
            mostFieldFS.setVisible(true);
            isMoneyFS.setVisible(true);
        }
    },
    dealDateField:function(field,newValue,oldValue){
        var form = field.up('form');
        if(oldValue){
        }
        var dateStore = [
            ['', '']
        ]
        if (newValue == 'datefield') {
            dateStore = [
                ['Y-m-d', '年-月-日'],
                ['m,d,Y', '月,日,年'],
                ['Y-m', '年-月'],
                ['m,Y', '月,年']
            ];
        } else if (newValue == 'datetimefield') {
            dateStore = [
                ['Y-m-d H:i:s', '年-月-日 时:分:秒'],
                ['Y-m-d H:i', '年-月-日 时:分'],
                ['m,d,Y H:i:s', '月,日,年 时:分:秒'],
                ['m,d,Y H:i', '月,日,年 时:分']
            ];
        } else if (newValue == 'timefield') {
            dateStore = [
                ['H:i:s', '时:分:秒']
            ];
        }
        var inputTypeField = form.down('combo[name=userField.dateFormat]');
        inputTypeField.setValue(null);
        inputTypeField.getStore().removeAll();
        inputTypeField.getStore().add(dateStore);
        var viewDetailMain = field.up('viewDetailMain');
        var viewDetailList = viewDetailMain.down('viewDetailList').down('baseList');
        var record = viewDetailList.getSelectionModel().getSelection()[0];
        inputTypeField.setValue(record.get('userField.dateFormat'));
    },
    dealBooleanField:function(field,newValue,oldValue){
        var form = field.up('form');
        var mostFieldFS = form.down('fieldset#mostFieldFS');
        if(oldValue){
            if(form.down('textfield[name=defValue]')) form.down('textfield[name=defValue]').setValue(null);
            form.down('textfield[name=initName]').setValue(null);
        }
        if(newValue == 'hidden'){
            mostFieldFS.setVisible(false);
        }else{
            mostFieldFS.setVisible(true);
        }
    },
    dealFileField:function(field,newValue,oldValue){
        var form = field.up('form');
        var dateFormat = form.down('textfield[name=userField.dateFormat]');
        if(newValue == 'baseUploadField'){
            dateFormat.setVisible(true);
        }else{
            dateFormat.setVisible(false);
        }
    },
    /**
     * 设置form表单上面defValue的textfield和combo哪个显示
     * @param form
     * @param textfieldTOF textfield显示或者不显示，true：显示：false：不显示
     */
    setDefValueComponentTF:function(form,textfieldTOF){
        var defValueComboFS = form.down('fieldset#defValueComboFS');
        var defValueText = form.down('textfield[name=defValue]');
        var defValueCombo = form.down('combo[name=defValue]');
        if(defValueText && defValueComboFS){
            defValueText.setVisible(textfieldTOF);
            defValueText.submitValue = textfieldTOF;
            defValueComboFS.setVisible(!textfieldTOF);
            defValueCombo.submitValue = !textfieldTOF;
        }
    },
    defValueComboStoreLoad:function(defValueCombo,dict,user,viewDetailMain){
        var defValueComboStore = defValueCombo.getStore();
        defValueComboStore.load({
            params:{dict:dict,user:user},
            callback:function(records, operation, success){
                if(success == true){ // 加载成功，默认值有值，则默认赋上
                    var viewDetailList = viewDetailMain.down('viewDetailList').down('baseList');
                    var record = viewDetailList.getSelectionModel().getSelection()[0];
                    var defValue = record.get('defValue');
                    if(defValue){
                        var tempValue = defValue.split(",");
                        var valueArr = [];
                        for(var i=0;i<tempValue.length;i++){
                            valueArr.push(parseInt(tempValue[i],10));
                        }
                        defValueCombo.setValue(valueArr);
                    }
                }
            }
        });
    },
    searchFieldCombo : function(o, newValue){
        var me = o.up('form');
        if(o.valueModels[0] == null || o.valueModels[0] == 'undefined') return;
        var pageType = o.valueModels[0].data.pageType;
        var textField = me.down("textfield[name=searchFieldTextValue]");
        var numberField = me.down('numberfield[name=searchFieldNumberValue]');
        var comboField = me.down("combo[name=searchFieldComboValue]");
        var startDateField = me.down("datefield[name=searchFieldStartDateValue]");
        var endDateField = me.down("datefield[name=searchFieldEndDateValue]");
        if(pageType == "combo" || pageType == "checkbox"){
            textField.hide();
            if(numberField) numberField.hide();
            if(startDateField) startDateField.hide();
            if(endDateField) endDateField.hide();
            if(comboField) {
                comboField.reset();
                var storeArr = o.valueModels[0].data.store;
                if(pageType == "checkbox" && (storeArr == '' || storeArr == null)){
                    storeArr = [["false","否"],["true","是"]];
                }
                comboField.getStore().removeAll();
                comboField.getStore().add(storeArr);
                comboField.show();
            }

        }else if(pageType == "textfield"){
            if(numberField) numberField.hide();
            if(comboField)  comboField.hide();
            if(startDateField) startDateField.hide();
            if(endDateField) endDateField.hide();
            if(textField) {
                textField.reset();
                textField.show();
            }
        }else if(pageType == 'datetimefield'){
            var dateStore = [['TODAY','今天'],['THIS_WEEK','本周'],['THIS_MONTH','本月'],['THIS_YEAR','今年'],['TOMORROW','明天'],
                ['NEXT_WEEK','下周'],['NEXT_MONTH','下月'] ,['YESTERDAY','昨天'],['LAST_WEEK','上周'],
                ['LAST_MONTH','上月'],['LAST_YEAR','去年'],['CUSTOM','自定义时间']];
            textField.hide();
            if(startDateField) startDateField.hide();
            if(endDateField) endDateField.hide();
            if(comboField){
                comboField.reset();
                comboField.getStore().removeAll();
                comboField.getStore().add(dateStore);
                comboField.show();
            }
        }else if(pageType == "numberfield"){
            if(comboField)comboField.hide();
            if(startDateField)startDateField.hide();
            if(endDateField) endDateField.hide();
            if(textField) textField.hide();
            if(numberField){
                numberField.reset();
                numberField.show();
            }
        }
    },
    searchFieldValueCombo : function(o, newValue){
        if(!newValue) return;
        var form = o.up('form');
        var comboField = form.down("combo[name=fieldName]");
        var pageType = comboField.valueModels[0].data.pageType;
        if(pageType == 'datetimefield'){
            var startDateField = form.down("datefield[name=searchFieldStartDateValue]");
            var endDateField = form.down("datefield[name=searchFieldEndDateValue]");
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
    },
    getAddObj:function(btn){
        var form = btn.up('form');
        var fieldNameCombo = form.down("combo[name=fieldName]");
        var fieldNameValue = fieldNameCombo.getValue();
        var fieldNameTextValue = fieldNameCombo.getRawValue();
        var pageType = fieldNameCombo.valueModels[0].data.pageType;
        var dbType = fieldNameCombo.valueModels[0].data.dbType;
        var operator = form.down("combo[name=operator]").getValue();
        var operatorText = form.down("combo[name=operator]").getRawValue();
        var searchFieldValue = "";
        var searchFieldTextValue = "";
        var startDateFieldValue = "";
        var endDateFieldValue = "";
        if(pageType == 'textfield') {
            searchFieldValue = form.down("textfield[name=searchFieldTextValue]").getValue();
            searchFieldTextValue = searchFieldValue;
        }else if(pageType == 'numberfield'){
            searchFieldValue = form.down("numberfield[name=searchFieldNumberValue]").getValue();
            searchFieldTextValue = searchFieldValue;
        }else if(pageType == 'combo' || pageType == 'checkbox' || pageType == 'datetimefield'){
            searchFieldValue = form.down("combo[name=searchFieldComboValue]").getValue();
            searchFieldTextValue = form.down("combo[name=searchFieldComboValue]").getRawValue();
            if(searchFieldValue == 'CUSTOM'){
                startDateFieldValue = form.down("datefield[name=searchFieldStartDateValue]").getValue();
                endDateFieldValue = form.down("datefield[name=searchFieldEndDateValue]").getValue();
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
        var obj = {fieldName:fieldNameValue,fieldNameText:fieldNameTextValue,operator:operator,operatorText:operatorText
            ,userFieldValue:searchFieldValue,userFieldTextValue:searchFieldTextValue,dbType:dbType
            ,startDate:startDateFieldValue,endDate:endDateFieldValue};
        return obj;
    }
});
