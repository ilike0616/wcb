/**
 * Created by like on 2015-05-05.
 */
Ext.define('user.view.paymentRecord.List', {
    extend: 'public.BaseList',
    alias: 'widget.paymentRecordList',
    autoScroll: true,
    store:Ext.create('user.store.PaymentRecordStore'),
    title: '扣费记录',
    split:true,
    forceFit:true,
    renderLoad:true,
    enableSearchField:false,
    viewId:'PaymentRecordList'
});