/**
 * Created by guozhen on 2015/3/9.
 */
Ext.define('public.BaseImport', {
    extend: 'Ext.window.Window',
    alias: 'widget.baseImport',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 900,
    height: 335,
    modal : true,
    layout: 'border',
    title : '导入',
    buttonAlign:'center',
    initComponent: function() {
        var me = this,
            items = me.items;
        var listDom = me.listDom;
        var title = listDom.up('tabpanel').getActiveTab().title;
        me.title = "导入"+title;
        if(!items){
            Ext.Ajax.request({
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
                        title: '请选择生成模板字段',
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
                {text:'直接导入',itemId:'import',iconCls:'table_save',listeners : {scope : me,click : me.import}},
                {text:'生成模板',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                {text:'关闭',itemId:'close',iconCls:'cancel',listeners : {scope : me,click : me.closeWin}}
            ]
        })
        me.callParent(arguments);
    },
    import : function(o,e,eOpts){
        var me = this;
        var listDom = me.listDom;
        var store = listDom.store;
        me.close();
        var importPanel = Ext.create('Ext.window.Window', {
            title: '导入文件',
            width: 400,
            bodyPadding: 10,
            resizable: false,
            modal : true,
            items: [{
                xtype:'form',
                border:0,
                items:[{
                    xtype: 'filefield',
                    name: 'importFile',
                    fieldLabel:'文件',
                    labelWidth:50,
                    msgTarget: 'side',
                    allowBlank: false,
                    width: 330,
                    buttonText: '上传',
                    regex:/\.xlsx|.xls$/i,
                    regexText: '只允许上传【.xlsx或.xls】文件'
                }/*,{
                    xtype: 'checkboxfield',
                    boxLabel: '是否替换匹配到的数据信息',
                    padding:'10 0 0 80',
                    name: 'isReplaceExistData',
                    inputValue: '1'
                }*/]
            }],
            buttons: [{
                text: '导入',
                handler: function() {
                    var form = this.up('window').down('form').getForm();
                    if(form.isValid()){
                        form.submit({
                            url: 'base/importObject?moduleId='+listDom.moduleId,
                            waitMsg: '正在导入数据，请稍后...',
                            method: 'POST',
                            success: function(fp, o) {
                                importPanel.close();
                                if(o.result.success){
                                    var msg = "";
                                    if(o.result.errorFlag && o.result.errorFlag == 'errorAll'){
                                        msg = "本次导入全部失败，失败原因如下：<br><br>缺少必填字段列表"+ o.result.loseFieldTexts;
                                    }else{
                                        store.load();
                                        msg = "总条数："+o.result.totalNum+"， 成功导入："+ o.result.successNum+"， 成功更新："+ o.result.updateNum+"， 失败条数："+ o.result.failedNum;
                                        if(parseInt(o.result.failedNum) > 0){
                                            msg += "<br><br><span style='color: red;margin-left: 80px;'>错误列表已下载，请查看！</span>";
                                        }
                                    }
                                    Ext.Msg.show({
                                        title: '导入信息',
                                        msg: msg,
                                        width: 550,
                                        modal:true,
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.MessageBox.INFO
                                    });
                                    if(parseInt(o.result.failedNum) > 0){
                                        window.location.href = o.result.httpFilePath;
                                        UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,2);
                                        Ext.defer(function(){
                                            UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,1);
                                        },300)
                                    }
                                }else{
                                    Ext.Msg.alert('导入信息','导入异常！');
                                }
                            },
                            failure: function(form, action) {
                                alert("出错了！");
                            }
                        });
                    }
                }
            }]
        });
        importPanel.show();
    },
    confirm : function(o, e, eOpts){
        var me = this;
        // checkbox组
        var checkboxGroup = me.down("fieldset checkboxgroup[name=chooseField]");
        // 选中的checkbox的值
        var checkedValue = checkboxGroup.getValue().field;
        if(checkedValue != null && checkedValue != 'undefined' && checkedValue.length > 0){
            var checkedLabel = []
            var checkedBitianValue = []
            Ext.Array.each(checkboxGroup.getChecked(), function(checkbox) {
                var boxLabel = checkbox.boxLabel;
                var spanIndex = checkbox.boxLabel.indexOf('</span>');
                if(spanIndex > -1){ // 去掉"<span style='color:red;font-weight:bold'>*</span>"
                    boxLabel = checkbox.boxLabel.substring(spanIndex+7)
                }
                checkedLabel.push(boxLabel);
                if(checkbox.readOnly) checkedBitianValue.push(checkbox.inputValue);
            });
            checkedValue = {checkedValue: "["+checkedValue+"]",checkedLabel:"["+checkedLabel+"]",checkedBitianValue:"["+checkedBitianValue+"]"};
            Ext.Ajax.request({
                url:'base/makeTemplateFile',
                params:checkedValue,
                method:'POST',
                timeout:4000,
                async:true,
                success:function(response,opts){
                    var d = Ext.JSON.decode(response.responseText);
                    if(d.success){
                        window.location.href = d.httpFilePath;
                        UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,2);
                        Ext.defer(function(){
                            UnloadConfirm.set(UnloadConfirm.MSG_UNLOAD,1);
                        },300)
                    }else{
                        Ext.example.msg('提示', d.httpFilePath);
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

            //win.close();
        }else{
            Ext.example.msg('提示', '请选择要生成模板的字段！');
        }
    },
    closeWin : function(o, e, eOpts){   // 关闭
        this.close();
    }
});