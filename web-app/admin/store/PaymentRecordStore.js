/**
 * Created by like on 2015-05-06.
 */
Ext.define('admin.store.PaymentRecordStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.PaymentRecordModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'paymentRecord/listForAdmin',
            remove: 'paymentRecord/delete',
            update: "paymentRecord/update",
            insert: "paymentRecord/insert",
            save: 'paymentRecord/save'
        }
    },
    autoLoad:true
});