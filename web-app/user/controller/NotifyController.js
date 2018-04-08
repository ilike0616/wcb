/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.controller.NotifyController', {
    extend: 'Ext.app.Controller',
    views: ['notify.List'],
    stores: ['NotifyStore'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
    ],
    init: function () {
        this.control({
            'notifyList button[operationId=notify_read]': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    this.toRead(grid, btn, true);
                }
            },
            'notifyList button[operationId=notify_view]': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    this.toRead(grid, btn, false);
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
})