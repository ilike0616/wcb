/**
 * 弹出列表选择
 * Created by guozhen on 2014/10/15.
 */
Ext.define('public.BaseSpecialTextfieldTree', {
    extend: 'Ext.form.Panel',
    alias: 'widget.baseSpecialTextfieldTree',
    requires : [
        'public.BasePopTreeList'
    ],
    layout: {
        type: 'column',
        columns: 2
    },
    defaults: {
        msgTarget: 'side', //qtip title under side
        xtype: 'textfield'
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items : [
                {
                    fieldLabel : me.fieldLabel,
                    name : me.name,
                    submitValue:false,
                    columnWidth:0.98
                },
               {
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
            ],
            listeners : {
                render : function(o,epts){
                    var hiddenFieldId = {
                        xtype : 'hiddenfield',
                        name : me.hiddenName
                    };
                    // 根据页面上是否存在me.hiddenName对应的隐藏变量，如果存在，则不添加，防止重复。否则，则生成一个
                    var win = o.ownerCt.ownerCt;
                    var hiddenField = win.down("hiddenfield[name="+me.hiddenName+"]");
                    if(hiddenField == null || Ext.typeOf(hiddenField) == 'undefined'){
                        me.add(hiddenFieldId);
                    }
                }
            }
        });

        me.callParent(arguments);
    },
    showObjectListWin : function(o){
        var me = this;
        var winForm = o.ownerCt.ownerCt;
        var filters = new Array();
        var filterSelfAndChildrenId = "";
        if(me.filterSelfAndChildren == true){
            filterSelfAndChildrenId = winForm.down("hiddenfield[name=id]").getValue();
            filters = [{
                property: 'filterSelfAndChildrenId',
                value: filterSelfAndChildrenId
            }]
        }
        if(Ext.typeOf(me.store) == 'string'){
            me.store = Ext.create('user.store.'+me.store,{
                filters: filters
            });
        }else{
            var findConditionValue = "";
            if(Ext.typeOf(me.findCondition) != 'undefined'){
                findConditionValue = winForm.down(me.findCondition[0].xtype+"[name="+me.findCondition[1].name+"]").getValue();
            }
            Ext.apply(me.store.proxy.extraParams,{filterSelfAndChildrenId: filterSelfAndChildrenId,findCondition:findConditionValue});
            me.store.load();
        }
        var dataIndexName = me.name;
        if(Ext.typeOf(me.name)=='undefined' || me.name == ""){
            alert("参数有误！");
            return;
        }
        var index = me.name.indexOf(".");
        if(index > -1){
            dataIndexName = me.name.substring(index+1);
        }
        var width = 300;
        if(Ext.typeOf(me.windowWidth) != 'undefined'){
            width = me.windowWidth;
        }
        Ext.create('Ext.window.Window', {
            title: me.fieldLabel+'列表',
            height: 480,
            width: width,
            layout: 'fit',
            modal : true,
            items: {
                xtype:'baseTreeGrid',
                store: me.store,
                columns : [{
                    xtype: 'treecolumn',
                    text: me.fieldLabel,
                    width: 270,
                    dataIndex: dataIndexName
                }],
                operateBtn : false,
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
        var grid = winForm.down("baseTreeGrid");
        var selData = grid.getSelectionModel().getSelection()[0];
        if(selData == null || Ext.typeOf(selData) == 'undefined'){
            Ext.Msg.alert("提示信息", "请选择记录！");
            return;
        }
        var parentFormObj = me.ownerCt.ownerCt;
        var name = me.name;
        var nameIndex = me.name.indexOf(".");
        if(nameIndex > -1){
            name = me.name.substr(nameIndex+1);
        }
        var textField = parentFormObj.down("textfield[name="+me.name+"]")
        var hiddenField = parentFormObj.down("hiddenfield[name="+me.hiddenName+"]");
        var newHiddenValue = selData.data['id'];
        textField.setValue(selData.data[name]);
        console.info(newHiddenValue);
        hiddenField.setValue(newHiddenValue);
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
        var grid = winForm.down("baseTreeGrid");
        // 必须清空原有的条件，不然下次打开还会带上上次查询的条件
        Ext.apply(grid.getStore().proxy.extraParams,{searchCondition:""});
        grid.getStore().clearFilter();
    }

})
