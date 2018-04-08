/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.SaleChanceFollowController',{
    extend : 'Ext.app.Controller',
    views:['saleChanceFollow.Main','saleChanceFollow.List'] ,
    stores:['SaleChanceFollowStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        
    ],
    init:function() {
        this.control({
            'saleChanceFollowMain saleChanceFollowList': {
                itemdblclick: function (grid, record, item, index, e, eOpts) {
                    if (grid.getXType() == 'gridview') {
                        grid = grid.up('grid');
                    }
                    if (grid.ownerCt.lockable == true) { // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('saleChanceFollowMain').down('tabpanel');
                    if (tabpanel.hidden) {
                        tabpanel.show();
                        grid.fireEvent('select', grid.getSelectionModel(), record, index, grid.getView());
                    } else {
                        tabpanel.hide();
                    }
                },
                select: function (selectModel, record, index, eOpts) {
                    var tabpanel = selectModel.view.up('saleChanceFollowMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if (baseList.ownerCt.lockable == true) { // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{
                        id: 'saleChanceFollow',
                        value: record.get('id')
                    }, {id: 'saleChanceFollow.subject', value: record.get('subject')}];

                    if (tabpanel.hidden == false) {
                        var sfa = tabpanel.down('sfaExecuteMain');
                        if (sfa.ownerCt.lockable == true) { // 如果有锁定列，则应该取父grid
                            sfa = sfa.ownerCt;
                        }
                        if (sfa) {
                            var store = sfa.down('sfaExecuteSfaList').getStore();
                            Ext.apply(store.proxy.extraParams, {
                                moduleId: 'sale_chance_follow',
                                linkId: record.get('id')
                            });
                            sfa.initValues = [{id: 'moduleId', value: 'sale_chance_follow'}, {
                                id: 'linkId',
                                value: record.get('id')
                            }, {id: 'linkName', value: record.get('subject')}];
                            store.load(function (records, operation, success) {
                                sfa.setTitle(sfa.title1 + "(" + this.getTotalCount() + ")");
                            });
                            sfa.down('sfaExecuteEventList').getStore().removeAll();
                            sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                        }
                    }
                }
            }
        });
    }
})
