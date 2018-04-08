/**
 * Created by like on 2015/7/9.
 */
Ext.define('user.controller.FinanceAccountController',{
    extend : 'Ext.app.Controller',
    views:['financeAccount.List'] ,
    stores:['FinanceAccountStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
    }
})