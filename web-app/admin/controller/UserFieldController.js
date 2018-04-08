Ext.define("admin.controller.UserFieldController",{
    extend:'Ext.app.Controller',
    views:['userField.List','userField.Main','userField.Add','userField.StringField','userField.IntField','userField.FloatField'
        ,'userField.DateField','userField.BooleanField','userField.FileField','userField.CustomCheckBoxGroupWin','userField.CustomSearchFieldWin','userField.CustomHejiFieldWin'
        ,'userField.CustomExtFieldWin','userField.ExtStringField','userField.ExtIntegerField','userField.ExtFloatField'
        ,'userField.ExtBooleanField','userField.ExtDateField','userField.associatedRequired.Win','userField.associatedRequired.Add','userField.associatedRequired.Edit'] ,
    stores:['UserFieldStore','UserModuleStore','ModuleListStore'],
    models:['UserFieldModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref:'userFieldList',
            selector:'userFieldMain userFieldList'
        },
        {
            ref:'userList',
            selector:'userFieldMain userList'
        },
        {
            ref: 'userFieldDeleteBtn',
            selector: 'userFieldMain userFieldList button[itemId=deleteButton]'
        }
    ],
    init:function(){
        this.application.getController("ModuleController");
        this.application.getController("ModelController");
        this.application.getController("FieldController");
        this.control({
            'userFieldMain userFieldList':{
                render:function(grid){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams, {dbtype:grid.dbtype});
                    store.load();
                },
                itemdblclick:function(v,record, item, index, e, eOpts ){
                    var me = this;
                    me.onShowUpdateWin(record,v);
                }
            },
            // UserField修改
            'userFieldMain userFieldList button#updateButton' : {
                click : function(btn){
                    var me = this;
                    var record = btn.up('userFieldList').getSelectionModel().getSelection();
                    me.onShowUpdateWin(record[0],btn);
                }
            },
            // 启用固定字段
            'userFieldMain userFieldList button#enableFixedField' : {
                click : function(btn){
                    var me = this;
                    var dbtype = btn.up("userFieldMain tabpanel").getActiveTab().dbtype;
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("customCheckBoxGroupWin",{
                        url : 'userField/searchEnableField',
                        title : btn.getText(),
                        dbtype : dbtype,
                        store : this.getUserFieldList().getStore(),
                        userValue : userValue,
                        moduleValue : moduleValue
                    });
                    win.show();
                }
            },
            // 启用关联字段
            'userFieldMain userFieldList button#enableRelatedField' : {
                click : function(btn){
                    var me = this;
                    var dbtype = btn.up("userFieldMain tabpanel").getActiveTab().dbtype;
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("customCheckBoxGroupWin",{
                        url : 'userField/searchRelatedObjectField',
                        title : btn.getText(),
                        dbtype : dbtype,
                        store : this.getUserFieldList().getStore(),
                        isRelatedField : true,
                        userValue : userValue,
                        moduleValue : moduleValue
                    });
                    win.show();
                }
            },
            //设置模糊查询
            'userFieldMain userFieldList button#setMohuSearchField' : {
                click : function(btn){
                    var me = this;
                    var dbtype = btn.up("userFieldMain tabpanel").getActiveTab().dbtype;
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("customSearchFieldWin",{
                        url : 'userField/searchMohuSearchField',
                        title : btn.getText(),
                        dbtype : dbtype,
                        store : this.getUserFieldList().getStore(),
                        userValue : userValue,
                        moduleValue : moduleValue
                    });
                    win.show();
                }
            },
            //设置合计字段
            'userFieldMain userFieldList button#setHejiField' : {
                click : function(btn){
                    var me = this;
                    var dbtype = btn.up("userFieldMain tabpanel").getActiveTab().dbtype;
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("customHejiFieldWin",{
                        url : 'userField/searchHejiField',
                        title : btn.getText(),
                        dbtype : dbtype,
                        store : this.getUserFieldList().getStore(),
                        userValue : userValue,
                        moduleValue : moduleValue
                    });
                    win.show();
                }
            },
            // 启用扩展字段
            'userFieldMain userFieldList button#enableExtField' : {
                click : function(btn){
                    var me = this;
                    var listDom = btn.up('userFieldMain').down('userFieldList');
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("customExtFieldWin",{
                        url : 'userField/searchExtField',
                        title : btn.getText(),
                        store : this.getUserFieldList().getStore(),
                        userValue : userValue,
                        moduleValue : moduleValue,
                        listDom:listDom
                    });
                    win.show();
                }
            },
            'button#extXXXFieldSave':{ // 启用各种扩展字段
                click:function(btn){
                    var win = btn.up('window');
                    var form = win.down('form');
                    var extBtn = win.extBtn;
                    if (!form.getForm().isValid()) return;
                    form.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        headers : {u:'admin'},
                        url:'userField/enableExtField',
                        method: 'POST',
                        submitEmptyText : false,
                        success: function(form, action) {
                            Ext.example.msg('提示', '保存成功');
                            var listDom = win.listDom;
                            listDom.getStore().load();
                            win.close();
                            var restCount = extBtn.restCount - 1; // 剩余数量
                            var dbType = extBtn.dbType;
                            extBtn.restCount = restCount;
                            if(dbType == 'java.lang.String'){
                                extBtn.setText('字符型('+restCount+')');
                            }else if(dbType == 'java.lang.Integer'){
                                extBtn.setText('整型('+restCount+')');
                            }else if(dbType == 'java.lang.Double'){
                                extBtn.setText('浮点型('+restCount+')');
                            }else if(dbType == 'java.lang.Boolean'){
                                extBtn.setText('布尔型('+restCount+')');
                            }else if(dbType == 'java.util.Date'){
                                extBtn.setText('日期型('+restCount+')');
                            }
                            if(restCount <= 0){
                                extBtn.setDisabled(true);
                            }
                        },
                        failure:function(form,action){
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            },
            // 企业字段->过滤条件【用户】
            'userFieldMain toolbar combo[name=user]':{
                change:function( combo, newValue, oldValue, eOpts ){
                    var filter = "{"+combo.name+":"+newValue+"}";
                    Ext.Array.forEach(Ext.ComponentQuery.query("userFieldMain userFieldList"),function(o,index){
                        var store = o.getStore();
                        Ext.apply(store.proxy.extraParams, Ext.JSON.decode(filter));
                        store.load();
                    });
                }
            },
            // 企业字段->过滤条件【模块】
            'userFieldMain toolbar combo[name=module]':{
                change:function( combo, newValue, oldValue, eOpts ){
                    var filter = "{"+combo.name+":"+newValue+"}";
                    Ext.Array.forEach(Ext.ComponentQuery.query("userFieldMain userFieldList"),function(o,index){
                        var store = o.getStore();
                        Ext.apply(store.proxy.extraParams, Ext.JSON.decode(filter));
                        store.load();
                    });
                }
            },            
            'userFieldMain userFieldList button#add':{
                click:function(btn){
                }
            },
            'combo[name=userFieldPageType]':{
                change:function( field, newValue, oldValue, eOpts ){
                    var form = field.up('form');
                    var dateFormat = form.down('textfield[name=dateFormat]');
                    // 特殊类型暂时不考虑默认值
                    if(newValue == 'baseSpecialTextfield'){
                        form.down('textfield[name=defValue]').setValue(null);
                        form.down('textfield[name=defValue]').hide();
                    }
                    if(newValue=='multichoice'||newValue=='radio'||newValue=='combo'){
                        form.down('combo[name=dict]').allowBlank = false;
                        form.down('combo[name=dict]').show();
                    }else{
                        if(form.down('combo[name=dict]')) {
                            form.down('combo[name=dict]').hide();
                            form.down('combo[name=dict]').allowBlank = true;
                            form.down('combo[name=dict]').setValue(null);
                        }
                    }

                    if(newValue=='numberfield'){
                        if(form.down('numberfield[name=min]')) {
                            var record = field.up('window').record;
                            form.down('numberfield[name=min]').show();
                            form.down('numberfield[name=max]').show();
                            form.down('numberfield[name=max]').setValue(record.get('max'));
                            form.down('numberfield[name=min]').setValue(record.get('min'));
                        }
                    }else{
                        if(form.down('numberfield[name=min]')) {
                            form.down('numberfield[name=min]').hide();
                            form.down('numberfield[name=max]').hide();
                            form.down('numberfield[name=max]').setValue(null);
                            form.down('numberfield[name=min]').setValue(null);
                        }
                    }

                    if(newValue == 'baseImageField' || newValue == 'baseUploadImageField'){
                        dateFormat.setVisible(false);
                        dateFormat.setValue(null);
                    }else if(newValue == 'baseUploadField') {
                        dateFormat.setVisible(true);
                    }

                    if(newValue=='datefield' || newValue=='datetime' || newValue=='timefield') {
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
                        } else if (newValue == 'datetime') {
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
                    }
                }
            },
            'propertygrid[name=propertyGrid]' : {
                beforeedit : function(e){
                    e.cancel = true;
                    return false;
                },
                itemdblclick : function(o, record, item, index, e, eOpts){
                    var me = this;
                    me.onShowInsertOrUpdWin(o,'userField/updateProperty','propertyGrid',record);
                },
                itemcontextmenu : function( o, record, item, index, e, eOpts ){
                    var controllerThis = this;
                    var objectField = o.up("window");
                    var grid = objectField.down("propertygrid[name=propertyGrid]");
                    var userFieldId = objectField.down("hiddenfield[name=id]").getValue();
                    var contextMenu = Ext.create('Ext.menu.Menu',{
                        items:[{
                            text:"删除",
                            iconCls:'table_remove',
                            handler:function(){
                                this.up("menu").destroy();
                                var paramsObj = {};
                                paramsObj.paramName = record.data["name"];
                                paramsObj.userField = userFieldId;
                                var params = {data:Ext.JSON.encode(paramsObj)}
                                var success = controllerThis.GridDoActionUtil.doAjax('userField/delProperty',params,grid.getStore());
                                if(success){
                                    grid.removeProperty(record.data["name"]);
                                }
                            }
                        }]
                    })
                    e.preventDefault();
                    contextMenu.showAt(e.getXY());
                }
            },
            'button#add_line':{
                click:function(btn){
                    var me = this;
                    me.onShowInsertOrUpdWin(btn,'userField/updateProperty','propertyGrid');
                }
            },
            //设置组合约束
            'userFieldMain userFieldList button#associatedRequiredButton' : {
                click: function (btn) {
                    var me = this;
                    var dbtype = btn.up("userFieldMain tabpanel").getActiveTab().dbtype;
                    var userValue =  btn.up("userFieldMain").down("combo[name=user]").getValue();
                    var moduleValue =  btn.up("userFieldMain").down("combo[name=module]").getValue();
                    var win = Ext.widget("associatedRequiredWin",{
                        url : 'associatedRequired/list',
                        title : btn.getText(),
                        dbtype : dbtype,
                        userValue : userValue,
                        moduleValue : moduleValue
                    });
                    win.show();
                }
            },
            'associatedRequiredWin baseList button#addButton':{
                click:function(btn){
                    var view,baseList = btn.up('baseList'),
                        win = baseList.up('associatedRequiredWin');
                    var userValue = win.down("combo[name=user]").getValue();
                    var moduleValue = win.down("combo[name=module]").getValue();
                    if((userValue == null || userValue == 'undefined')){
                        Ext.example.msg('提示', '请选择所属用户！');
                        return;
                    }
                    if((moduleValue == null || moduleValue == 'undefined')){
                        Ext.example.msg('提示', '请选择所属模块！');
                        return;
                    }
                    view =  Ext.widget("associatedRequiredAdd",{listDom:baseList});
                    view.down('form').down('field[name=user]').setValue(userValue);
                    view.down('form').down('field[name=module]').setValue(moduleValue);
                    view.show();
                }
            },
            'associatedRequiredWin baseList button#updateButton':{
                click:function(btn){
                    var view,baseList = btn.up('baseList'),
                        win = baseList.up('associatedRequiredWin');
                    var userValue = win.down("combo[name=user]").getValue();
                    var moduleValue = win.down("combo[name=module]").getValue();
                    if((userValue == null || userValue == 'undefined')){
                        Ext.example.msg('提示', '请选择所属用户！');
                        return;
                    }
                    if((moduleValue == null || moduleValue == 'undefined')){
                        Ext.example.msg('提示', '请选择所属模块！');
                        return;
                    }
                    view =  Ext.widget("associatedRequiredEdit",{listDom:baseList});
                    view.down('form').down('field[name=user]').setValue(userValue);
                    view.down('form').down('field[name=module]').setValue(moduleValue);
                    view.show();
                    var record = baseList.getSelectionModel().getSelection()[0];
                    if(record) {
                        view.down('hiddenfield[name=id]').setValue(record.get('id'));
                        view.down('field[name=name]').setValue(record.get('name'));
                        var fields = [];
                        Ext.Array.each(record.get('userFields'),function(userField,index,userFields){
                            fields.push(userField.fieldName);
                        });
                        view.down('itemselector[name=associatedRequired]').setValue(fields);
                    }
                }
            },
            'associatedRequiredWin baseList button#deleteButton':{
                click:function(btn){
                    this.GridDoActionUtil.doDelete(btn.up('gridpanel'),btn.subject,btn);
                }
            }
        });
    },
    onShowInsertOrUpdWin : function(o,url,propertyGridName,record){
        var me = this;
        var objectField = o.up("window");
        var objectFieldId = objectField.down("hiddenfield[name=id]").getValue();
        var insertOrUpd = 'insert';
        var readOnly = false;
        if(record != null && record != 'undefined'){
            insertOrUpd = 'update';
            readOnly = true;
        }
        var grid = objectField.down("propertygrid[name="+propertyGridName+"]");
        var win = Ext.create("Ext.window.Window",{
            modal: true,
            layout: 'fit',
            title: '添加属性',
            items: [
                {
                    bodyStyle: 'padding:10px 5px 10px',
                    defaults: {
                        labelAlign: 'right',
                        labelWidth : 50,
                        xtype: 'textfield',
                        width:250
                    },
                    items: [{
                        fieldLabel: '名称',
                        name: 'key',
                        readOnly : readOnly,
                        allowBlank : false
                    },{
                        fieldLabel: '值',
                        name: 'value',
                        allowBlank : false
                    },{
                        fieldLabel: '值类型',
                        name: 'valueType',
                        xtype: 'combo',
                        autoSelect:true,
                        forceSelection:true,
                        allowBlank : false,
                        store: [
                            ['String','字符型'],
                            ['Integer','数值型'],
                            ['Date','日期型'],
                            ['Boolean','布尔型']
                        ]
                    }],
                    buttons: [{
                        text:'保存',itemId:'addProperty',iconCls:'table_save',handler:function(o){
                            var key = win.down("textfield[name=key]").getValue();
                            var value = win.down("textfield[name=value]").getValue();
                            var valueType = win.down("combo[name=valueType]").getValue();
                            valueType = valueType == "null"?"string":valueType;
                            var paramsObj = {};
                            paramsObj.paramName = key;
                            paramsObj.paramValue = value;
                            paramsObj.paramType = valueType;
                            paramsObj.objectFieldId = objectFieldId;
                            paramsObj.insertOrUpd = insertOrUpd;
                            var params = {data:Ext.JSON.encode(paramsObj)}
                            var success = me.GridDoActionUtil.doAjax(url,params,null,false);
                            if(success){
                                if(valueType == 'Date'){
                                    value = Ext.Date.parse(value, "Y-m-d");
                                }else if(valueType == 'Integer'){
                                    value = parseFloat(value);
                                }else if(valueType == 'Boolean'){
                                    if(value.toLowerCase() == 'true'){
                                        value = true;
                                    }else{
                                        value = false;
                                    }
                                }
                                var source = grid.getSource();
                                source[key] = value;
                                win.close(); // 不能放到最后
                                grid.setSource(source);
                            }else{
                                win.close(); // 不能放到最后
                            }
                        }
                    }]
                }
            ]
        }).show();
        if(record != null && record != 'undefined'){
            var key = record.data["name"];
            var value = record.data["value"];
            win.down("textfield[name=key]").setValue(key);
            win.down("textfield[name=value]").setValue(value);
            var type = 'String';
            if (Ext.isDate(value)) {
                type = 'Date';
            } else if (Ext.isNumber(value)) {
                type = 'Integer';
            } else if (Ext.isBoolean(value)) {
                type = 'Boolean';
            }
            win.down("textfield[name=valueType]").setValue(type);
        }
    },
    // 双击列表数据或者点击修改按钮
    onShowUpdateWin :function(record,v){
        var view = Ext.widget(record.get('win'),{record:record});
        var grid = v.up('userFieldList');
        if(!grid.dbtype){
            view.down('button[autoUpdate=true]').target = 'userFieldMain userFieldList[title=全部]'
        }
        if(record.get('win') == 'fileField'){
            var userFieldPageTypeStore = view.down('combo[name=userFieldPageType]').getStore();
            // OneToOne关系不能使用多文件上传
            if(record.get('relation') == 'OneToOne'){
                userFieldPageTypeStore.removeAt(1);
                userFieldPageTypeStore.removeAt(1);
            }
        }
        view.setTitle("修改字段["+record.get('fieldName')+":"+record.get('text')+"]["+record.get('dbTypeName')+"]");
        view.down('form').loadRecord(record);
        view.down('combo[name=userFieldPageType]').setValue(record.get('pageType'));
        view.show();
        var user = view.down('hidden[name=user.id]').getValue();
        var dictField = view.down('combo[name=dict]');
        if(Ext.typeOf(dictField) != 'undefind'){
        	if(dictField){
	            var store = dictField.getStore();
                // isPaging:false 说明不分页
	            Ext.apply(store.proxy.extraParams,{user:user,isPaging:false})
	            store.load(function(records, operation, success) {
	                if(success){
	                    dictField.setValue(record.get('dict.id'));
	                }
	            });
        	}
        }
        var propertyGrid = view.down("propertygrid[name=propertyGrid]");
        Ext.Ajax.request({
            params : {userField : record.get("id")},
            url:'userField/searchUserFieldExtend',
            method:'POST',
            timeout:4000,
            success:function(response,opts){
                var result = Ext.JSON.decode(response.responseText);
                var obj = {}
                if(result != ""){
                    var data = result.data;
                    Ext.Array.each(data,function(it){
                        var key = it.paramName;
                        var value = it.paramValue;
                        var valueType = it.paramType;
                        if(valueType == 'Date'){
                            value = Ext.Date.parse(value, "Y-m-d");
                        }else if(valueType == 'Integer'){
                            value = parseFloat(value);
                        }else if(valueType == 'Boolean'){
                            if(value.toLowerCase() == 'true'){
                                value = true;
                            }else{
                                value = false;
                            }
                        }
                        obj[key] = value;
                    })
                }
                propertyGrid.setSource(obj);
            },
            failure:function(response,opts){
            }
        })
    }
});