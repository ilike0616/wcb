/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('agent.controller.AddBalanceController',{
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
                    var addBalanceType = o.addBalanceType;
                    Ext.apply(store.proxy.extraParams,{fromWhere:'agent',kind:addBalanceKind,addBalanceType:addBalanceType});
                    store.load();
                }
            }
        });
    }
})
