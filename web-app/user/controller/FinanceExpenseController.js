/**
 * Created by like on 2015/7/10.
 */
Ext.define('user.controller.FinanceExpenseController',{
    extend : 'Ext.app.Controller',
    views:['financeExpense.List','financeExpense.Add','financeExpense.Charge','financeExpense.Wrong'] ,
    stores:['FinanceExpenseStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.application.getController("AuditController");
        this.control({
            'financeExpenseCharge button[itemId=save]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','charge');
                }
            },
            'financeExpenseCharge button[itemId=forbid]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','forbid');
                }
            },
            'financeExpenseWrong button[itemId=save]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'),'','wrong');
                }
            },
            'financeExpenseList button[operationId=finance_expense_audit_again]':{
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