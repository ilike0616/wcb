/**
 * Created by guozhen on 2015/3/9.
 */
Ext.define('public.BaseExport', {
    extend: 'Ext.window.Window',
    alias: 'widget.baseExport',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 900,
    height: 335,
    modal : true,
    layout: 'border',
    buttonAlign:'center',
    initComponent: function() {
        var me = this,
            items = me.items;
        var listDom = me.listDom;
        var title = listDom.up('tabpanel').getActiveTab().title;
        me.title = "导出"+title;
        if(!items){
            Ext.Ajax.request({
                //url:'viewDetail/searchEnableField?view='+me.parentWin.viewId,
                url:'base/searchExportField?moduleId='+listDom.moduleId,
                method:'POST',
                timeout:4000,
                async: false,
                success:function(response,opts){
                    items = Ext.JSON.decode(response.responseText).data;
                } ,
                failure:function(e,op){
                    Ext.Msg.alert("发生错误");
                }
            })
        }
        Ext.applyIf(me, {
            items: [
                {
                    region:'center',
                    border : 0,
                    items : [{
                        xtype:'baseFieldSet',
                        checkboxToggle:true,
                        forceFit:true,
                        title: '请选择导出字段',
                        margin : '0 5 0 5',
                        overflowY: 'auto',
                        height : 250,
                        items : [{
                            xtype: 'checkboxgroup',
                            name : 'chooseField',
                            columns: 4,
                            defaults : {
                                padding : '5px 0px 5px 0px'
                            },
                            items: items
                        }]
                    }]
                }
            ],
            buttons: [
                {text:'导出',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                {text:'关闭',itemId:'close',iconCls:'cancel',listeners : {scope : me,click : me.closeWin}}
            ]
        })
        me.callParent(arguments);
    },
    confirm : function(o, e, eOpts){
        var me = this;
        // checkbox组
        var checkboxGroup = me.down("fieldset checkboxgroup[name=chooseField]");
        // 选中的checkbox的值
        var checkedValue = checkboxGroup.getValue().field;
        if(checkedValue != null && checkedValue != 'undefined' && checkedValue.length > 0){
            var msg = Ext.Msg.show({
                width: 350,
                height: 100,
                icon:'ext_download',
                iconHeight: 50,
                layout: 'fit',
                title: '处理中...',
                msg: "正在生成文件，请稍后..."
            });
            var checkedLabel = []
            Ext.Array.each(checkboxGroup.getChecked(), function(checkbox) {
                var boxLabel = checkbox.boxLabel;
                var spanIndex = checkbox.boxLabel.indexOf('</span>');
                if(spanIndex > -1){ // 去掉"<span style='color:red;font-weight:bold'>*</span>"
                    boxLabel = checkbox.boxLabel.substring(spanIndex+7)
                }
                checkedLabel.push(boxLabel);
            });
            var searchValue = "";
            var searchCondition = []
            if(Ext.typeOf(me.listDom) != 'undefined'){
                var store = me.listDom.store;
                var tempSearchCondition = store.proxy.extraParams.searchCondition;
                if(tempSearchCondition){
                    tempSearchCondition = Ext.JSON.decode(tempSearchCondition);
                    Ext.Array.each(tempSearchCondition, function(condition) {
                        searchCondition.push(Ext.JSON.encode(condition));
                    });
                }
                var records = me.listDom.getSelectionModel().getSelection();
                if(records && records.length > 0){
                    var ids = [];
                    Ext.Array.each(records,function(record,index){
                        ids.push(record.get('id'));
                    })
                    var conditionObj = new Object();
                    conditionObj.searchFieldName = "id";
                    conditionObj.searchFieldOperation = "in";
                    conditionObj.searchFieldValue = ids;
                    conditionObj.dbType = "java.lang.String";
                    searchCondition.push(Ext.JSON.encode(conditionObj));
                }
                searchCondition.join(",");
                searchCondition = "["+searchCondition+"]";
                if(Ext.typeOf(store.filters.items[0]) != "undefined"){
                    searchValue = store.filters.items[0].value;
                }
            }

            checkedValue = {checkedValue: "["+checkedValue+"]",checkedLabel:"["+checkedLabel+"]",
                searchCondition:searchCondition,searchValue: searchValue,export: 'export'};
            Ext.Ajax.request({
                url:me.listDom.store.getProxy().api['read'],
                params:checkedValue,
                method:'POST',
                timeout:600000, // 此处时间要长一点,10分钟
                async:true,
                success:function(response,opts){
                    var d = Ext.JSON.decode(response.responseText);
                    if(d.success){
                        me.close();
                        msg.hide();
                        UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,2);
                        window.location.href = d.httpFilePath;
                        Ext.defer(function(){
                            UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,1);
                        },3000)
                    }else{
                    }
                },
                failure:function(response,opts){
                    var errorCode = "";
                    if(response.status){
                        errorCode = 'error:'+response.status;
                    }
                    Ext.example.msg('提示', '操作失败！'+errorCode);
                }
            });
        }else{
            Ext.example.msg('提示', '请选择要导出的字段！');
        }
    },
    closeWin : function(o, e, eOpts){   // 关闭
        this.close();
    }
});