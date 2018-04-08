/**
 * Created by like on 2015/8/20.
 */
Ext.define("admin.view.userField.associatedRequired.Add", {
    extend: 'Ext.window.Window',
    alias: 'widget.associatedRequiredAdd',
    requires: [
        'Ext.ux.form.ItemSelector'
    ],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    modal: true,
    width: 500,
    layout: 'anchor',
    title: '新增组合约束',
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    items: [{
                        xtype: 'textfield',
                        fieldLabel: '描述',
                        name: 'name',
                        labelWidth: 50,
                        padding: '2 5 2 5',
                        width: 450
                    }, {
                        xtype: 'itemselector',
                        name: 'associatedRequired',
                        buttons: ['add', 'remove'],
                        buttonsText: {add: "增加", remove: "移除"},
                        anchor: '100%',
                        height: 300,
                        style: 'margin:3px 5px 5px 5px',
                        imagePath: 'ext-4.2.1/examples/ux/css/images',
                        store: Ext.create('admin.store.UserFieldStore'),
                        displayField: 'text',
                        valueField: 'fieldName',
                        fromTitle: '可选字段',
                        toTitle: '已选字段'
                    }, {
                        xtype: 'hiddenfield',
                        name: 'user'
                    }, {
                        xtype: 'hiddenfield',
                        name: 'module'
                    }
                    ],
                    listeners: {
                        afterrender: function (view, eOpts) {
                            var user = view.down('hiddenfield[name=user]').getValue();
                            var module = view.down('hiddenfield[name=module]').getValue();
                            var itemSelector = view.down('itemselector[name=associatedRequired]');
                            itemSelector.fromField.store.removeAll()
                            var itemSelectorStore = itemSelector.getStore()
                            console.info(user != null);
                            console.info(module != null);
                            if(user != null && module !=null) {
                                itemSelectorStore.load({params: {user: user, module: module,bitian:false,isExcludePaging:true}});
                            }
                        }
                    },
                    buttons: [
                        {
                            text: '确定',
                            itemId: 'confirm',
                            iconCls: 'table_save',
                            listeners: {scope: me, click: me.confirm}
                        },
                        {text: '关闭', itemId: 'close', iconCls: 'table_save', listeners: {scope: me, click: me.closeWin}}
                    ]
                }
            ]
        });
        me.callParent(arguments);
    },
    confirm: function (o, e, eOpts) {
        var me = this, formObj = o.up('form');
        var form = formObj.getForm();
        var name = formObj.down('textfield[name=name]').getValue();
        var userfields = formObj.down('itemselector[name=associatedRequired]').getValue();
        var user = formObj.down('hiddenfield[name=user]').getValue();
        var module = formObj.down('hiddenfield[name=module]').getValue();
        if (userfields.length < 2) {
            Ext.example.msg('提示', '至少选择两个字段！');
            return;
        }
        var params = {user: user, module: module, userfields: userfields,name:name};
        this.GridDoActionUtil.doAjax('associatedRequired/insert', params, me.listDom.store, false)
        this.close();
    },
    closeWin: function (o, e, eOpts) {
        this.close();
    }
});