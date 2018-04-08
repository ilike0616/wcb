/**
 * Created by like on 2015/8/12.
 */
Ext.define('user.controller.ReviewController',{
    extend : 'Ext.app.Controller',
    views:['review.List','review.Add','review.ReplyList'] ,
    stores:['ReviewStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
            /**
             * 所有模块统一的添加方法
             */
            'button#review_save':{
                click:function(btn){
                    var form = btn.up('form'),
                        win = btn.up('window');
                    if (!form.getForm().isValid()) return;
                    form.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        url:'review/insert',
                        method: 'POST',
                        params: {moduleId:form.moduleId},
                        timeout:90000,
                        submitEmptyText : false,
                        success: function(form, action) {
                            Ext.example.msg('提示', '保存成功');
                            if(win){
                                win.close();
                            }
                        },
                        failure:function(form,action){
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            if(result.msg) {
                                Ext.example.msg('提示', result.msg);
                            }
                        }
                    });
                }
            },
            "baseWinForm[moduleId=review][optType=view] reviewReplyList":{
                beforerender:function(grid){
                    var win = grid.up('window'),
                        form = grid.up('form'),
                        listDom = win.listDom,
                        record = listDom.getSelectionModel().getSelection()[0];
                    Ext.apply(grid.getStore().proxy.extraParams,{review:record.get('id')});
                    grid.getStore().load();
                }
            },
            "reviewList button[operationId=review_link_view]":{
                click:function(btn){
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    var moduleId = record.get('module').moduleId;
                    var viewId = '',modelName = '';
                    Ext.Ajax.request({
                        url:"view/getSearchViewId",
                        params:{moduleId:moduleId},
                        method:'POST',
                        async:false,
                        timeout:20000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            viewId = d.searchView;
                            modelName = d.modelName
                        }
                    });
                    //创建查看窗口
                    var view =  Ext.widget("baseWin",{
                        items: [
                            {
                                xtype: 'baseForm',
                                defaults:{
                                    readOnly:true
                                },
                                viewId:viewId,
                                buttons:[{
                                    text:'关闭',iconCls:'cancel',
                                    handler:function(btn){
                                        btn.up('window').close();
                                    }
                                }]
                            }
                        ]
                    });
                    view.show();
                    //加载数据
                    Ext.Ajax.request({
                        url:modelName+"/list",
                        params:{id:record.get('linkId')},
                        method:'POST',
                        async:false,
                        timeout:20000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            if(d.success){
                                view.down('form').form.setValues(d.data[0]);
                            }

                        }
                    });
                }
            }
        });
    }
});