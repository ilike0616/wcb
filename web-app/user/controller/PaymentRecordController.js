/**
 * Created by like on 2015-05-05.
 */
Ext.define('user.controller.PaymentRecordController',{
    extend : 'Ext.app.Controller',
    views:['paymentRecord.List'] ,
    stores:['PaymentRecordStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [],
    init:function() {
        this.control({
        });
    }
})