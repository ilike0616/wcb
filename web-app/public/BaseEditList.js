/**
 * Created by shqv on 2014-6-11.
 */
Ext.define('public.BaseEditList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.baseEditList',
    //true时，自动加载时还未获取主表单form的id
    renderLoad: false,
    enableSummary: false,
    isSearchView: false,
    initComponent: function () {
        var me = this,
            enableSummary = me.enableSummary;
        if (!me.columns && me.viewId) {
            Ext.Ajax.request({
                url: 'view?viewId=' + me.viewId,
                method: 'POST',
                timeout: 4000,
                async: false,
                success: function (response, opts) {
                    var view = Ext.JSON.decode(response.responseText);
                    me.columns = view.columns;
                    enableSummary = view.enableSummary;
                },
                failure: function (e, op) {
                    Ext.Msg.alert("发生错误");
                }
            });
        }
        var plugins = []
        var editBtn = []
        if (!me.isSearchView) {
            editBtn = [{
                xtype:'tbfill'
            },{
                itemId: 'add',
                iconCls: 'table_add',
                text: '添加',
                scope: this
            }, {
                itemId: 'del',
                iconCls: 'table_remove',
                text: '删除',
                disabled: true,
                autodisabled: true,
                scope: this
            }];
            plugins = [new Ext.grid.plugin.CellEditing({clicksToEdit: 1})];
        }
        Ext.applyIf(me, {
            store: Ext.create('user.store.' + me.store),
            plugins: plugins,
            tbar: editBtn
        });
        if (enableSummary == true) {
            Ext.applyIf(me, {
                features: [
                    {
                        ftype: 'summary',
                        dock: 'bottom'
                    }
                ]
            })
        }
        me.callParent(arguments);
    }
})
