/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.controller.AddBalanceController',{
    extend : 'Ext.app.Controller',
    views:['addBalance.List'] ,
    stores:['AddBalanceStore'],
    models:['AddBalanceModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
        });
    }
})
