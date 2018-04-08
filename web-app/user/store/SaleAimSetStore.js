/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.SaleAimSetStore',{
    extend : 'Ext.data.Store',
    model:'user.model.SaleAimSetModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'saleAim/setList',
            save: 'saleAim/update'
        }
    },
    autoLoad:true
})