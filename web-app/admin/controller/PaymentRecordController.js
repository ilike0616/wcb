/**
 * Created by like on 2015-05-06.
 */
Ext.define('admin.controller.PaymentRecordController', {
    extend: 'Ext.app.Controller',
    views: ['paymentRecord.List'],
    stores: ['PaymentRecordStore'],
    models: ['PaymentRecordModel'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
    ],
    init: function () {
        this.control({

        });
    }
})