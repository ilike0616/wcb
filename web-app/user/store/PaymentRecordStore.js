/**
 * Created by like on 2015-05-05.
 */
Ext.define('user.store.PaymentRecordStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('payment_record'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'paymentRecord/list',
            remove: 'paymentRecord/delete',
            update: "paymentRecord/update",
            insert: "paymentRecord/insert",
            save: 'paymentRecord/save'
        }
    },
    autoLoad:false
})