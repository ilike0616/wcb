/**
 * Created by like on 2015/7/10.
 */
Ext.define('user.controller.FinanceIncomeController',{
    extend : 'Ext.app.Controller',
    views:['financeIncome.List','financeIncome.Charge','financeIncome.Wrong'] ,
    stores:['FinanceIncomeStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.application.getController("AuditController");
        this.control({
            'financeIncomeCharge button[itemId=save]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','charge');
                }
            },
            'financeIncomeCharge button[itemId=forbid]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','forbid');
                }
            },
            'financeIncomeWrong button[itemId=save]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','wrong');
                }
            },
            'financeIncomeList button[operationId=finance_income_audit_again]':{
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
    }
});