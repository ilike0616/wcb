/**
 * Created by like on 2015/7/7.
 */
Ext.define('user.controller.FareClaimsController',{
    extend : 'Ext.app.Controller',
    views:['fareClaims.Main','fareClaims.List','fareClaims.DetailEditList','fareClaims.DetailViewList'] ,
    stores:['FareClaimsStore','FareClaimsDetailStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.application.getController("AuditController");
        this.application.getController('FinanceExpenseController')
        this.control({
            'fareClaimsMain fareClaimsList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('fareClaimsMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid.getView());
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('fareClaimsMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'fareClaims',value:record.get('id')},{id:'fareClaims.subject',value:record.get('subject')}];

                    if(tabpanel.hidden==false){
                        var income = tabpanel.down('financeExpenseList');
                        if(income.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            income = income.ownerCt;
                        }
                        if(income){
                            var store = income.getStore();
                            Ext.apply(store.proxy.extraParams,{fareClaims:record.get('id')});
                            income.initValues = [{id:'fareClaims',value:record.get('id')},{id:'fareClaims.subject',value:record.get('subject')}];
                            store.load(function(records){
                                income.setTitle(income.title1+"("+records.length+")");
                            });
                        }
                    }
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=add] baseEditList': {
                afterrender: function (grid, eOpts) {
                    var me = this;
                    grid.mon(grid.store, 'update', function (store, record, operation, eOpts) {
                        me.setFormMoney(store, record, operation, eOpts, grid);
                    });
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=update] baseEditList': {
                afterrender: function (grid, eOpts) {
                    var me = this;
                    grid.mon(grid.store, 'update', function (store, record, operation, eOpts) {
                        me.setFormMoney(store, record, operation, eOpts, grid);
                    });
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=add] baseEditList button[itemId=add]': {
                click: function (btn) {
                    var store = btn.up('baseEditList').getStore();
                    store.add( {} );
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=add] baseEditList button[itemId=del]': {
                click: function (btn) {
                    var store = btn.up('baseEditList').getStore();
                    var record = btn.up('baseEditList').getSelectionModel().getSelection()[0];
                    store.remove(record);
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=update] baseEditList button[itemId=add]': {
                click: function (btn) {
                    var store = btn.up('baseEditList').getStore();
                    store.add( {} );
                }
            },
            'baseWinForm[moduleId=fare_claims][optType=update] baseEditList button[itemId=del]': {
                click: function (btn) {
                    var store = btn.up('baseEditList').getStore();
                    var record = btn.up('baseEditList').getSelectionModel().getSelection()[0];
                    store.remove(record);
                }
            },
            //出账
            'fareClaimsList button[operationId=fare_claims_finance_expense]':{
                click: function(btn){
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    var targetDom = Ext.ComponentQuery.query('financeExpenseList')[0]
                    var v = Ext.widget(btn.vw,{listDom:btn.up('baseList'),targetDom:targetDom}),
                        form = v.down('form');
                    if(form.down('field[name="subject"]')){
                        form.down('field[name="subject"]').setValue(record.get('subject'));
                    }
                    if(form.down('field[name="fareClaims"]')){
                        form.down('field[name="fareClaims"]').setValue(record.get('id'));
                    }else{
                        form.add({
                            name:'fareClaims',
                            hidden:true,
                            value:record.get('id')
                        })
                    }
                    if(form.down('field[name="fareClaims.subject"]')){
                        form.down('field[name="fareClaims.subject"]').setValue(record.get('subject'));
                    }
                    var money = record.get('money');
                    var store = Ext.create('user.store.FinanceExpenseStore');
                    Ext.apply(store.proxy.extraParams,{fareClaims:record.get('id')});
                    store.load(function(records){
                        Ext.Array.each(records,function(old){
                            if(old.get('money')){
                                money -= old.get('money');
                            }
                        });
                        if(money <=0){
                            Ext.MessageBox.confirm('提醒', '该报销已全部产生出账单，是否继续出账？', function(btn, text){
                                if (btn == 'yes'){
                                    v.show();
                                }
                            });
                        }else{
                            v.show();
                        }
                        if(form.down('field[name="money"]')){
                            form.down('field[name="money"]').setValue(money);
                        }
                    });
                }
            },
            'fareClaimsList button[operationId=fare_claims_audit_again]':{
                click:function(btn){
                    var grid = btn.up('baseList');
                    var store = grid.getStore();
                    var record = grid.getSelectionModel().getSelection()[0];
                    Ext.Ajax.request({
                        url: store.getProxy().api['reAudit'],
                        params: {id: record.get('id')},
                        method: 'POST',
                        timeout: 4000,
                        success: function (response, opts) {
                            var d = Ext.JSON.decode(response.responseText);
                            if (d.success) {
                                Ext.example.msg('提示', '操作成功，已重新送审！');
                                store.load();
                            } else {
                                console.info(d.errors);
                                Ext.example.msg('提示', '操作失败！');
                            }
                        }, failure: function (response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                        }
                    });
                }
            }
        });
    },
    setFormMoney: function (store, record, operation, eOpts, grid) {
        console.info('setFormMoney');
        if(eOpts == 'detailMoney'){
            var form = grid.up('form');
            form.down('field[name=money]').setValue(store.getSum(store.getRange(),'detailMoney'))
        }
    }
})