/**
 * Created by guozhen on 2014/10/31.
 */
Ext.define('admin.view.userField.CustomExtFieldWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.customExtFieldWin',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 650,
    height: 210,
    modal : true,
    layout: 'border',
    userValue : '', // 上个页面选择的用户
    moduleValue : '', // 上个页面选择的模块
    listDom:null,
    initComponent: function() {
        var me = this;
        title = me.title;
        userValue = me.userValue;
        moduleValue = me.moduleValue;
        listDom = me.listDom;
        Ext.applyIf(me, {
            items: [
                {
                    region:'north',
                    border : 0,
                    items: [{
                        xtype: 'toolbar',
                        layout : 'column',
                        columns : 2,
                        margin : '1 5 5 10',
                        border : 0,
                        defaults:{
                            labelWidth:70,
                            labelAlign: 'left'
                        },
                        items: [{
                            xtype: 'combo',
                            fieldLabel: '所属用户',
                            name: 'user',
                            emptyText: '请选择...',
                            autoSelect : true,
                            forceSelection:true,
                            displayField : 'name',
                            valueField : 'id',
                            queryMode: 'local',
                            store:Ext.create('admin.store.UserStore',{
                                pageSize:9999999,
                                listeners:{
                                    load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                        if(me.userValue != "" && me.userValue != null){
                                            me.down("combo[name=user]").setValue(me.userValue);
                                        }
                                    }
                                }
                            }),
                            listeners : {
                                change : function(){
                                    me.searchField(this,'user');
                                }
                            }
                        },{
                            xtype: 'combo',
                            fieldLabel: '所属模块',
                            name: 'module',
                            emptyText: '请选择...',
                            autoSelect : true,
                            forceSelection:true,
                            displayField : 'moduleName',
                            valueField : 'id',
                            queryMode: 'local',
                            store:Ext.create('admin.store.ModuleListStore',{
                                listeners:{
                                    load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                        if(me.moduleValue != "" && me.moduleValue != null){
                                            me.down("combo[name=module]").setValue(me.moduleValue);
                                        }
                                    }
                                }
                            }),
                            listeners : {
                                change : function(){
                                    me.searchField(this,'module');
                                }
                            }
                        }]
                    }]
                },
                {
                    region:'center',
                    border : 0,
                    items : [{
                        xtype:'fieldset',
                        title: '扩展字段',
                        items : [{
                            xtype: 'toolbar',
                            layout:'column',
                            border:0,
                            margin : '0 5 5 2',
                            defaults:{
                                xtype:'button',
                                height:50,
                                restCount:0, // 剩余数量
                                dbType:null,
                                disabled:true,
                                columnWidth:0.20,
                                listeners:{
                                    click:function(btn){
                                        me.operateButton(btn);
                                    }
                                }
                            },
                            items: [
                                {
                                    text:'字符型(0)',
                                    name:'cString'
                                },{
                                    text:'整型(0)',
                                    name:'cInteger'
                                },{
                                    text:'浮点型(0)',
                                    name:'cDouble'
                                },{
                                    text:'布尔型(0)',
                                    name:'cBoolean'
                                },{
                                    text:'日期型(0)',
                                    name:'cDate'
                                }
                            ]
                        }]
                    }]
                }
            ],
            buttons: [
                {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
            ]
        })
        me.callParent(arguments);
    },
    searchField : function(o,from){  // 查询可选字段
        var me = this;
        var win = o.up("customExtFieldWin");
        var userValue = win.down("combo[name=user]").getValue();
        var moduleValue = win.down("combo[name=module]").getValue();
        if(from == 'user'){
            // 根据用户,重新加载模块
            var moduleStore = me.down('combo[name=module]').getStore();
            Ext.apply(moduleStore.proxy.extraParams,{user:userValue});
            moduleStore.load();
        }
        // 只有手动选择的情况下，userValue为空，才给提示
        if((userValue == null || userValue == 'undefined') && (me.userValue == null || me.userValue == 'undefined')){
            Ext.example.msg('提示', '请选择所属用户！');
            return;
        }
        if((moduleValue == null || moduleValue == 'undefined') && (me.moduleValue == null || me.moduleValue == 'undefined')){
            Ext.example.msg('提示', '请选择所属模块！');
            return;
        }
        // 因为前面页面用户和模块都选了，所以给本页面的用户和模块默认赋值时，都执行了本方法。
        // 因此两者必须都不为空，否则直接返回，什么都不提示
        if((userValue == null && moduleValue != null) || (userValue != null && moduleValue == null)){
            return;
        }
        Ext.Ajax.request({
            params : {user : userValue,module : moduleValue},
            url:me.url,
            method:'POST',
            timeout:4000,
            success:function(response,opts){
                var result = Ext.JSON.decode(response.responseText);
                if(result != ""){
                    var data = result.data;
                    Ext.Array.each(data,function(it){
                        var dbType = it.dbType;
                        var restCount = it.restCount; // 剩余数量
                        var button;
                        if(dbType == 'java.lang.String'){
                            button = win.down('fieldset toolbar button[name=cString]');
                            button.setText('字符型('+restCount+')');
                        }else if(dbType == 'java.lang.Integer'){
                            button = win.down('fieldset toolbar button[name=cInteger]');
                            button.setText('整型('+restCount+')');
                        }else if(dbType == 'java.lang.Double'){
                            button = win.down('fieldset toolbar button[name=cDouble]');
                            button.setText('浮点型('+restCount+')');
                        }else if(dbType == 'java.lang.Boolean'){
                            button = win.down('fieldset toolbar button[name=cBoolean]');
                            button.setText('布尔型('+restCount+')');
                        }else if(dbType == 'java.util.Date'){
                            button = win.down('fieldset toolbar button[name=cDate]');
                            button.setText('日期型('+restCount+')');
                        }
                        if(button != null){
                            button.restCount = restCount;
                            button.dbType = dbType;
                            if(button.disabled){ // button不可用，个数大于0，则button应为可用
                                if(restCount > 0){
                                    button.setDisabled(false);
                                }
                            }else{ // button可用，个数小于等于0，则button应为不可用
                                if(restCount <= 0){
                                    button.setDisabled(true);
                                }
                            }
                        }
                    })
                }
            },
            failure:function(response,opts){
            }
        })
    },
    operateButton : function(btn){    // 点击按钮执行的操作
        var me = this;
        var win = btn.up("customExtFieldWin");
        var userValue = win.down("combo[name=user]").getValue();
        var moduleValue = win.down("combo[name=module]").getValue();
        var btnName = btn.name;
        var btnDBType = btn.dbType;
        var widgetName = "";
        if(btnDBType == 'java.lang.String'){
            widgetName = "extStringField";
        }else if(btnDBType == 'java.lang.Integer'){
            widgetName = "extIntegerField";
        }else if(btnDBType == 'java.lang.Double'){
            widgetName = "extFloatField";
        }else if(btnDBType == 'java.lang.Boolean'){
            widgetName = "extBooleanField";
        }else if(btnDBType == 'java.util.Date'){
            widgetName = "extDateField";
        }
        var view = Ext.widget(widgetName,{
            user:userValue,
            module:moduleValue,
            dbType:btn.dbType,
            extBtn:btn,
            listDom:listDom
        })
        view.show();
    },
    closeWin : function(o, e, eOpts){   // 关闭
        var me = this;
        me.close();
    }
});