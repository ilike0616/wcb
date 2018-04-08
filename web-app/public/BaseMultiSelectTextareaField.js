/**
 * 选择多数据组件
 * Created by guozhen on 2014/10/15.
 */
Ext.define('public.BaseMultiSelectTextareaField', {
    extend: 'Ext.form.Panel',
    alias: 'widget.baseMultiSelectTextareaField',
    border:0,
    layout: 'anchor',
    defaults: {
        msgTarget: 'side' //qtip title under side
    },
    storeName: '',
    initComponent: function() {
        var me = this,allowBlank=true;
        this.storeName = me.store;
        var fromType = 'user';  // 管理员还是用户层调用,默认用户层
        if(me.fromType != '' && Ext.typeOf(me.fromType) != 'undefined'){
            fromType = me.fromType;
        }
        me.store = Ext.create(fromType+'.store.'+me.store+'');
        me.store.load();
        if(me.allowBlank==false){
        	allowBlank = false;
        }
        // 设置宽度
        var width = 405;
        if(Ext.typeOf(me.width) != 'undefined' && me.width != null){
            width = me.width;
        }
        var disabled = false;
        if(Ext.typeOf(me.disabled) != 'undefined' && me.disabled != null){
            disabled = me.disabled;
        }
        Ext.applyIf(me, {
            items : [
                {
                    xtype:'fieldcontainer',
                    fieldLabel: me.fieldLabel,
                    labelWidth: 90,
                    items: [{
                        xtype: 'button',
                        iconCls : 'dept_table',
                        disabled : disabled,
                        listeners : {
                            scope : me,
                            click : me.showObjectListWin
                        }
                    },{
                        xtype: 'button',
                        iconCls : 'table_remove',
                        disabled : disabled,
                        margin:'0 10 0 10',
                        tooltip:'清空',
                        listeners : {
                            scope : me,
                            click : me.clearSelectedValue
                        }
                    }]
                },{
                    xtype:'fieldcontainer',
                    style : {
                        marginLeft : '90px'
                    },
                    items: [{
                        xtype : 'textareafield',
                        grow : true,
                        width:width,
                        readOnly:true,
                        name : me.name
                    }]
                },{
                    xtype:'hiddenfield',
                    name:me.hiddenName
                }
            ]
        });

        me.callParent(arguments);
    },
    showObjectListWin : function(){
        var me = this;
        var operateBtn = true;  //关闭操作按钮
        var enableBasePaging = true;    //false 关闭翻页按钮
        var enableSearchField = true;   //false 关闭搜索框
        var enableComplexQuery = true;  //false 关闭查询功能
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
        Ext.create('Ext.window.Window', {
            title: me.fieldLabel+'列表',
            height: 480,
            width: 730,
            layout: 'fit',
            modal : true,
            items: {
                url:me.url,
                xtype:'basePopList',
                store: me.store,
                viewId: me.viewId,
                columns: [
                    {
                        text:'员工姓名',
                        dataIndex:'name'
                    }
                ],
                extraParams:{fromFlag:me.fromFlag,userId:me.user},
                mode: 'MULTI',
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
        var selData = grid.getSelectionModel().getSelection();
        if(selData == null || Ext.typeOf(selData) == 'undefined'){
            Ext.Msg.alert("提示信息", "请选择记录！");
            return;
        }

        // 得到所有选中的记录，然后取出名字和ID
        var showNames = [];
        var hiddens = [];
        Ext.each(selData,function(record){
            showNames.push(record.get('name'));
            hiddens.push(record.get('id'));
        })
        if(showNames.length > 0){
            showNames.join(",");
            hiddens.join(",");
        }

        // 原有已经选择的数据
        var originHiddens = me.down('hiddenfield[name='+me.hiddenName+']').getValue();
        var originShowNames = me.down('textareafield[name='+me.name+']').getValue();
        if(originHiddens != null && originHiddens != '') hiddens = originHiddens + "," + hiddens
        if(originShowNames != null && originShowNames != '') showNames = originShowNames + "," +showNames

        me.down('hiddenfield[name='+me.hiddenName+']').setValue(hiddens);
        me.down('fieldcontainer textareafield[name='+me.name+']').setValue(showNames);

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
    clearSelectedValue : function(){
        var me = this;
        me.down('fieldcontainer textareafield[name='+me.name+']').setValue(null);
        me.down('hiddenfield[name='+me.hiddenName+']').setValue('');
    }

})
