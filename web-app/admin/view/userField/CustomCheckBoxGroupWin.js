/**
 * Created by guozhen on 2014/10/31.
 */
Ext.define('admin.view.userField.CustomCheckBoxGroupWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.customCheckBoxGroupWin',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 950,
    height: 450,
    modal : true,
    layout: 'border',
    dbtype : '',    // 用于筛选记录
    userValue : '', // 上个页面选择的用户
    moduleValue : '', // 上个页面选择的模块
    isRelatedField : false, // 是否启用关联对象,true：是；false：否
    initComponent: function() {
        var me = this;
        title = me.title,
        dbtype = me.dbtype,
        userValue = me.userValue,
        moduleValue = me.moduleValue,
        isRelatedField = me.isRelatedField,
        Ext.applyIf(me, {
            items: [
                {
                    region:'north',
                    border : 0,
                    items: [{
                        xtype: 'fieldset',
                        title: '必选条件',
                        layout : 'column',
                        columns : 2,
                        margin : '0 5 0 5',
                        defaults :{
                            labelWidth : 70,
                            padding : '3 3 3 3'
                        },
                        items: [{
                            xtype: 'combo',
                            fieldLabel: '所属用户',
                            labelAlign: 'right',
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
                            labelAlign: 'right',
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
                                    me.searchRelatedObject(me);
                                }
                            }
                        },{
                            xtype: 'combo',
                            labelAlign: 'right',
                            fieldLabel: '关联对象',
                            name: 'relatedModel',
                            emptyText: '请选择...',
                            autoSelect : true,
                            forceSelection:true,
                            hidden : !me.isRelatedField, // 是否启用关联对象,true：是；false：否
                            store: [['','']],
                            listeners : {
                                change : function(){
                                    me.searchField(this,'relatedModel');
                                }
                            }
                        }]
                    }]
                },
                {
                    region:'center',
                    border : 0,
                    items : [{
                        xtype:'baseFieldSet',
                        checkboxToggle:true,
                        title: '可选字段',
                        margin : '0 5 0 5',
                        overflowY: 'auto',
                        height : 290,
                        items : [{
                            xtype: 'checkboxgroup',
                            name : 'chooseField',
                            columns: 3,
                            defaults : {
                                padding : '5px 0px 5px 0px'
                            },
                            items: [
                            ]
                        }]
                    }]
                }
            ],
            buttons: [
                {text:'启用',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
            ]
        })
        me.callParent(arguments);
    },
    searchField : function(o,from){  // 查询可选字段
        var me = this;
        var win = o.up("customCheckBoxGroupWin");
        var userValue = win.down("combo[name=user]").getValue();
        var moduleValue = win.down("combo[name=module]").getValue();
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
        var relatedModelValue = "";
        var relatedModelText = "";
        if(isRelatedField){   // 找出当前模块包含的关联对象
            var relatedModelCombo = win.down("combo[name=relatedModel]");
            if(from != 'relatedModel' && moduleValue != null){
                var store = me.searchRelatedObject(me);
                relatedModelCombo.getStore().removeAll();
                relatedModelCombo.getStore().add(store);
            }else{
                relatedModelValue = relatedModelCombo.getValue();
            }
            if(relatedModelValue == null || relatedModelValue == 'undefined' || relatedModelValue == ''){
                return;
            }
            relatedModelText = relatedModelCombo.getRawValue();
        }

        Ext.Ajax.request({
            params : {user : userValue,module : moduleValue,dbtype : dbtype,relatedModelValue : relatedModelValue,relatedModelText : relatedModelText},
            url:me.url,
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
                    win.down("checkboxgroup[name=chooseField]").removeAll();
                    win.down("checkboxgroup[name=chooseField]").add(items);
                }
            },
            failure:function(response,opts){
            }
        })
    },
    confirm : function(o, e, eOpts){    // 确定
        var me = this;
        // 必选条件下拉框的值
        var userValue = me.down("combo[name=user]").getValue();
        var moduleValue = me.down("combo[name=module]").getValue();
        var relatedModelValue = me.down("combo[name=relatedModel]").getValue();
        // checkbox组
        var checkboxGroup = me.down("fieldset checkboxgroup[name=chooseField]");
        // 选中的checkbox的值
        var checkedValue = checkboxGroup.getValue();
        if(checkedValue != null && checkedValue != 'undefined'){
            checkedValue = {user:userValue,module:moduleValue,data: checkedValue,relatedModelValue : relatedModelValue};
            this.GridDoActionUtil.doAjax('userField/enableField',checkedValue,me.store);
            me.close();
        }else{
            Ext.example.msg('提示', '请选择要启用的字段！');
        }
    },
    closeWin : function(o, e, eOpts){   // 关闭
        var me = this;
        me.close();
    },
    searchRelatedObject : function(win){    // 查询关联对象
        if(win == null || win == 'undefined') return;
        var store = [['','']];
        var moduleValue = win.down("combo[name=module]").getValue();
        if(moduleValue != null && moduleValue != 'undefined'){
            Ext.Ajax.request({
                params : {module : moduleValue},
                url:'userField/searchRelatedObject',
                method:'POST',
                async: false,
                timeout:4000,
                success:function(response,opts){
                    var result = Ext.JSON.decode(response.responseText);
                    if(result != ""){
                        win.down("combo[name=relatedModel]").reset();
                        var data = result.data;
                        Ext.Array.each(data,function(it){
                            store.push([it.dbType,it.fieldName]);
                        })
                    }
                },
                failure:function(response,opts){
                }
            })
        }
        return store;
    }
});