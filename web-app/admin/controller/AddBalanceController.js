/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.controller.AddBalanceController',{
    extend : 'Ext.app.Controller',
    views:['addBalance.Main','addBalance.List'] ,
    stores:['AddBalanceStore'],
    models:['AddBalanceModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'addBalanceMain addBalanceList':{
                afterrender:function( o,eOpts){
                    var store = o.getStore();
                    var addBalanceKind = o.addBalanceKind;
                    Ext.apply(store.proxy.extraParams,{kind:addBalanceKind});
                    store.load();
                }
            }
        });
    }
})
