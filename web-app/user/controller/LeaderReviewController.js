/**
 * Created by like on 2015/8/13.
 */
Ext.define('user.controller.LeaderReviewController',{
    extend : 'Ext.app.Controller',
    views:['leaderReview.List','leaderReview.View','leaderReview.Reply'] ,
    stores:['LeaderReviewStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [{
        ref : 'leaderReviewList',
        selector : 'leaderReviewList'
    }],
    init:function() {
        this.control({
            'leaderReviewList button[operationId=leader_review_read]': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    this.toRead(grid, btn, true);
                }
            },
            'leaderReviewList button[operationId=leader_review_view]': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    this.toRead(grid, btn, false);
                }
            },
            'leaderReviewList button[operationId=leader_review_reply]': {
                click: function (btn) {
                    Ext.widget('leaderReviewReply').show();
                }
            },
            "leaderReviewView reviewReplyList":{
                beforerender:function(grid){
                    var win = grid.up('window'),
                        form = grid.up('form'),
                        listDom = win.listDom,
                        record = listDom.getSelectionModel().getSelection()[0];
                    Ext.apply(grid.getStore().proxy.extraParams,{review:record.get('id')});
                    grid.getStore().load();
                }
            },
            'leaderReviewReply button[itemId=reply]': {
                click : function(btn) {
                    var store = this.getLeaderReviewList().getStore();
                    var frm = btn.up('form');
                    var win = btn.up('window');
                    if (!frm.getForm().isValid())
                        return;
                    var record = this.getLeaderReviewList().getSelectionModel().getSelection()[0];
                    frm.submit({
                        waitMsg : '正在提交数据',
                        waitTitle : '提示',
                        url : store.getProxy().api['reply'],
                        method : 'POST',
                        params : {review:record.get('id')},
                        submitEmptyText : false,
                        success : function(form, action) {
                            store.load();
                            Ext.example.msg('提示', '保存成功');
                            if (win) {
                                win.close();
                            }
                        },
                        failure : function(form, action) {
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            },
            "leaderReviewList button[operationId=leader_review_link_view]":{
                click:function(btn){
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    var module_id = record.get('module').id;
                    var viewId = '',modelName = '';
                    Ext.Ajax.request({
                        url:"view/getSearchViewId",
                        params:{id:module_id},
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
    },
    //设置为已读
    'toRead': function (grid, btn, isMsg) {
        var store = grid.getStore();
        var records = grid.getSelectionModel().getSelection();
        var data = [];
        Ext.Array.each(records, function (model) {
            if (model.get('id')) {
                data.push(Ext.JSON.encode(model.get('id')));
            }
        });
        Ext.Ajax.request({
            url: store.getProxy().api['toRead'],
            params: {ids: "[" + data.join(",") + "]"},
            method: 'POST',
            timeout: 4000,
            success: function (response, opts) {
                var d = Ext.JSON.decode(response.responseText);
                store.load();
                if (isMsg) {
                    if (d.success) {
                        Ext.example.msg('提示', '标记为已读操作成功');
                    } else {
                        console.info(d.errors);
                        Ext.example.msg('提示', '标记为已读操作失败！');
                    }
                }
            }, failure: function (response, opts) {
                var errorCode = "";
                if (response.status) {
                    errorCode = 'error:' + response.status;
                }
                Ext.example.msg('提示', '标记为已读操作失败！' + errorCode);
            }
        });
    }
});