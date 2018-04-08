/**
 * Created by like on 2015-04-29.
 */
Ext.define('admin.controller.EditionController',{
    extend : 'Ext.app.Controller',
    views:['edition.Main','edition.List','edition.Add','edition.Edit'] ,
    stores:['EditionStore'],
    models:['EditionModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'editionMain editionList': {
                itemdblclick: function (grid, record, item, index, e, eOpts) {
                    var tabpanel = grid.up('editionMain').down('tabpanel');
                    record = grid.getSelectionModel().getSelection()[0];
                    if (tabpanel.hidden) {
                        tabpanel.show();
                        grid.fireEvent('select', grid.getSelectionModel(), record, index, grid);
                    } else {
                        tabpanel.hide();
                    }
                },
                select: function (selectModel, record, index, eOpts) {
                    var tabpanel = eOpts.up('editionMain').down('tabpanel');
                    eOpts.up('baseList').initValues = [
                        {id: 'edtion.id', value: record.get('id')},
                        {id: 'edition.name', value: record.get('name')}
                    ];
                    if (tabpanel.hidden == false) {
                        var user = tabpanel.down('userList');
                        if (user) {
                            var store = user.getStore();
                            Ext.apply(store.proxy.extraParams, {edition: record.get('id')});
                            store.load(function (records, operation, success) {
                                user.setTitle(user.title1 + "(" + this.getTotalCount() + ")");
                            });
                        }
                    }
                }
            },
            'editionMain editionList dataview': {
                itemkeydown: function (view, record, item, index, e, eOpts) {
                    if (e.getKey() == e.ESC) {
                        view.up('editionMain').down('tabpanel').hide();
                    }
                }
            },
            // 修改页面渲染后，重新加载templateUserStore
            'editionEdit':{
                beforerender:function(o,eOpts){
                    var templateUserStore = o.down('combo[name=templateUser]').getStore();
                    Ext.apply(templateUserStore.proxy.extraParams,{useSysTpl:false});
                    templateUserStore.load();
                    var record = o.listDom.getSelectionModel().getSelection()[0];
                    o.down('combo[name=templateUser]').setValue(record.get('templateUser'));
                }
            },
            // 新增页面渲染后，重新加载templateUserStore
            'editionAdd':{
                beforerender:function(o,eOpts){
                    var templateUserStore = o.down('combo[name=templateUser]').getStore();
                    Ext.apply(templateUserStore.proxy.extraParams,{useSysTpl:false});
                    templateUserStore.load();
                }
            }
        });
    }
})